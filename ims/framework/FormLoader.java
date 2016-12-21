package ims.framework;

import ims.framework.controls.DynamicForm;
import ims.framework.interfaces.IAppForm;
import ims.framework.utils.SizeInfo;

public interface FormLoader
{
	public FormUiLogic loadComponent(int value, IAppForm parentForm, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex, boolean skipContextValidation) throws Exception;
	public FormUiLogic loadDynamicForm(int value, int startControlID, SizeInfo runtimeSize, Control parentControl, int startTabIndex, String callerIdentifier, DynamicForm parentDynamicForm) throws Exception;
    public FormUiLogic load(int value) throws Exception;
    public FormInfo getInfo(int value) throws Exception;
    public void clearContext(int value, boolean both) throws Exception;
}
