package ims.framework.cn.controls;

import ims.framework.cn.IVisualControl;
import ims.framework.Control;
import ims.framework.SessionConstants;
import ims.domain.SessionData;
import ims.framework.cn.data.BookingCalendarData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.BookingCalendarEvent;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DayOfWeek;
import ims.framework.utils.TimeSpan;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

public class BookingCalendar extends ims.framework.controls.BookingCalendar implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(HttpSession session, Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean isRebooking)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, isRebooking);
		this.session = session;
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
	
	public Date getCurrentMonth()
	{
		return this.data.getCurrentMonth();
	}

	public void setCurrentMonth(Date value)
	{
		this.data.setCurrentMonth(value);
	}

	public Date getSelectedDay()
	{
		return this.data.getSelectedDate();
	}

	public void setSelectedDay(Date value)
	{
		this.data.setSelectedDate(value);
	}

	public ArrayList getSelectedDates()
	{
		return this.data.getSelectedDates();
	}

	public ArrayList getNoSlotDates()
	{
		return this.data.getNoSlotDates();
	}

	public void check(Date dt)
	{
		this.data.check(dt);
		if (TimeSpan.getTimeSpan(dt, new Date()).getDays() < 0)
			this.showMessage("The selected date " + dt.toString() + " is in the past.");
	}

	public void uncheck(Date dt)
	{
		this.data.uncheck(dt);
	}
	public void addConflict(Date dt)
	{
		this.data.addConflict(dt);
	}
	public void addDisabledDayOfWeek(DayOfWeek dow)
	{
		this.data.addDisabledDayOfWeek(dow);
	}
	public void addNoSessionDates(Date date)
	{
		this.data.addNoSessionDates(date);
	}
	public void addNoSlotDate(Date dt)
	{
		this.data.addNoSlotDate(dt);
	}
	public void addPercentage(Date dt, int percent)
	{
		this.data.addPercentage(dt, percent);
	}
	public void checkBooked(Date dt)
	{
		this.data.checkBooked(dt);
	}	
	public void setBackColor(Date dt, Color backColor)
	{
		this.data.setBackColor(dt, backColor);
	}
	public void setDateColor(Date dt, Color backgroundColor, Color textColor)
	{
		this.data.setDateColor(dt, backgroundColor, textColor);
	}
	public void clearChecks()
	{
		this.data.clearChecks();
	}
	public void clearConflicts()
	{
		this.data.clearConflicts();
	}
	public void clearDisabledDaysOfWeek()
	{
		this.data.clearDisabledDaysOfWeek();
	}
	public void clearNoSessionDates()
	{
		this.data.clearNoSessionDates();
	}
	public void clearNoSlotDates()
	{
		this.data.clearNoSlotDates();
	}
	public void clearPercentages()
	{
		this.data.clearPercentages();
	}
	public void clearBookedChecks()
	{
		this.data.clearBookedChecks();
	}	
	public void clearBackColors()
	{
		this.data.clearBackColors();	
	}	
	@Override
	public boolean areChecksReadOnly()
	{
		return this.data.areChecksReadOnly();
	}
	@Override
	public void setChecksReadOnly(boolean value)
	{
		this.data.setChecksReadOnly(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (BookingCalendarData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof BookingCalendarEvent)
		{
			boolean wasChanged = this.data.wasChanged();
			
			BookingCalendarEvent ev = (BookingCalendarEvent)event;
			int value = ev.getValue();		
			Date tmp = this.data.stateChanged(value);
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			if(value < 0)
			{
				if(super.bookingCalendarMonthSelectedDelegate != null)
				{	
					super.bookingCalendarMonthSelectedDelegate.handle(this.data.getCurrentMonth());
				}			
			}
			else if(value < 32)
			{
				if(super.bookingCalendarDateSelectedDelegate != null)
				{	
					super.bookingCalendarDateSelectedDelegate.handle(this.data.getSelectedDate());
				}
			}
			else
			{
				if(super.bookingCalendarDateCheckedDelegate != null)
				{	
					super.bookingCalendarDateCheckedDelegate.handle(tmp, value < 2000);
				}
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<bookingcalendar id=\"a");
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
		if(!super.isRebooking)
		{
			sb.append("\" rebooking=\"false");
		}
		
		sb.append("\" today=\"");
		sb.append(new Date().toString());
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<bookingcalendar id=\"a");
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
			
			sb.append("\" checksDisabled=\"");
			sb.append(areChecksReadOnly() ? "true" : "false");
		
			sb.append("\" selectedMonth=\"");
			sb.append(this.data.getCurrentMonth().getMonth());
			sb.append("\" selectedYear=\"");
			sb.append(this.data.getCurrentMonth().getYear());
			if (this.data.getSelectedDate() != null)
			{	
				sb.append("\" selectedDate=\"");
				sb.append(this.data.getSelectedDate().toString());
			}
			
			sb.append("\" disabledDays=\"");
			int size = this.data.getDisabledDaysOfWeek().size() - 1;
			for (int i = 0; i <= size; ++i)
			{	
				sb.append(((DayOfWeek)this.data.getDisabledDaysOfWeek().get(i)).toString());
				if (i != size)
					sb.append(',');
			}
			
			sb.append("\" noSessionDays=\"");
			size = this.data.getNoSessionDays().size() - 1;
			for (int i = 0; i <= size; ++i)
			{	
				sb.append(this.data.getNoSessionDays().get(i).getDay());
				if (i != size)
					sb.append(',');
			}
			
			sb.append("\" conflictDays=\"");
			size = this.data.getConflictDays().size() - 1;
			for (int i = 0; i <= size; ++i)
			{	
				sb.append(this.data.getConflictDays().get(i).getDay());
				if (i != size)
					sb.append(',');
			}
			
			sb.append("\" noSlotsDays=\"");
			size = this.data.getNoSlotsDays().size() - 1;
			for (int i = 0; i <= size; ++i)
			{	
				sb.append(this.data.getNoSlotsDays().get(i).getDay());
				if (i != size)
					sb.append(',');
			}
			
			sb.append("\" percent=\"");
			size = this.data.getPercentage().size() - 1;
			for (int i = 0; i <= size; ++i)
			{	
				sb.append(this.data.getPercentage().get(i).intValue());
				if (i != size)
					sb.append(',');
			}
			
			sb.append("\" bookedDays=\"");
			size = this.data.getBookedDays().size() - 1;
			for (int i = 0; i <= size; ++i)
			{	
				sb.append(this.data.getBookedDays().get(i).getDay());
				if (i != size)
					sb.append(',');
			}
			
			if (super.isRebooking)
			{
				sb.append("\" rebookedDays=\"");
				size = this.data.getRebookedDays().size() - 1;
				for (int i = 0; i <= size; ++i)
				{	
					sb.append(this.data.getRebookedDays().get(i).getDay());
					if (i != size)
						sb.append(',');
				}
			}
			
			sb.append("\" >");
			
			for(int x = 0; x < this.data.getDatesBackColor().size(); x++)
			{
				BookingCalendarData.DateColor dateColor = this.data.getDatesBackColor().get(x);
				if(this.data.getCurrentMonth().getYear() == dateColor.getDate().getYear() && this.data.getCurrentMonth().getMonth() == dateColor.getDate().getMonth())
				{
					//Compatibility with older versions
					if (dateColor.getColor() != null)
						sb.append("<day n=\"" + dateColor.getDate().getDay() + "\" color=\"" + dateColor.getColor().toString() + "\" />");
					else				
						sb.append("<day n=\"" + dateColor.getDate().getDay() + "\" color=\"" + dateColor.getBackColor().toString() + "\" textColor=\"" + dateColor.getTextColor().toString() + "\" />");			
				}
			}
			
			sb.append("</bookingcalendar>");
		}
		else
		{
			sb.append("\" />");
		}		
	}
	
	// this method must absolutely the same as UIEngine.showMessage
	@SuppressWarnings("unchecked")
	private void showMessage(String value)
	{
		value = value.replace('\"', '\'');
	    SessionData sessData = (SessionData) this.session.getAttribute(SessionConstants.SESSION_DATA);

		ArrayList script = sessData.messageBox.get();
		script.add(value);
	}
	public boolean wasChanged() 
	{
		return this.data.wasChanged();
	}
	public void markUnchanged() 
	{
		this.data.markUnchanged();
	}
	
	private BookingCalendarData data;
	private transient HttpSession session;
	private int tabIndex;	
}
