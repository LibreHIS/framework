package ims.framework.cn.data;

import ims.base.interfaces.IModifiable;
import ims.framework.enumerations.FormMode;

public class CustomComponentData extends FormData implements IControlData, IModifiable
{
	private static final long serialVersionUID = 1L;
	
	public boolean wasChanged() 
	{
		return dataWasChanged;
	}
	public void markUnchanged() 
	{	
		dataWasChanged = false;		
	}
	public Boolean isEnabled()
	{
		return this.enabled;
	}
	public Boolean isVisible()
	{
		return this.visible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
		{
			if(this.enabled == null)
				this.dataWasChanged = true;
			else
				this.dataWasChanged = this.enabled.booleanValue() != value;
		}
		
		this.enabled = new Boolean(value);
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
		{
			if(this.visible == null)
				this.dataWasChanged = true;
			else
				this.dataWasChanged = this.visible.booleanValue() != value;
		}
		
		this.visible = new Boolean(value);
	}
	public FormData getFormData() 
	{
		return formData;
	}
	public void setFormData(FormData value) 
	{
		formData = value;
	}
	public FormMode getFormMode()
	{
		return formMode;
	}
	public void setFormMode(FormMode formMode)
	{
		this.formMode = formMode;
	}
	
	private boolean dataWasChanged = true;
	private Boolean enabled;
	private Boolean visible;
	private FormMode formMode = FormMode.VIEW;
	FormData formData = new FormData();
}
