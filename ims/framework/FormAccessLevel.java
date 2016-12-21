package ims.framework;

import java.io.Serializable;

import ims.domain.SessionData;
import ims.framework.enumerations.FormAccess;
import ims.framework.interfaces.INavForm;

public abstract class FormAccessLevel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected FormAccessLoader formAccessLoader;
	protected ims.domain.ContextEvalFactory evalFactory;
	
	public FormAccessLevel(FormAccessLoader formAccessLoader, ims.domain.ContextEvalFactory evalFactory)
	{
		this.formAccessLoader = formAccessLoader;
		this.evalFactory = evalFactory;
	}	
	public abstract boolean isAccessibleFromNavigation(int formID) throws Exception;
	public abstract boolean isAccessibleFromNavigation(INavForm navForm, SessionData sessData) throws Exception;
	public abstract FormAccess getFormAccess(INavForm navForm, SessionData sessData) throws Exception;
}
