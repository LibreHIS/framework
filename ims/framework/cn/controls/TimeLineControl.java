package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.TimeLineControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.TimeLineClick;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class TimeLineControl extends ims.framework.controls.TimeLineControl implements IVisualControl
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
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public void setFromDate(Date date)
	{
		this.data.setFrom(date);
	}
	public void setToDate(Date date)
	{
		this.data.setTo(date);
	}
	public ims.framework.controls.TimeLineEvent addTimeLineEvent(String caption, Image before, Image after, Object value)
	{
		return this.data.addTimeLineEvent(caption, before, after, value);
	}
	public ArrayList getTimeLineEvents()
	{
		return this.data.getTimeLineEvents();
	}
	
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (TimeLineControlData)data;
		super.visible = this.data.isVisible();
	}	
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		if(event instanceof TimeLineClick)
		{
			if(super.delegate != null)
			{
				TimeLineClick e = (TimeLineClick)event;
				super.delegate.handle((ims.framework.controls.TimeLineEvent)this.data.getTimeLineEvents().get(e.getEventID()), e.getDate());
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
	    if (this.data.getFrom() == null)
			throw new ConfigurationException("'From' date is not supplied for the time line control");
		if (this.data.getTo() == null)
			throw new ConfigurationException("'To' date is not supplied for the time line control");
		if (this.data.getTimeLineEvents().size() == 0)
			throw new ConfigurationException("There are no events supplied for the time line control");
		
		sb.append("<timeline id=\"a");
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
		
		sb.append("\" from=\"");
		int month = this.data.getFrom().getMonth();
		if (month < 10)
			sb.append('0');
		sb.append(month);
		sb.append('.');
		sb.append(this.data.getFrom().getYear());
		
		sb.append("\" to=\"");
		month = this.data.getTo().getMonth();
		if (month < 10)
			sb.append('0');
		sb.append(month);
		sb.append('.');
		sb.append(this.data.getTo().getYear());

		sb.append("\" now=\"");
		sb.append(new Date().toString(DateFormat.RUSSIAN));
		sb.append("\">");
		
		for (int i = 0; i < this.data.getTimeLineEvents().size(); ++i)
		{
			TimeLineControlData.TimeLineEvent event = (TimeLineControlData.TimeLineEvent)this.data.getTimeLineEvents().get(i);
			sb.append("<line id=\"");
			sb.append(i);
			sb.append("\" caption=\"");
			sb.append(StringUtils.encodeXML(event.getCaption()));
			sb.append("\" before=\"");
			sb.append(event.getBeforeImage().getImagePath());
			sb.append("\" after=\"");
			sb.append(event.getAfterImage().getImagePath());
			sb.append("\"/>");
		}
		
		sb.append("</timeline>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<timeline id=\"a");
		sb.append(super.id);
		sb.append("\" visible=\"");
		sb.append(this.data.isVisible() ? "true" : "false");
				
		if(this.data.isVisible())
		{
			sb.append("\">");
			
			for (int i = 0; i < this.data.getTimeLineEvents().size(); ++i)
			{
				TimeLineControlData.TimeLineEvent event = (TimeLineControlData.TimeLineEvent)this.data.getTimeLineEvents().get(i);
				sb.append("<line>");
				for (int j = 0; j < event.size(); ++j)
				{
					sb.append("<date value=\"");
					sb.append(event.getDate(j).toString(DateFormat.RUSSIAN));
					sb.append("\" tooltip=\"");
					sb.append(StringUtils.encodeXML(event.getValue(j)));
					sb.append("\"/>");
				}
				sb.append("</line>");
				
			}
			sb.append("</timeline>");
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
	
	private TimeLineControlData data;
}
