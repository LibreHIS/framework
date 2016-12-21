package ims.framework;

import ims.framework.enumerations.FormAccess;
import ims.framework.interfaces.IDynamicNavigation;
import ims.framework.interfaces.INavForm;
import ims.framework.interfaces.INavigation;

public abstract class DynamicNavigation implements IDynamicNavigation
{
	public abstract boolean shouldRenderForm();
	public abstract void renderForm(StringBuffer sb) throws Exception;
	public abstract void renderData(StringBuffer sb) throws Exception;
	public abstract INavForm getForm(int id);
	public abstract Integer getUniqueNavigationId(int formId);
	public abstract INavigation getNavigation();
	public abstract FormAccess getFormAccess(INavForm navForm) throws Exception;
}
