package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.DiaryData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.DiaryButtonPressed;
import ims.framework.cn.events.DiarySelectionChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.DiaryEvent;
import ims.framework.controls.DiaryEvents;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.DiaryView;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Date;
import ims.framework.utils.StringUtils;

public class Diary extends ims.framework.controls.Diary implements IVisualControl
{	
	private static final long serialVersionUID = 1L;

	protected int tabIndex;	
	private DiaryData data;
	
	public final void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.tabIndex = tabIndex;		
	}
	
	@Override
	public void setTitle(String title)
	{
		data.setTitle(title);
	}
	@Override
	public String getTitle()
	{
		return data.getTitle();
	}
	@Override
	public void clear()
	{		
		getEvents().clear();
	}
	@Override
	public DiaryEvents getEvents()
	{
		return data.getEvents();
	}	
	@Override
	public DiaryEvent getSelectedEvent()
	{
		return null;
	}	
	@Override
	public DiaryView getCurrentView()
	{
		return data.getCurrentView();
	}
	@Override
	public void setCurrentView(DiaryView currentView)
	{
		data.setCurrentView(currentView);
	}
	@Override
	public Date getCurrentDate()
	{
		return data.getCurrentDate();
	}
	@Override
	public void setCurrentDate(Date currentDate)
	{		
		data.setCurrentDate(currentDate);
	}	
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		if(event instanceof DiarySelectionChanged)
		{
			DiarySelectionChanged castEvent = (DiarySelectionChanged)event;
			DiaryEvent diaryEvent = data.getEvents().get(castEvent.getValue());
			
			if(diaryEvent == null)
				data.setSelectedEventID(-1);
			else
				data.setSelectedEventID(data.getEvents().indexOf(diaryEvent));
			
			if(selectionChangedDelegate != null)
				selectionChangedDelegate.handle(diaryEvent);
			
			return true;
		}
		else if(event instanceof DiaryButtonPressed)
		{
			DiaryButtonPressed castEvent = (DiaryButtonPressed)event;
			
			int increment = 1;
			if(castEvent.getCommand() == DiaryButtonCommand.BACK)
			{
				increment = -1;
			}
			else if(castEvent.getCommand() == DiaryButtonCommand.FORWARD)
			{
				increment = 1;
			}
			
			Date currentDate = data.getCurrentDate();			
			if(data.getCurrentView() == DiaryView.THREE_MONTHS)
			{
				currentDate = new Date(currentDate.getYear(), currentDate.getMonth(), 1);
				currentDate = currentDate.addMonth(increment);
				data.setCurrentDate(currentDate);
			}
			
			
			if(currentDateChangedDelegate != null)
				currentDateChangedDelegate.handle(currentDate);
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<diary id=\"a");
		sb.append(super.id);		
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);		
		
		if(super.menu != null)
		{
			sb.append("\" menuID=\"");
			sb.append(super.menu.getID());
		}		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		

		sb.append("\"/>");	
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<diary id=\"a");
		sb.append(super.id);
		
		sb.append("\" selectedEventID=\""); 
		sb.append(data.getSelectedEventID());		
		
		// true by default
		// sb.append("\" autoPostBack=\"true");
		
		sb.append("\" mode=\"");
		sb.append(data.getCurrentView().getModeCode());
		sb.append("\" from=\"");
		sb.append(data.getCurrentDate().getYear());
		if (data.getCurrentDate().getMonth() < 10)
			sb.append("0" + data.getCurrentDate().getMonth());
		else
			sb.append(data.getCurrentDate().getMonth());
		sb.append("\" title=\"");
		sb.append(StringUtils.encodeXML(data.getTitle()));
		sb.append("\" eventsInRow=\""); 
		sb.append(data.getCurrentView().getNoEventsInRow());
		sb.append("\" >");
		
				
		if(data.getEvents().size() == 0)
			sb.append("<events/>");
		else if(data.getCurrentView() == DiaryView.THREE_MONTHS)
		{		
			renderThreeMonthsView(sb);
		}
		
		sb.append("</diary>");
	}
	private void renderThreeMonthsView(StringBuffer sb)
	{
		DiaryEvents events = null;
		
		sb.append("<events>");
		for(int x = 1; x <= 31; x++)
		{
			Date currentDate = new Date(data.getCurrentDate().getYear(), data.getCurrentDate().getMonth(), 1);
			
			sb.append("<r>");
			sb.append("<c>");			
			
			Date date = new Date(currentDate.getYear(), currentDate.getMonth(), x);
			events = data.getEventsForDate(date);
			for(int y = 0; y < events.size(); y++)
			{
				renderEvent(sb, events.get(y), data.getEvents().indexOf(events.get(y)));				
			}			
			
			sb.append("</c>");
			sb.append("<c>");
		
			currentDate = currentDate.addMonth(1);
			
			date = new Date(currentDate.getYear(), currentDate.getMonth(), x);
			events = data.getEventsForDate(date);
			for(int y = 0; y < events.size(); y++)
			{
				renderEvent(sb, events.get(y), data.getEvents().indexOf(events.get(y)));
			}
			
			sb.append("</c>");
			sb.append("<c>");
			
			currentDate = currentDate.addMonth(1);
			
			date = new Date(currentDate.getYear(), currentDate.getMonth(), x);
			events = data.getEventsForDate(date);
			for(int y = 0; y < events.size(); y++)
			{
				renderEvent(sb, events.get(y), data.getEvents().indexOf(events.get(y)));
			}
			
			sb.append("</c>");
			
			sb.append("</r>");
		}		
		sb.append("</events>");
	}
	private void renderEvent(StringBuffer sb, DiaryEvent event, int id)
	{		
		sb.append("<e id=\"");
		sb.append(id);
		sb.append("\"");
		
		if(event.getImage() != null)
		{
			sb.append(" img=\"");
			sb.append(event.getImage().getImagePath());
			sb.append("\"");
		}
		
		if(event.getTextColor() != null)
		{
			sb.append(" textColor=\"");
			sb.append(event.getTextColor());
			sb.append("\"");
		}
				
		if(event.getBackColor() != null)
		{
			sb.append(" backColor=\"");
			sb.append(event.getBackColor());
			sb.append("\"");
		}
		
		if(event.getTooltip() != null)
		{
			sb.append(" tooltip=\"");
			sb.append(StringUtils.encodeXML(event.getTooltip()));
			sb.append("\"");
		}
		
		sb.append(">");
		sb.append(StringUtils.encodeXML(event.getText()));
		sb.append("</e>");		
	}

	public void restore(IControlData data, boolean isNew)
	{
		this.data = (DiaryData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			//this.data.setTooltip(this.tooltip);
		}
		else 
		{
			//super.tooltip = this.data.getTooltip();
		}
	}
	public void markUnchanged()
	{
	}
	public boolean wasChanged()
	{
		return true;
	}		
}
