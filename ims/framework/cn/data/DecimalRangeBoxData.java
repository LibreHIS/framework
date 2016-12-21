package ims.framework.cn.data;

import ims.framework.utils.DecimalRange;

public class DecimalRangeBoxData implements IControlData
{
	private static final long serialVersionUID = -6110664206454855613L;
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
	public DecimalRange getValue()
	{
		return this.value;
	}
	public void setValue(DecimalRange value)
	{
		if(!this.valueChanged)
		{
			if(this.value == null)
				this.valueChanged = value != null;
			else 
				this.valueChanged = !this.value.equals(value);
		}
		
		this.value = value;
	}
	public void setRequired(boolean value)
	{
		if(!this.requiredChanged)
			this.requiredChanged = this.required != value;
		
		this.required = value;
	}
	public boolean isRequired()
	{
		return this.required;
	}	
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setRequiredUnchanged()
	{
		this.requiredChanged = false;
	}
	public boolean isRequiredChanged()
	{
		return requiredChanged;
	}
	public void setValueUnchanged()
	{
		this.valueChanged = false;
	}
	public boolean isValueChanged()
	{
		return valueChanged;
	}

	private boolean enabled = true;
	private boolean visible = true;
	private boolean required = false;
	private DecimalRange value = new DecimalRange();
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean requiredChanged = false;
	private boolean valueChanged = false;
}
