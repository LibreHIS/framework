package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.StringUtils;
import ims.framework.cn.data.BarControlData;
import ims.framework.controls.BarControlResource;

public class BarControl extends ims.framework.controls.BarControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
	}	
	protected void free()
	{
		super.free();		
		this.data = null;		
	}	
	public void addResource(BarControlResource resource)
	{
		this.data.addResource(resource);
	}
	public void addResource(int total, int completed, String message)
	{
		addResource(new BarControlResource(total, completed, message));
	}	
	public void clear()
	{
		this.data.clear();
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (BarControlData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<barcontrol id=\"a");
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
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<barcontrol id=\"a");
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
			}
			
			sb.append("\" >");
			
			if(data.isResourcesChanged())
			{
				sb.append("<resources>");
				for(int x = 0; x < this.data.getResources().size(); x++)
				{
					BarControlResource resource = (BarControlResource)this.data.getResources().get(x);
					sb.append("<resource total=\"" + resource.getTotal() + "\" completed=\"" + resource.getCompleted() + "\" message=\"" + StringUtils.encodeXML(resource.getMessage()) + "\" />"); 
				}
				sb.append("</resources>");
				
				data.setResourcesChanged(false);
			}
			
			sb.append("</barcontrol>");			
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
			
			if(data.isResourcesChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private BarControlData data;	
}
