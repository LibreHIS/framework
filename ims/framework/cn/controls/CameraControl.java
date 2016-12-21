package ims.framework.cn.controls;

import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.CameraControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.CameraImageCaptured;
import ims.framework.cn.events.CameraImageRemoved;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class CameraControl extends ims.framework.controls.CameraControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();		
		this.data = null;		
	}	
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (CameraControlData)data;
		
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();	
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		if(event instanceof CameraImageCaptured)
		{
			data.setImageString(((CameraImageCaptured)event).getImageString());
			if(super.valueChangedDelegate != null)
			{	
				super.valueChangedDelegate.handle();
			}
			
			return true;
		}
		else if(event instanceof CameraImageRemoved)
		{
			data.setImageString(null);
			if(super.valueChangedDelegate != null)
			{	
				super.valueChangedDelegate.handle();
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<flash id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);		
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\" src=\"");
		sb.append("flash/camera.swf");
		
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<flash id=\"a");
		sb.append(super.id);
		
		if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledChanged(false);
				}
			}
				
			sb.append("\" >");
			
			sb.append("</flash>");
		}
		else
		{
			sb.append("\" />");
		}	
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(data.isEnabledChanged())
				return true;		
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private CameraControlData data;
	
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	@Override
	public String getImageString()
	{
		return data.getImageString();
	}	
}
