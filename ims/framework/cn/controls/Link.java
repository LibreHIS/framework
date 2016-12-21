package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.LinkData;
import ims.framework.cn.events.ButtonClicked;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.StringUtils;

public class Link extends ims.framework.controls.Link implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, boolean canEditData, String tooltip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.tabIndex = tabIndex;
		this.text = text;
		this.canEditData = canEditData;
		super.tooltip = tooltip;
	}
	public void setTooltip(String value)
	{
		super.tooltip = value;		
		this.data.setTooltip(value);
	}
	protected void free() // free resources
	{
		super.free();
		
		this.text = null;
		this.data = null;
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
	public String getText()
	{
		return this.text;
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (LinkData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			this.data.setTooltip(super.tooltip);
		}
		else
		{
			super.tooltip = this.data.getTooltip();
		}
	}		
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof ButtonClicked)
		{
			checkIfControlCanFire(false);
			
			if(super.clickDelegate != null)
			{
				super.clickDelegate.handle();
			}
			
			return true;
		}
		
		return false;		
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<link id=\"a");
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
		
		sb.append("\" value=\"");
		sb.append(ims.framework.utils.StringUtils.encodeXML(this.text));		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<link id=\"a");
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
				if (this.form.isReadOnly() && this.canEditData)
				{
					sb.append("\" enabled=\"");
					sb.append("false");
					
					sb.append("\" tooltip=\"");
					sb.append(StringUtils.encodeXML("<b>Disabled</b><br>The Form is Read Only"));
				}
				else
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
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private LinkData data;
	private int tabIndex;
	private String text;
	private boolean canEditData;
}
