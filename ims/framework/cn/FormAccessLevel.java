package ims.framework.cn;

import ims.domain.SessionData;
import ims.framework.Context;
import ims.framework.enumerations.ContextQueryItem;
import ims.framework.enumerations.FormAccess;
import ims.framework.interfaces.IContextEvalProvider;
import ims.framework.interfaces.IFormAccessValidator;
import ims.framework.interfaces.INavForm;
import ims.vo.ValueObjectRef;

public final class FormAccessLevel extends ims.framework.FormAccessLevel
{
	private static final long serialVersionUID = 1L;
	
	public FormAccessLevel(ims.framework.FormAccessLoader formAccessLoader, ims.domain.ContextEvalFactory evalFactory)
	{
		super(formAccessLoader, evalFactory);
	}	
	public boolean isAccessibleFromNavigation(int formID) throws Exception
	{
		IFormAccessValidator formAccess = formAccessLoader.getFormAccessValidator(formID);
		if(formAccess == null)
			return false;
		return formAccess.isAccessible();
	}
	public boolean isAccessibleFromNavigation(INavForm navForm, SessionData sessData) throws Exception  // FWUI-1770 sessData passed in 
	{
		if(getFormAccess(navForm, sessData) == FormAccess.NO_ACCESS)
			return false;		
		IFormAccessValidator formAccess = formAccessLoader.getFormAccessValidator(navForm.getAppForm().getFormId());
		if(formAccess == null)
			return false;
		return formAccess.isAccessible();
	}	
	public FormAccess getFormAccess(INavForm navForm,  SessionData sessData) throws Exception
	{
		FormAccess result = FormAccess.READ_WRITE;
		
		if (evalFactory.hasContextEvalProvider())
		{
			IContextEvalProvider provider = evalFactory.getContextEvalProvider();
			Boolean isPatientDead=null;
			Boolean isEpisodeEnded=null;
			// FWUI-1770 
			// Check if the RIP and EpisodeEnded values are in sessionData to prevent
			// calling provider again
			if (sessData != null)
			{
				isPatientDead =sessData.isPatientRIP.get();
				isEpisodeEnded=sessData.isEpisodeEnded.get();
			}
			
			if (isPatientDead == null )  // call provider to check if patient is in context
			{
				if (sessData != null)
				{
					Context ctx = sessData.context.get();
					ValueObjectRef voRef = (ValueObjectRef)ctx.get(ContextQueryItem.PATIENT.getKey());
					if (voRef != null)
						isPatientDead = provider.isPatientRip();
					else
						isPatientDead=false;
					
					sessData.isPatientRIP.set(isPatientDead);
				}
				else
				{
					isPatientDead = provider.isPatientRip();
				}
			}

			if (isEpisodeEnded == null)
			{
				if (sessData != null)
				{
					Context ctx = sessData.context.get();
					
					ValueObjectRef voRef = (ValueObjectRef)ctx.get(ContextQueryItem.EPISODE_OF_CARE.getKey());
					if (voRef != null)
						isEpisodeEnded = provider.isEpisodeEnded();
					else
						isEpisodeEnded=false;

					sessData.isEpisodeEnded.set(isEpisodeEnded);
				}
				else
					isEpisodeEnded = provider.isEpisodeEnded();
			}

			if(isEpisodeEnded != null && isEpisodeEnded)
				result = navForm.getFormAccessForEpisEnd();
			
			if(result == FormAccess.NO_ACCESS)
				return result;
			
			if(isPatientDead != null && isPatientDead && (result == FormAccess.READ_WRITE || navForm.getFormAccessForRip() != FormAccess.READ_WRITE))					
				result = navForm.getFormAccessForRip();				
		}
		
		if(navForm.isReadOnly() && result == FormAccess.READ_WRITE)
			result = FormAccess.READ_ONLY;	
		
		if(result == FormAccess.READ_WRITE)
		{
			IFormAccessValidator formAccess = formAccessLoader.getFormAccessValidator(navForm.getAppForm().getFormId());
			if (formAccess != null && !formAccess.isAccessible())
				result = FormAccess.NO_ACCESS;
			else if(formAccess != null && formAccess.isReadOnly())
				result = FormAccess.READ_ONLY;
		}
			
		return result;
	}
}
