package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.WavPlayerData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public class WavPlayer extends ims.framework.controls.WavPlayer implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.tabIndex = tabIndex;
	}
	protected void free()
	{
		super.free();
		
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
	public void setValue(String value)
	{
		this.data.setValue(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (WavPlayerData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event)
	{
		return false;		
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<wavplayer id=\"a");
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
		sb.append("<wavplayer id=\"a");
		sb.append(super.id);
		
		if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledUnchanged();
				}
			}
		
			if (this.data.isValueChanged())
			{
				sb.append("\" src=\"");
				sb.append(this.data.getValue());
				this.data.setValueUnchanged();
			}
		}
		
		sb.append("\" />");
	}
	public boolean wasChanged() 
	{
		if(this.data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					return true;
				}
			}
			
			if (this.data.isValueChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private WavPlayerData data;
	private int tabIndex;
}
