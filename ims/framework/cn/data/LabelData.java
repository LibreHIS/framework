package ims.framework.cn.data;

import ims.framework.utils.Color;

public class LabelData implements IControlData
{	
	private static final long serialVersionUID = -4327529720284373905L;
	public boolean isVisible()
	{
		return this.visible;
	}
	public String getValue()
	{
		return this.value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public boolean hasValue()
	{
		return this.hasValue;
	}
	public void setValue(String value)
	{
		if(!this.valueChanged)
		{
			if(this.value == null)
				this.valueChanged = value != null;
			else 
				this.valueChanged = !this.value.equals(value);
		}
		
		this.hasValue = true;
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
		
		this.tooltip = value;		
	}
	public String getTooltip()
	{
		return this.tooltip;
	}	
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
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

	private boolean visible = true;
	private String value = null;
	private boolean hasValue = false;
	private Color textColor = null;
	private String tooltip = null;
	
	private boolean visibleChanged = false;
	private boolean valueChanged = false;
	private boolean textColorChanged = false;
	private boolean tooltipChanged = false;
}
