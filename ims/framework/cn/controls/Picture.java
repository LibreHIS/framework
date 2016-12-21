package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.PictureData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.utils.Image;

public class Picture extends ims.framework.controls.Picture implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean autoSize)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.autoSize = autoSize;
	}
	protected void free() // free resources
	{
		super.free();
		
		this.data = null;		
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public void setValue(Image value)
	{
		this.data.setValue(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (PictureData)data;
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event)
	{
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<picture id=\"a");
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
		if(this.autoSize)
		{
			sb.append("\" autoSize=\"true");
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<picture id=\"a");
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
				if(this.data.getValue() != null)
				{
				    sb.append("\" src=\"");			
					sb.append(this.data.getValue().getImagePath());
				}
			
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
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private PictureData data;	
	private boolean autoSize;
}
