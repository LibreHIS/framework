package ims.framework.cn.data;

import ims.framework.utils.PartialDate;

public class PartialDateBoxData implements IControlData
{
	private static final long serialVersionUID = 35155489957582315L;
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
	public PartialDate getValue()
	{
		return this.value;
	}
	public PartialDate getMinValue()
	{
		return this.minValue;
	}
	public PartialDate getMaxValue()
	{
		return this.maxValue;
	}
	public void setValue(PartialDate value)
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
	public void setMinValue(PartialDate value)
	{
		if(!this.minValueChanged)
		{
			if(this.minValue == null)
				this.minValueChanged = value != null;
			else 
				this.minValueChanged = !this.minValue.equals(value);
		}
		
		this.minValue = value;
	}
	public void setMaxValue(PartialDate value)
	{
		if(!this.maxValueChanged)
		{
			if(this.maxValue == null)
				this.maxValueChanged = value != null;
			else 
				this.maxValueChanged = !this.maxValue.equals(value);
		}
		
		this.maxValue = value;
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
	public void setRequiredChanged(boolean requiredChanged)
	{
		this.requiredChanged = requiredChanged;
	}
	public boolean isRequiredChanged()
	{
		return requiredChanged;
	}
	public void setValueChanged(boolean valueChanged)
	{
		this.valueChanged = valueChanged;
	}
	public boolean isValueChanged()
	{
		return valueChanged;
	}
	public void setMinValueChanged(boolean minValueChanged)
	{
		this.minValueChanged = minValueChanged;
	}
	public boolean isMinValueChanged()
	{
		return minValueChanged;
	}
	public void setMaxValueChanged(boolean maxValueChanged)
	{
		this.maxValueChanged = maxValueChanged;
	}
	public boolean isMaxValueChanged()
	{
		return maxValueChanged;
	}
	
	
	
	private boolean enabled = true;
	private boolean visible = true;
	private boolean required = false;
	private PartialDate value = null;
	private PartialDate minValue;
	private PartialDate maxValue;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean requiredChanged = false;
	private boolean valueChanged = false;
	private boolean minValueChanged = false;
	private boolean maxValueChanged = false;
}
