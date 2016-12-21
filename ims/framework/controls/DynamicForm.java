package ims.framework.controls;

import ims.framework.Control;
import ims.framework.FormName;
import ims.framework.delegates.DynamicFormModeChanged;
import ims.framework.enumerations.FormMode;

public abstract class DynamicForm extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected DynamicFormModeChanged formModeChangeDelegate;
	
	public abstract void open(FormName form);
	public abstract void open(FormName form, Object[] params);	
	public abstract void clear();
	public abstract FormName getPreviousNonDialogFormName();
	public abstract FormMode getOpenedFormMode();
	public abstract FormName getOpenedForm();
	public abstract void setReadOnly(boolean value);
	public abstract boolean isReadOnly();
	public abstract boolean supportsDataOperations();
	public abstract String[] validateData();
	public abstract void saveData() throws Exception;
	
	public void setFormModeChangedEvent(DynamicFormModeChanged delegate)
	{
		formModeChangeDelegate = delegate;
	}
}