package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.HintData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.interfaces.IScreenHint;
import ims.framework.utils.StringUtils;

public class Hint extends ims.framework.controls.Hint implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, IScreenHint hint)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);		
		this.tooltip = hint == null ? null : hint.getScreenHintText();
		if(this.tooltip != null)
		{
			this.tooltip = this.tooltip.replace("\n", "<br>");			
		}		
	}
	protected void free()
	{
		super.free();
				
		this.tooltip = null;
		this.data = null;
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (HintData)data;
		super.visible = this.data.isVisible();		

		if(isNew)
		{
			this.data.setTooltip(this.tooltip);
		}
		else
		{
			this.tooltip = this.data.getTooltip();
		}
	}
	public boolean fireEvent(IControlEvent event)
	{		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<hint id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"16");
		sb.append("\" height=\"16"); 
		sb.append("\" tabIndex=\"-1");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
				
		sb.append("\" tooltip=\"");
		sb.append(this.tooltip == null ? "" : StringUtils.encodeXML(this.tooltip));
		
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<hint id=\"a");
		sb.append(super.id);
				
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(data.isTooltipChanged())
			{
				sb.append("\" tooltip=\"");
				sb.append(this.tooltip == null ? "" : StringUtils.encodeXML(this.tooltip));
				data.setTooltipChanged(false);
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
			if(data.isTooltipChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private HintData data;
	private String tooltip;
}