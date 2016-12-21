package ims.framework.cn.data;

import java.util.ArrayList;
import java.util.List;

import ims.framework.enumerations.DiaryView;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.Image;

public class DiaryData implements IControlData
{
	private static final long serialVersionUID = -5934170892936307332L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public DiaryEvents getEvents()
	{
		return events;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setEnabledChanged(boolean enabledChanged)
	{
		this.enabledChanged = enabledChanged;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public DiaryView getCurrentView()
	{		
		return currentView;
	}
	public void setCurrentView(DiaryView currentView)
	{	
		if(currentView == null)
			throw new CodingRuntimeException("Invalid event view type");
		
		this.currentView = currentView;
		
		updateCurrentDate();
	}	
	public Date getCurrentDate()
	{
		return currentDate;
	}
	public void setCurrentDate(Date currentDate)
	{
		if(currentDate == null)
			throw new CodingRuntimeException("Invalid event current date");
		
		this.currentDate = currentDate;
		
		updateCurrentDate();
	}	
	public DiaryEvents getEventsForDate(Date date)
	{		
		DiaryEvents filteredEvents = new DiaryEvents();
		
		for(int x = 0; x < this.events.size(); x++)
		{
			if(events.get(x).getStartDateTime().getDate().equals(date))
				filteredEvents.add((ims.framework.cn.data.DiaryData.DiaryEvent)events.get(x));
		}
		
		return filteredEvents;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		if(title == null)
			throw new CodingRuntimeException("Invalid title");
		
		this.title = title;
	}
	public void setSelectedEventID(int id)
	{
		selectedEventID = id;
	}
	public int getSelectedEventID()
	{
		return selectedEventID;
	}
	
	private void updateCurrentDate()
	{
		if(currentView == DiaryView.THREE_MONTHS)
			currentDate = new Date(currentDate.getYear(), currentDate.getMonth(), 1);
	}
	
	private boolean enabled = true;
	private boolean visible = true;	
	private DiaryEvents events = new DiaryEvents();
	private DiaryView currentView = DiaryView.THREE_MONTHS;
	private Date currentDate = new Date();
	private String title = "Diary";
	private int selectedEventID = -1;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	
	public class DiaryEvents implements ims.framework.controls.DiaryEvents
	{
		private List<DiaryEvent> list = new ArrayList<DiaryEvent>();
		
		private static final long serialVersionUID = 1L;

		public void clear()
		{
			list.clear();
			selectedEventID = -1;
		}
		public ims.framework.controls.DiaryEvent get(int index)
		{
			return list.get(index);
		}
		public ims.framework.controls.DiaryEvent getByIdentifier(Object value)
		{
			return null;
		}
		public int indexOf(ims.framework.controls.DiaryEvent event)
		{
			return list.indexOf(event);
		}
		public ims.framework.controls.DiaryEvent newEvent(DateTime dateTime, String text)
		{
			DiaryEvent event = new DiaryEvent(dateTime, text);
			list.add(event);
			return event;
		}
		public boolean remove(ims.framework.controls.DiaryEvent event)
		{
			return list.remove(event);
		}
		public int size()
		{
			return list.size();
		}
		public void add(DiaryEvent event)
		{
			list.add(event);
		}
		public void markUnchanged()
		{
			for(int x = 0; x < list.size(); x++)
			{
				list.get(x).markUnchanged();
			}
		}
		public boolean wasChanged()
		{
			for(int x = 0; x < list.size(); x++)
			{
				if(list.get(x).wasChanged())
					return true;
			}
			
			return false;
		}		
	}
	public class DiaryEvent implements ims.framework.controls.DiaryEvent
	{
		private static final long serialVersionUID = 1L;

		private DateTime startDateTime;
		private String text;
		private Image image;
		private Object identifier;
		private Color textColor;
		private Color backColor;
		private String tooltip;
		private boolean dataWasChanged = true;
		
		public DiaryEvent(DateTime startDateTime, String text)
		{
			this.startDateTime = startDateTime;
			this.text = text;
		}
		
		public Color getBackColor()
		{
			return backColor;
		}
		public Object getIdentifier()
		{
			return identifier;
		}
		public Image getImage()
		{
			return image;
		}
		public DateTime getStartDateTime()
		{
			return startDateTime;
		}
		public String getText()
		{
			return text;
		}
		public Color getTextColor()
		{
			return textColor;
		}
		public String getTooltip()
		{
			return tooltip;
		}
		public void setBackColor(Color backColor)
		{
			if(!dataWasChanged)
			{
				if(backColor != null && this.backColor != null)
				{
					dataWasChanged = !this.backColor.equals(backColor);
				}
				else
				{
					dataWasChanged = (backColor == null && this.backColor != null) || (backColor != null && this.backColor == null);
				}
			}
			
			this.backColor = backColor;
		}
		public void setIdentifier(Object identifier)
		{
			if(!dataWasChanged)
			{
				if(identifier != null && this.identifier != null)
				{
					dataWasChanged = !this.identifier.equals(identifier);
				}
				else
				{
					dataWasChanged = (identifier == null && this.identifier != null) || (identifier != null && this.identifier == null);
				}
			}
			
			this.identifier = identifier;
		}
		public void setImage(Image image)
		{
			if(!dataWasChanged)
			{
				if(image != null && this.image != null)
				{
					dataWasChanged = !this.image.equals(image);
				}
				else
				{
					dataWasChanged = (image == null && this.image != null) || (image != null && this.image == null);
				}
			}
			
			this.image = image;
		}
		public void setStartDateTime(DateTime startDateTime)
		{
			if(startDateTime == null)
				throw new CodingRuntimeException("Invalid date");
			
			if(!dataWasChanged)
			{				
				if(startDateTime != null && this.startDateTime != null)
				{
					dataWasChanged = !this.startDateTime.equals(startDateTime);
				}
				else
				{
					dataWasChanged = (startDateTime == null && this.startDateTime != null) || (startDateTime != null && this.startDateTime == null);
				}
			}
			
			this.startDateTime = startDateTime;
		}
		public void setText(String text)
		{
			if(text == null)
				throw new CodingRuntimeException("Invalid text");
			
			if(!dataWasChanged)
			{
				if(text != null && this.text != null)
				{
					dataWasChanged = !this.text.equals(text);
				}
				else
				{
					dataWasChanged = (text == null && this.text != null) || (text != null && this.text == null);
				}
			}
			
			this.text = text;
		}
		public void setTextColor(Color textColor)
		{
			if(!dataWasChanged)
			{
				if(textColor != null && this.textColor != null)
				{
					dataWasChanged = !this.textColor.equals(textColor);
				}
				else
				{
					dataWasChanged = (textColor == null && this.textColor != null) || (textColor != null && this.textColor == null);
				}
			}
			
			this.textColor = textColor;
		}
		public void setTooltip(String tooltip)
		{
			if(!dataWasChanged)
			{
				if(tooltip != null && this.tooltip != null)
				{
					dataWasChanged = !this.tooltip.equals(tooltip);
				}
				else
				{
					dataWasChanged = (tooltip == null && this.tooltip != null) || (tooltip != null && this.tooltip == null);
				}
			}
			
			this.tooltip = tooltip;
		}
		public void markUnchanged()
		{
			dataWasChanged = false;
		}
		public boolean wasChanged()
		{			
			return dataWasChanged;
		}		
	}	
}
