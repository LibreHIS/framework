package ims.framework.cn;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ims.domain.SessionData;
import ims.domain.impl.DomainImplFlyweightFactory;
import ims.framework.Control;
import ims.framework.FormBridge;
import ims.framework.FormInfo;
import ims.framework.FormUiLogic;
import ims.framework.SessionConstants;
import ims.framework.UILogic;
import ims.framework.controls.DynamicForm;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.interfaces.IAppForm;
import ims.framework.utils.SizeInfo;

public final class FormLoader implements ims.framework.FormLoader,  Serializable
{
	private static final long serialVersionUID = 1L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(FormLoader.class);	
		
	public FormLoader(HttpServletRequest request, HttpSession session)
	{
		setContext(request, session);
	}
	
	private void setContext(HttpServletRequest request, HttpSession session)
	{
		this.request = request;		
		this.session = session;
	}
		
	public FormUiLogic loadComponent(int value, IAppForm parentForm, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex, boolean skipContextValidation) throws Exception
	{
		return load(value, parentForm, startControlID, runtimeSize, parentControl, startTabIndex, true, skipContextValidation, null, null);
	}
	public FormUiLogic loadDynamicForm(int value, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex, String callerIdentifier, DynamicForm parentDynamicForm) throws Exception
	{
		return load(value, startControlID, runtimeSize, parentControl, startTabIndex, callerIdentifier, parentDynamicForm);
	}
	public FormUiLogic load(int value) throws Exception
	{
		SessionData sessData = (SessionData) this.session.getAttribute(SessionConstants.SESSION_DATA);
		IAppForm registeredForm = getRegisteredForm(value);		
		return load(value, getRegisteredForm(value), 0, (registeredForm.isDialog() ? null : sessData.runtimeClientFormArea.get()), null, 0, false, false, null, null);		
	}
	public FormUiLogic load(int value, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex, String callerIdentifier, DynamicForm parentDynamicForm) throws Exception
	{
		return load(value, getRegisteredForm(value), startControlID, runtimeSize, parentControl, startTabIndex, false, false, callerIdentifier, parentDynamicForm);
	}
	private FormUiLogic load(int value, IAppForm parentForm, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex, boolean isComponent, boolean skipContextValidation, String callerIdentifier, DynamicForm parentDynamicForm) throws Exception
	{
		UIFactory uiFactory = new UIFactory(this.request, this.session);		
		Context ctx = new Context(this.session);
		UIEngine uiEngine = new UIEngine(this.request, this.session, callerIdentifier, parentDynamicForm);
		
		// Save objects in session, so we can free them later
		SessionData sessData = (SessionData) this.session.getAttribute(SessionConstants.SESSION_DATA);
		sessData.context.set(ctx);

		ims.framework.Form form = uiFactory.getForm();
	
		//IAppRole role = sessData.role.get();	
		//Configuration configuration = sessData.configurationModule.get();
		//boolean readOnly = configuration.isFormReadOnly(role, value,ctx);
		//form.setReadOnly(readOnly);

		IAppForm registeredForm = getRegisteredForm(value);
		
		isComponent = registeredForm.isComponent();
		String boClassName = registeredForm.getRieBoClassName();
		
		if(!isComponent)
			sessData.rieBoClassName.set(boClassName);
				
		// Setup the help link
		form.setHelpLink(registeredForm.getHelpLink());		
		Object genForm = null;
		Integer prevFormId = sessData.previousNonDialogFormID.get();
		if (prevFormId != null && value == prevFormId.intValue())
		{			
			//TODO: Rethink how to re-use GenForm when haven't moved to new form.
			//genForm = sessData.form.get();
		}
		
		if(form.getFormInfo() == null)
		{
			if(isComponent)
				form.setFormInfo(FormBridgeFlyweightFactory.getInstance().createFormInfo(this.getGenFormPackageName(parentForm).concat(".FormInfo"), parentForm.getFormId()));
			else
				form.setFormInfo(FormBridgeFlyweightFactory.getInstance().createFormInfo(this.getGenFormPackageName(registeredForm).concat(".FormInfo"), registeredForm.getFormId()));
		}				
		if (genForm == null)
		{
			genForm = FormBridgeFlyweightFactory.getInstance().createForm(this.getGenFormPackageName(registeredForm).concat(".GenForm"), this, form, parentForm, uiFactory, ctx, skipContextValidation, startControlID, runtimeSize, parentControl, startTabIndex);
		}
		
		UILogic uilogic = UILogicFactory.getInstance().createUILogic(registeredForm.getModule(), registeredForm.getLogicClass());
		Class domainInterfaceClass = uilogic.getDomainInterface();
		String domainClassName = null;
		if (domainInterfaceClass.getName().equals("ims.dto.DTODomain")) 
		{
			domainClassName = "ims.dto.DTODomainImplementation";
		}
		else if (domainInterfaceClass.getName().equals("ims.domain.DomainInterface")) 
		{
			domainClassName = "ims.domain.impl.DomainImpl";
		}
		else 
		{
			domainClassName = registeredForm.getDomainImpl();
		}		
		
		Object domain = null;
		if (domainClassName != null && domainClassName.length() > 0)
		{
			ims.domain.DomainSession domainSession = ims.domain.DomainSession.getSession(this.session);
			
			DomainImplFlyweightFactory factory = DomainImplFlyweightFactory.getInstance();
			if (LocalLogger.isDebugEnabled())
				LocalLogger.debug("DomainImplFlyweightFactory instance retrieved - " + factory);
			domain = factory.create(domainClassName, domainSession);
			if (LocalLogger.isDebugEnabled())
				LocalLogger.debug("DomainImplFlyweightFactory created and domain retrieved");
			}
		//sessData.domain.set(domain);

		java.lang.reflect.Method method = null;
		if(isComponent)
		{
			method = uilogic.getClass().getMethod("setContext", new Class[] { ims.framework.UIComponentEngine.class, genForm.getClass(), domainInterfaceClass });
			method.invoke(uilogic, new Object[] { new UIComponentEngine(uiEngine, ((FormBridge)genForm).getUniqueIdentifier()), genForm, domain } );
		}
		else
		{
			method = uilogic.getClass().getMethod("setContext", new Class[] { ims.framework.UIEngine.class, genForm.getClass(), domainInterfaceClass} );
			method.invoke(uilogic, new Object[] { uiEngine, genForm, domain });
		}
		
		return new FormUiLogic(form, uilogic);
	}
	public final FormInfo getInfo(int value) throws Exception
	{
		Class formInfoClass = Class.forName(this.getGenFormPackageName(this.getRegisteredForm(value)).concat(".FormInfo"));
		Constructor c = formInfoClass.getConstructor(new Class[] { Integer.class });
		return (FormInfo)c.newInstance(new Object[] { value });
	}		
	public void clearAllLocalContext() 
	{
		Context ctx = new Context(this.session);
		String[] localContextNames = ctx.getAllLocalVariablesNames();
		for(int x = 0; x < localContextNames.length; x++)
		{
			ctx.remove(localContextNames[x]);
		}
	}
	public void clearContext(int value, boolean includeGlobalVariables) throws Exception
	{
		Context ctx = new Context(this.session);
		FormInfo formInfo = this.getInfo(value);

		// clearing all form local context
		String[] localContextNames = ctx.getLocalVariablesNames(formInfo.getLocalVariablesPrefix());
		for(int x = 0; x < localContextNames.length; x++)
		{
			ctx.remove(localContextNames[x]);
		}
		
		// clearing all custom controls local context
		// No longer neeeded. MM / 2008.09
		/*FormInfo[] componentsFormInfo = formInfo.getComponentsFormInfo();
		for(int x = 0; x < componentsFormInfo.length; x++)
		{
			String[] localContextNamesForComponents = ctx.getLocalVariablesNames(formInfo.getLocalVariablesPrefix() + componentsFormInfo[x].getLocalVariablesPrefix());
			for(int y = 0; y < localContextNamesForComponents.length; y++)
			{
				ctx.remove(localContextNamesForComponents[y]);
			}
		}*/
		
		if (includeGlobalVariables)
		{	
			String[] hold = getAllContextVariablesIncludingComponents(formInfo);
			String[] globalContextNames = ctx.getGlobalVariablesNames();
			for (int i = 0; i < globalContextNames.length; ++i)
			{
				String name = globalContextNames[i];
				boolean flag = true;
				for (int j = 0; j < hold.length; ++j)
				{
					if (name.equals(hold[j]))
					{
						flag = false;
						break;
					}
				}
				if (flag)
					ctx.remove(name);
			}
		}
	}	
	private String[] getAllContextVariablesIncludingComponents(FormInfo formInfo) 
	{
		String[] result = formInfo.getContextVariables();		
		
		FormInfo[] componentsInfo = formInfo.getComponentsFormInfo();
		if(componentsInfo != null)
		{
			for(int x = 0; x < componentsInfo.length; x++)
			{
				result = addToArrayAndFilter(result, componentsInfo[x].getContextVariables());
			}
		}
		
		return result;
	}
	
	private String[] addToArrayAndFilter(String[] initial, String[] toAdd)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		if(initial != null)
		{
			for(int x = 0; x < initial.length; x++)
			{
				if(!result.contains(initial[x]))
					result.add(initial[x]);
			}
		}
		if(toAdd != null)
		{
			for(int x = 0; x < toAdd.length; x++)
			{
				if(!result.contains(toAdd[x]))
					result.add(toAdd[x]);
			}
		}
		
		String[] resultArray = new String[result.size()];
		for(int x = 0; x < result.size(); x++)
		{
			resultArray[x] = result.get(x);
		}
		
		return resultArray;
	}

	private IAppForm getRegisteredForm(int value) throws ConfigurationException 
	{
	    SessionData sessData = (SessionData) this.session.getAttribute(SessionConstants.SESSION_DATA);
		Map map = sessData.configurationModule.get().getRegisteredForms();
		IAppForm registeredForm = (IAppForm) map.get(new Integer(value));
		if (null == registeredForm)
			throw new ConfigurationException("Form is not registered. Form ID:"+value); 
		
		return registeredForm;
	}

	private String getGenFormPackageName(IAppForm registeredForm) 
	{
		return registeredForm.getGenFormPackageName();
	}
	
	private transient HttpServletRequest request;	
	private transient HttpSession session;	
}