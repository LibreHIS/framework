package ims.framework.cn.data;

import java.util.ArrayList;

import ims.framework.utils.Date;
import ims.framework.utils.Image;

public class TimeLineControlData extends ChangeableData implements IControlData
{
	private static final long serialVersionUID = -746802241168471629L;

	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setFrom(Date value)
	{
		if(!this.dataWasChanged)
		{
			if(this.from == null)
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.from.equals(value);
		}
		
		this.from = value;
	}
	public Date getFrom()
	{
		return this.from;
	}
	public void setTo(Date value)
	{
		if(!this.dataWasChanged)
		{
			if(this.to == null)
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.to.equals(value);
		}
		
		this.to = value;
	}
	public Date getTo()
	{
		return this.to;
	}
	public TimeLineEvent addTimeLineEvent(String caption, Image before, Image after, Object value)
	{
		this.dataWasChanged = true;
		TimeLineEvent t = new TimeLineEvent(caption, before, after, value);
		this.events.add(t);
		return t;
	}
	public ArrayList getTimeLineEvents()
	{
		return this.events;
	}
	private boolean visible = true;
	private Date from = null;
	private Date to = null;
	private ArrayList<TimeLineEvent> events = new ArrayList<TimeLineEvent>();
	
	public static class TimeLineEvent extends ims.framework.controls.TimeLineEvent
	{
		private static final long serialVersionUID = 1L;
		
		TimeLineEvent(String caption, Image before, Image after, Object value)
		{
			this.caption = caption;
			this.before = before;
			this.after = after;
			this.value = value;
		}
		public void addDate(Date date, String value)
		{
			this.dates.add(date);
			this.captions.add(value);
		}
		public void clear()
		{
			this.dates.clear();
			this.captions.clear();
		}
		public Object getValue()
		{
			return this.value;
		}
		public String getCaption()
		{
			return this.caption;
		}
		public Image getBeforeImage()
		{
			return this.before;
		}
		public Image getAfterImage()
		{
			return this.after;
		}
		public int size()
		{
			return this.dates.size();
		}
		public Date getDate(int index)
		{
			return this.dates.get(index);
		}
		public String getValue(int index)
		{
			return this.captions.get(index);
		}
		
		private String caption;
		private Image before;
		private Image after;
		private Object value;
		private ArrayList<Date> dates = new ArrayList<Date>();
		private ArrayList<String> captions = new ArrayList<String>();
	}
}
