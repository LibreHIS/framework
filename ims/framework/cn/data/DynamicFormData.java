package ims.framework.cn.data;

import java.util.ArrayList;
import java.util.List;

import ims.framework.Control;
import ims.framework.Form;
import ims.framework.FormName;
import ims.framework.FormUiLogic;
import ims.framework.cn.events.IMenuEvent;
import ims.framework.controls.DynamicForm;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.FormMode;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.interfaces.IDynamicFormDataOperations;
import ims.framework.utils.SizeInfo;

public class DynamicFormData implements IControlData
{
	private static final long serialVersionUID = -5934170892936307332L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setEnabledChanged(boolean enabledChanged)
	{
		this.enabledChanged = enabledChanged;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public FormUiLogic getForm()
	{
		return formUiLogic;
	}		
	public FormName getFormName()
	{
		return formName;
	}
	public boolean getFormWasChanged()
	{
		return formWasChanged;
	}
	public void markFormAsUnchanged()
	{
		formWasChanged = false;		
	}
	public Control findControl(int controlID) 
	{
		if(formUiLogic == null)
			return null;
		return ((ims.framework.cn.Form)formUiLogic.getForm()).findControl(controlID);
	}
	public boolean fireEvent(IMenuEvent event) throws PresentationLogicException 
	{
		if(formUiLogic == null)
			return false;
		return ((ims.framework.cn.Form)formUiLogic.getForm()).fireEvent(event);
	}
	public boolean fireFormDialogClosedEvent(int formName, DialogResult result, String callerIdentifier) throws PresentationLogicException 
	{
		if(formUiLogic == null)
			return false;
		if(!String.valueOf(id).equals(callerIdentifier))
		    return ((ims.framework.cn.Form)formUiLogic.getForm()).fireFormDialogClosedEvent(formName, result, callerIdentifier);
			
		return ((ims.framework.cn.Form)formUiLogic.getForm()).fireFormDialogClosedEvent(formName, result, null);		
	}
	public Object[] getLastParams() 
	{
		return lastParams;
	}	
	public ims.framework.cn.Form open(ims.framework.cn.FormLoader loader, FormName formName, Object[] params, int id, SizeInfo sizeInfo, DynamicForm parentControl, int tabIndex, boolean isNew, Form parentForm) throws Exception 
	{
		this.id = String.valueOf(id);
		lastParams = params;		
		
		if(isNew && this.formUiLogic != null)
		{
			((ims.framework.cn.Form)this.formUiLogic.getForm()).free();
		}
		
		if(formName == null)
		{
			formWasChanged = true;
			this.formName = null;
			formUiLogic = null;
			clearLocalContextForCurrentlyLoadedForm(loader);
			return null;
		}
		
		if(isNew)
		{
			if(this.formName != null && (previousNonDialogFormName.size() == 0 || previousNonDialogFormName.size() > 0 && !previousNonDialogFormName.get(previousNonDialogFormName.size() - 1).equals(this.formName)))
			{
				previousNonDialogFormName.add(this.formName);				
			}
			
			clearLocalContextForCurrentlyLoadedForm(loader);
			loader.clearContext(formName.getID(), false);
			
			formUiLogic = loader.loadDynamicForm(formName.getID(), id, sizeInfo, parentControl, tabIndex, String.valueOf(id), parentControl);			
			((ims.framework.cn.Form)formUiLogic.getForm()).setMode(FormMode.VIEW, false);			
			((ims.framework.cn.Form)formUiLogic.getForm()).restore(new FormData(), isNew);
			((ims.framework.cn.Form)formUiLogic.getForm()).setReadOnly(readOnly || parentForm.isReadOnly());
			formWasChanged = true;
			this.formName = formName;			
			((ims.framework.cn.Form)formUiLogic.getForm()).fireFormOpenEvent(params);
			((ims.framework.cn.Form)formUiLogic.getForm()).fireFormModeChangedEvent();			
		}
		else
		{
			((ims.framework.cn.Form)formUiLogic.getForm()).setReadOnly(readOnly || parentForm.isReadOnly());
		}
				
		return (ims.framework.cn.Form)formUiLogic.getForm();
	}	
	private void clearLocalContextForCurrentlyLoadedForm(ims.framework.cn.FormLoader loader) throws Exception 
	{
		if(this.formName != null)
		{
			loader.clearContext(this.formName.getID(), false);
		}
	}
	public void clear() 
	{
		formUiLogic = null;
		id = null;
		formName = null;
		formWasChanged = true;
		lastParams = null;		
	}
	public boolean supportsDataOperations() 
	{
		if(formUiLogic == null)
			return false;
		
		return IDynamicFormDataOperations.class.isAssignableFrom(formUiLogic.getUiLogic().getClass());		
	}
	public void saveData() throws Exception 
	{
		if(!supportsDataOperations())
			throw new CodingRuntimeException("The loaded form or component does not support data operations");
		
		((IDynamicFormDataOperations)formUiLogic.getUiLogic()).saveData();
	}
	public String[] validateData() 
	{
		if(!supportsDataOperations())
			throw new CodingRuntimeException("The loaded form or component does not support data operations");
		
		return ((IDynamicFormDataOperations)formUiLogic.getUiLogic()).validateData();
	}
	public FormName getPreviousNonDialogFormName() 
	{
		if(previousNonDialogFormName.size() == 0)
			return null;
		
		return previousNonDialogFormName.get(previousNonDialogFormName.size() - 1);
	}
	public void setPreviousNonDialogFormName(FormName formName) 
	{
		previousNonDialogFormName.add(formName);
	}
	public FormMode getOpendFormMode() 
	{
		return formUiLogic == null ? null : formUiLogic.getForm().getMode();
	}	
	public void setReadOnly(boolean value)
	{
		this.readOnly = value;
	}
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
		
	private boolean enabled = true;
	private boolean visible = true;
	private boolean readOnly = false;
	private FormUiLogic formUiLogic = null;
	private String id = null;
	private boolean formWasChanged = false;
	private FormName formName = null;
	private Object[] lastParams = null;	
	private List<FormName> previousNonDialogFormName = new ArrayList<FormName>();
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
}
