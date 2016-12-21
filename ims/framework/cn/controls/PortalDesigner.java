package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.PortalDesignerData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.StringUtils;

public class PortalDesigner extends ims.framework.controls.PortalDesigner implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String caption, String toolTip)	
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, tooltip, caption);
		
		this.tooltip = toolTip;
		this.tabIndex = tabIndex;		
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public void setTooltip(String value)
	{
		super.tooltip = value;
		this.data.setTooltip(value);		
	}	
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);		
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public void setCaption(String value) 
	{
		this.data.setCaption(value);
		this.caption = value;
	}
	public String getCaption()
	{
		return caption;
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (PortalDesignerData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			this.data.setCaption(this.caption);
			this.data.setTooltip(this.tooltip);			
		}
		else
		{
			this.caption = this.data.getCaption();
			this.tooltip = this.data.getTooltip();			
		}		
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<button id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{		
		sb.append("<button id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{			
			if(!hasAnyParentDisabled())
			{				
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					data.setEnabledChanged(false);
				}
				
				if(data.isTooltipChanged())
				{
					sb.append("\" tooltip=\"");
					sb.append(super.tooltip == null ? "" : StringUtils.encodeXML(super.tooltip));
					data.setTooltipChanged(false);
				}				
			}
				
			if(data.isCaptionChanged())
			{
				sb.append("\" value=\"");
				sb.append(this.caption == null ? "" : ims.framework.utils.StringUtils.encodeXML(this.caption));				
				data.setCaptionChanged(false);
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
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
				if(data.isTooltipChanged())
					return true;
			}
			
			if(data.isCaptionChanged())
				return true;						
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private PortalDesignerData data;
	private int tabIndex;

	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		return false;
	}
}