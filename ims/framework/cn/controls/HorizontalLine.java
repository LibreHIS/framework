package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.HorizontalLineData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public class HorizontalLine extends ims.framework.controls.HorizontalLine implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, int style)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.style = style;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (HorizontalLineData)data;
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event)
	{
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<horizontalline id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		if (this.style == 0)
			sb.append("9");
		else 
			sb.append("1");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\" type=\"");
		if (this.style == 0)
			sb.append("Footer");
		else if (this.style == 1)
			sb.append("Solid");
		else
			sb.append("Gradient");
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<horizontalline id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleUnchanged();
		}
		
		sb.append("\" />");
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private HorizontalLineData data;
	private int style;
}
