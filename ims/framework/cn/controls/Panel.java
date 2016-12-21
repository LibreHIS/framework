package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.PanelData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public class Panel extends ims.framework.controls.Panel implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, int style, String imageUrl)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.text = text;
		this.style = style;
		this.imageUrl = imageUrl;
	}
	protected void free()
	{
		super.free();
		
		this.text = null;
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
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (PanelData)data;
		super.visible = this.data.isVisible();		
		
		if(isNew)
		{
			this.data.setValue(this.text);
		}
		else
		{
			this.text = this.data.getValue();
		}
	}
	public boolean fireEvent(IControlEvent event)
	{
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<panel id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"-1");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\" type=\"");
		if (this.style == 0)
			sb.append("Dark");
		else if (this.style == 1)
			sb.append("White");
		else
			sb.append("MiniBox");					
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<panel id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(data.isValueChanged())
			{
				sb.append("\" text=\"");
				sb.append(this.text == null ? "" : ims.framework.utils.StringUtils.encodeXML(this.text));
				data.setValueChanged(false);
				
				sb.append("\" img=\"");
				sb.append(this.imageUrl);
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
		}
		
		return false;
	}
	public void markUnchanged() 
	{
	}
	
	private PanelData data;
	private String text;
	private int style;
	private String imageUrl;
}
