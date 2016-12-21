package ims.framework.cn.data;

import ims.framework.utils.Color;

public class TextBoxData implements IControlData
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
	public String getValue()
	{
		return this.value;
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
	public void setValue(String value)
	{
		if(value != null && value.equals(""))
			value = null;
		
		if(!this.valueChanged)
		{
			if(this.value == null)
				this.valueChanged = value != null;
			else
				this.valueChanged = !this.value.equals(value);
		}
		
		this.value = value;
	}
	public void setTextColor(Color color)
	{
		if(!this.textColorChanged)
		{
			if(this.textColor == null)
				this.textColorChanged = value != null;
			else
				this.textColorChanged = !this.textColor.equals(value);
		}
		
		this.textColor = color;
	}
	public Color getTextColor()
	{
		return this.textColor;
	}
	public void setSelectedText(String value)
	{
		this.selectedText = value;
	}
	public String getSelectedText()
	{
		return this.selectedText == null ? "" : this.selectedText;
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
		return this.tooltip;
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
	public void setSelectedTextChanged(boolean selectedTextChanged)
	{
		this.selectedTextChanged = selectedTextChanged;
	}
	public boolean isSelectedTextChanged()
	{
		return selectedTextChanged;
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
	private String value = null;
	private String selectedText = null;
	private Color textColor = null;
	private String tooltip = null;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean requiredChanged = false;
	private boolean valueChanged = false;
	private boolean selectedTextChanged = false;
	private boolean textColorChanged = false;
	private boolean tooltipChanged = false;
}
