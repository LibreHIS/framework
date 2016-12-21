package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.AvailabilityControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.AvailabilityControlClicked;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.FrameworkInternalException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Date;
import ims.framework.utils.Time;

public class AvailabilityControl extends ims.framework.controls.AvailabilityControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.tabIndex = tabIndex;
	}
	protected void free() // free resources
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
	public void setStartDate(Date date)
	{
		this.data.setStartDate(date);
	}
	public void setEndDate(Date date)
	{
		this.data.setEndDate(date);
	}
	public void addExcludedDate(Date date) throws FrameworkInternalException
	{
		this.data.addExcludedDate(date);
	}
	public void addBookedTime(Date date, Time startTime, Time endTime)  throws FrameworkInternalException
	{
		this.data.addBookedTime(date, startTime, endTime);
	}
	public void addUnavailableTime(Date date, Time startTime, Time endTime)  throws FrameworkInternalException
	{
		this.data.addUnavailableTime(date, startTime, endTime);
	}
	public void addTooltip(Date date, String tooltip)
	{
		this.data.addTooltip(date, tooltip);
	}
	public void clear()
	{
		this.data.clear();
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (AvailabilityControlData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof AvailabilityControlClicked)
		{
			if(super.delegate != null)
			{
				int index = ((AvailabilityControlClicked)event).getValue();
				super.delegate.handle(new Time(index / 2 + 8, index % 2 == 0 ? 0 : 30));
			}
			
			return true;			
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<availabilitycontrol id=\"a");
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
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<availabilitycontrol id=\"a");
		sb.append(super.id);
		sb.append("\" visible=\"");		
		sb.append(this.data.isVisible() ? "true" : "false");
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				sb.append("\" enabled=\"");
				sb.append(this.data.isEnabled() ? "true" : "false");
			}
			
			sb.append("\" >");
			
			this.data.render(sb);
			
			sb.append("</availabilitycontrol>");
		}		
		else
		{
			sb.append("\" />");
		}
	}
	public boolean wasChanged() 
	{
		return this.data.wasChanged();
	}
	public void markUnchanged() 
	{
		this.data.markUnchanged();
	}
	
	private AvailabilityControlData data;
	private int tabIndex; 
}
