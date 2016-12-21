package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.LabelData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Color;
import ims.framework.utils.StringUtils;

public class Label extends ims.framework.controls.Label implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, int style, String tooltip, int size)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.text = text;
		this.style = style;
		this.tooltip = tooltip;
		this.size = size;
	}
	protected void free()
	{
		super.free();
				
		this.text = null;
		this.tooltip = null;
		this.data = null;
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public String getValue()
	{
		return this.text;
	}
	public void setValue(String value)
	{
		this.text = value;
		this.data.setValue(value);
	}
	public void setTextColor(Color color)
	{
		this.data.setTextColor(color);
	}
	public void setTooltip(String value)
	{
		this.tooltip = value;
		this.data.setTooltip(value);		
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (LabelData)data;
		super.visible = this.data.isVisible();		

		if(isNew)
		{
			this.data.setValue(this.text);
			this.data.setTooltip(this.tooltip);
		}
		else
		{
			this.text = this.data.getValue();		
			this.tooltip = this.data.getTooltip();
		}
	}
	public boolean fireEvent(IControlEvent event)
	{		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<label id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"18"); // If it does not look good on render, use 20
		sb.append("\" tabIndex=\"-1");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		if(this.style != 0)
		{
			sb.append("\" style=\"");
			//if (this.style == 0)
			//	sb.append("standard");
			//else
				sb.append("bold");
		}		
		if(this.size != 0)
		{
			sb.append("\" size=\"");		
			if (this.size == 1)
				sb.append("medium");
			else if (this.size == 2)
				sb.append("large");
			//else 
			//	sb.append("small");
		}
		
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<label id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(data.isTextColorChanged())
			{
				if(this.data.getTextColor() != null)
				{
					sb.append("\" textColor=\"");
					sb.append(this.data.getTextColor());
					this.data.setTextColor(null);
				}			
				
				data.setTextColorChanged(false);
			}
			
			if(data.isTooltipChanged())
			{
				sb.append("\" tooltip=\"");
				sb.append(this.tooltip == null ? "" : StringUtils.encodeXML(this.tooltip));
				data.setTooltipChanged(false);
			}
			
			if(data.isValueChanged())
			{
				sb.append("\" text=\"");
				sb.append(this.text == null ? "" : ims.framework.utils.StringUtils.encodeXML(this.text));
				data.setValueChanged(false);
			}
		}
		
		sb.append("\" />");
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(data.isValueChanged())
				return true;
			if(data.isTextColorChanged())
				return true;
			if(data.isTooltipChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private LabelData data;
	private String text;	
	private int style;
	private int size;
	private String tooltip;
}