package ims.framework.cn.data;

import ims.framework.utils.DateTime;

public class DateTimeControlData implements IControlData
{
	private static final long serialVersionUID = -6500376972213780229L;
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
	public DateTime getValue()
	{
		return this.value;
	}
	public DateTime getMinValue()
	{
		return this.minValue;
	}
	public DateTime getMaxValue()
	{
		return this.maxValue;
	}
	public void setValue(DateTime value)
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
	public void setMinValue(DateTime value)
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
	public void setMaxValue(DateTime value)
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
	public void setTooltip(String value)
	{
		if(!this.tooltipChanged)
		{
			if(this.tooltip == null)
				this.tooltipChanged = value != null;
			else
				this.tooltipChanged = !this.tooltip.equals(value);
		}
		
		this.tooltip = value;		
	}
	public String getTooltip()
	{
		return this.tooltip == null ? "" : this.tooltip;
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
	public void setTooltipChanged(boolean tooltipChanged)
	{
		this.tooltipChanged = tooltipChanged;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	private boolean required = false;
	private DateTime value = null;
	private DateTime minValue = null;
	private DateTime maxValue = null;
	private String tooltip = null;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean requiredChanged = false;
	private boolean valueChanged = false;
	private boolean minValueChanged = false;
	private boolean maxValueChanged = false;
	private boolean tooltipChanged = false;
}
