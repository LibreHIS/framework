package ims.framework.cn.data;

import ims.framework.utils.Color;

public class IntBoxData implements IControlData
{	
	private static final long serialVersionUID = -7237581640467145846L;
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
	public Integer getValue()
	{
		return this.value;
	}
	public void setValue(Integer value)
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
	public void setTextColor(Color value)
	{
		if(!this.textColorChanged)
		{
			if(this.textColor == null)
				this.textColorChanged = value != null;
			else
				this.textColorChanged = !this.textColor.equals(value);
		}
		
		this.textColor = value;
	}
	public Color getTextColor()
	{
		return this.textColor;
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
		
	    this.hasTooltip = true;
		this.tooltip = value;
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public boolean hasTooltip()
	{
		return this.hasTooltip;
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
	public void setTextColorChanged(boolean textColorChanged)
	{
		this.textColorChanged = textColorChanged;
	}
	public boolean isTextColorChanged()
	{
		return textColorChanged;
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
	private Integer value = null;
	private Color textColor = null;
	private String tooltip = null;
	private boolean hasTooltip = false;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean requiredChanged = false;
	private boolean valueChanged = false;
	private boolean textColorChanged = false;
	private boolean tooltipChanged = false;
}