package ims.framework.cn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ims.configuration.ConfigFlag;
import ims.domain.DomainSession;
import ims.domain.SessionData;
import ims.domain.adapters.DomainContextAdapter;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.factory.ContextEvalFactory;
import ims.framework.ContextReference;
import ims.framework.ContextReferences;
import ims.framework.SessionConstants;
import ims.framework.cn.adapters.HttpContextAdapter;
import ims.framework.enumerations.ContextQueryItem;
import ims.framework.enumerations.FormMode;
import ims.framework.interfaces.IContextAdapter;
import ims.framework.interfaces.IContextEvalProvider;
import ims.vo.ImsCloneable;
import ims.vo.ValueObjectRef;

import javax.servlet.http.HttpSession;

public class Context implements ims.framework.Context, Serializable
{
	private static final long serialVersionUID = 1L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(Context.class);
	private static final boolean useHardcodedClearing = false;
	
	public Context(HttpSession session)
	{
		setContext(session);
	}
	public Context(DomainSession session)
	{
		setContext(session);
	}	
	private final void setContext(HttpSession session)
	{
		this.session = new HttpContextAdapter(session);
	}
	private final void setContext(DomainSession session)
	{
		this.session = new DomainContextAdapter(session);
	}

	public Object get(String key)
	{
		return session.getAttribute(key);
	}	
	public void put(String key, Object value)
	{
		Object currVal = get(key);
		Object refContext=null;
		if (value!=null&&key.equals(ContextQueryItem.CURRENT_CARE_CONTEXT.getKey())&&ims.configuration.gen.ConfigFlag.FW.USE_REFERRAL_CARECONTEXT.getValue())
		{
			refContext = getContextEvalProvider().getReferralCareContextForCareContext((ValueObjectRef)value);
			if(refContext!=null)
			{
				value=refContext;
			}
			// WDEV-21070 Now set the CatsReferral into context
			ValueObjectRef catsReferral = getContextEvalProvider().getCatsReferralForCareContext((ValueObjectRef)value);
			setCatsReferral(catsReferral);
		}
		if (value == null)
			this.session.removeAttribute(key);
		else
			this.session.setAttribute(key, value);
		
		if (currVal == null && value == null)
			return;		
		
		if(!useHardcodedClearing)
		{
			List<ContextReference> contextReferences = ContextReferences.getReferences();
			ContextReference changedContext = null; 
			for(int x = 0; x < contextReferences.size(); x++)
			{
				if(contextReferences.get(x).getKey().equals(key))
				{
					changedContext = contextReferences.get(x);
					break;
				}
			}
			
			if(changedContext != null)
			{
				LocalLogger.info("Context '" + changedContext.getName() + "' was " + (value == null ? "cleared" : "changed") + ".");
				if (value == null || (value != null && currVal == null) || (currVal != null && value != null && !currVal.equals(value)))
				{
					for(int x = 0; x < changedContext.getReferences().size(); x++)
					{
						LocalLogger.info("Context reference '" + changedContext.getReferences().get(x).getName() + "' was cleared because '" + changedContext.getName() + "' was " + (value == null ? "cleared" : "changed") + ".");
						this.put(changedContext.getReferences().get(x).getKey(), null);
					}
				}
			}
		}
		
		if (key.equals(ContextQueryItem.PATIENT.getKey()))
		{
			if (value == null || (value != null && currVal == null) || (currVal != null && value != null && !currVal.equals(value)))
			{
				if(useHardcodedClearing)
				{
					this.session.removeAttribute(ContextQueryItem.EPISODE_OF_CARE.getKey());
					this.session.removeAttribute(ContextQueryItem.CURRENT_CARE_CONTEXT.getKey());
					this.session.removeAttribute(ContextQueryItem.CURRENT_CARE_CONTEXT_TYPE.getKey());
					this.session.removeAttribute(ContextQueryItem.CLINICAL_CONTACT.getKey());
					this.session.removeAttribute(ContextQueryItem.CATS_REFERRAL.getKey());
					this.session.removeAttribute(ContextQueryItem.PATIENT_ICP_RECORD.getKey());
					
				}
				
			}
			setPatient((ValueObjectRef)value);
		}
		else if (key.equals(ContextQueryItem.EPISODE_OF_CARE.getKey()))
		{
			if (value == null || (value != null && currVal == null) || (currVal != null && value != null && !currVal.equals(value)))
			{
				if(useHardcodedClearing)
				{
					this.session.removeAttribute(ContextQueryItem.CURRENT_CARE_CONTEXT.getKey());
					this.session.removeAttribute(ContextQueryItem.CURRENT_CARE_CONTEXT_TYPE.getKey());
					this.session.removeAttribute(ContextQueryItem.CLINICAL_CONTACT.getKey());
				}
			}
			if (value != null && (currVal == null || !currVal.equals(value)))
			{
				ValueObjectRef patRef = getContextEvalProvider().getPatientForEpisodeOfCare((ValueObjectRef)value);
				setPatient(patRef);							
			}
		}
		else if (key.equals(ContextQueryItem.CURRENT_CARE_CONTEXT.getKey()))
		{
			if (value == null || (value != null && currVal == null) || (currVal != null && value != null && !currVal.equals(value)))
			{
				if(useHardcodedClearing)
				{
					this.session.removeAttribute(ContextQueryItem.CLINICAL_CONTACT.getKey());
				}
			}
			if (value != null && (currVal == null || !currVal.equals(value)))
			{
				ValueObjectRef episRef = getContextEvalProvider().getEpisodeOfCareForCareContext((ValueObjectRef)value);
				setEpis(episRef);		
				
				ValueObjectRef patRef = getContextEvalProvider().getPatientForEpisodeOfCare(episRef);
				setPatient(patRef);							
			}
						
		}
		else if (key.equals(ContextQueryItem.CLINICAL_CONTACT.getKey()))
		{
			if (value != null && (currVal == null || !currVal.equals(value)))
			{
				ValueObjectRef ctxRef = getContextEvalProvider().getCareContextForClinicalContact((ValueObjectRef)value);
				setCtx(ctxRef);					
				
				ValueObjectRef episRef = getContextEvalProvider().getEpisodeOfCareForCareContext(ctxRef);
				setEpis(episRef);		
				
				ValueObjectRef patRef = getContextEvalProvider().getPatientForEpisodeOfCare(episRef);
				setPatient(patRef);										
			}
		}
	}
	
	private void setPatient(ValueObjectRef patRef)
	{
		IContextEvalProvider contextEvalProvider = getContextEvalProvider();
		if(!session.isDomainOnlySession())
		{
			contextEvalProvider.execute(patRef);
			contextEvalProvider.setPatientInfoAndIcons(patRef);			
			
			if (patRef != null && ConfigFlag.DOM.READ_AUDIT_ENABLED.getValue())
			{
				contextEvalProvider.recordReadAudit(patRef, "Patient Clinical");				
			}
		}
		
		if (patRef == null)
		{
			this.session.removeAttribute(ContextQueryItem.PATIENT.getKey());
			// FWUI-1770 - we need to clear
			SessionData sessData = (SessionData)this.session.getAttribute(SessionConstants.SESSION_DATA);
			if (sessData != null)
			{
				sessData.isPatientRIP.set(null);
				sessData.isEpisodeEnded.set(null);
			}
		}
		else
		{
			this.session.setAttribute(ContextQueryItem.PATIENT.getKey(), patRef);
			
			// FWUI-1770 set the patientRIP value and clear episode
			SessionData sessData = (SessionData)this.session.getAttribute(SessionConstants.SESSION_DATA);
			if (sessData != null)
			{
				sessData.isPatientRIP.set(contextEvalProvider.isPatientRip());
				sessData.isEpisodeEnded.set(null);
			}
		}
		
		
	}

	private void setEpis(ValueObjectRef episRef)
	{
		if (episRef == null)
		{
			this.session.removeAttribute(ContextQueryItem.EPISODE_OF_CARE.getKey());
			
			// FWUI-1770  clear episode Ended value
			SessionData sessData = (SessionData)this.session.getAttribute(SessionConstants.SESSION_DATA);
			if (sessData != null)
				sessData.isEpisodeEnded.set(null);
		}
		else
		{
			this.session.setAttribute(ContextQueryItem.EPISODE_OF_CARE.getKey(), episRef);
			// FWUI-1770 set the EpisodeEnded value
			SessionData sessData = (SessionData)this.session.getAttribute(SessionConstants.SESSION_DATA);
			if (sessData != null)
			{
				ims.domain.ContextEvalFactory evalFactory = new ContextEvalFactory(sessData.domainSession.get());
				IContextEvalProvider provider = evalFactory.getContextEvalProvider();
				sessData.isEpisodeEnded.set(provider.isEpisodeEnded());
			}

		}
	}
	
	//WDEV-21070
	private void setCatsReferral(ValueObjectRef catsReferralRef)
	{
		if(catsReferralRef==null)
		{
			this.session.removeAttribute(ContextQueryItem.CATS_REFERRAL.getKey());
		}
		else
		{
			this.session.setAttribute(ContextQueryItem.CATS_REFERRAL.getKey(),catsReferralRef);
		}
	}
	
	private void setCtx(ValueObjectRef ctxRef)
	{
		if (ctxRef == null)
		{
			this.session.removeAttribute(ContextQueryItem.CURRENT_CARE_CONTEXT.getKey());
		}
		else
		{
			this.session.setAttribute(ContextQueryItem.CURRENT_CARE_CONTEXT.getKey(), ctxRef);			
		}
	}

	
	private IContextEvalProvider getContextEvalProvider()
	{
		ims.domain.ContextEvalFactory evalFactory = new ContextEvalFactory(getDomainSession());
		if (!evalFactory.hasContextEvalProvider())
			throw new DomainRuntimeException("No ContextEvalProvider has been configured.");
		IContextEvalProvider prov = evalFactory.getContextEvalProvider();

		return prov;
	}
	
	private DomainSession getDomainSession()
	{
		SessionData sessData = (SessionData)session.getAttribute(SessionConstants.SESSION_DATA);
		if (sessData == null)
			throw new DomainRuntimeException("SessionData cannot be null");
		
		DomainSession domSess = sessData.domainSession.get();
		if (domSess == null)
			throw new DomainRuntimeException("DomainSession in SessionData cannot be null");
		
		return domSess;		
	}

	public void remove(String key)
	{
		put(key, null);
	}
	public String[] getAllLocalVariablesNames()
	{
		ArrayList<String> variablesNames = new ArrayList<String>();
		
		Iterator<String> e = this.session.getAttributeNames();
		while (e.hasNext())
		{
			String name = e.next();
			if (name.startsWith("_lv_"))
				variablesNames.add(name);
		}

		String[] result = new String[variablesNames.size()];
		for(int x = 0; x < variablesNames.size(); x++)
		{
			result[x] = variablesNames.get(x);
		}
		return result;
	}
	public String[] getLocalVariablesNames(String prefix)
	{
		if(prefix == null || prefix.trim().length() == 0)
			throw new RuntimeException("Invalid prefix for local context variables");
		
		ArrayList<String> variablesNames = new ArrayList<String>();
		
		Iterator<String> e = this.session.getAttributeNames();
		while (e.hasNext())
		{
			String name = e.next();
			if (name.startsWith(prefix))
				variablesNames.add(name);
		}

		String[] result = new String[variablesNames.size()];
		for(int x = 0; x < variablesNames.size(); x++)
		{
			result[x] = variablesNames.get(x);
		}
		return result;
	}
	public String[] getGlobalVariablesNames()
	{
		ArrayList<String> variablesNames = new ArrayList<String>();
		
		Iterator<String> e = this.session.getAttributeNames();
		while (e.hasNext())
		{
			String name = e.next();
			if (name.startsWith("_cv_"))
				variablesNames.add(name);
		}
		
		String[] result = new String[variablesNames.size()];
		for(int x = 0; x < variablesNames.size(); x++)
		{
			result[x] = variablesNames.get(x);
		}
		return result;
	}
	public String[] getPersistentGlobalVariablesNames()
	{
		ArrayList<String> variablesNames = new ArrayList<String>();
		
		Iterator<String> e = this.session.getAttributeNames();
		while (e.hasNext())
		{
			String name = e.next();
			if (name.startsWith("_cvp_"))
				variablesNames.add(name);
		}
		
		String[] result = new String[variablesNames.size()];
		for(int x = 0; x < variablesNames.size(); x++)
		{
			result[x] = variablesNames.get(x);
		}
		return result;
	}
    private transient IContextAdapter session;    
	
	public boolean isValidContextType(Class clazz) 
	{
		if (ImsCloneable.class.isAssignableFrom(clazz)) 
			return true;
		
		if (clazz.equals(ims.framework.FormName.class)) 
			return true;
		
		if (clazz.equals(String.class) ||
			clazz.equals(Integer.class) ||
			clazz.equals(Boolean.class) ||
			clazz.equals(Float.class) ||
			clazz.equals(FormMode.class) ||
			clazz.equals(Double.class)) 
			return true;
		
		if (clazz.equals(String[].class) ||
			clazz.equals(Integer[].class) ||
			clazz.equals(Boolean[].class) ||
			clazz.equals(Float[].class) ||
			clazz.equals(FormMode[].class) ||
			clazz.equals(Double[].class)) 
			return true;
		
		return false;
	}
}