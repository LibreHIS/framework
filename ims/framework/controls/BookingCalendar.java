package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.BookingCalendarDateChecked;
import ims.framework.delegates.BookingCalendarDateSelected;
import ims.framework.delegates.BookingCalendarMonthSelected;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DayOfWeek;

public abstract class BookingCalendar extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean isRebooking)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.isRebooking = isRebooking;
	}
	protected void free() // free resources
	{
		super.free();
		
		this.bookingCalendarDateSelectedDelegate = null;
		this.bookingCalendarMonthSelectedDelegate = null;
		this.bookingCalendarDateCheckedDelegate = null;
	}
		
	public abstract Date getCurrentMonth();
	public abstract void setCurrentMonth(Date value);
	
	public abstract Date getSelectedDay();
	public abstract void setSelectedDay(Date value);
	public abstract ArrayList<Date> getSelectedDates();
	public abstract ArrayList<Date> getNoSlotDates();

	public abstract void check(Date dt);
	public abstract void uncheck(Date dt);
	public abstract void addConflict(Date dt);
	public abstract void addDisabledDayOfWeek(DayOfWeek dow);
	public abstract void addNoSessionDates(Date date);
	public abstract void addNoSlotDate(Date dt);
	public abstract void addPercentage(Date dt, int percent);
	public abstract void checkBooked(Date dt);
	public abstract void setBackColor(Date dt, Color backColor);			
	public abstract void setDateColor(Date dt, Color backgroundColor, Color textColor);

	public abstract void clearChecks();
	public abstract void clearConflicts();
	public abstract void clearDisabledDaysOfWeek();
	public abstract void clearNoSessionDates();
	public abstract void clearNoSlotDates();
	public abstract void clearPercentages();
	public abstract void clearBookedChecks();
	public abstract void clearBackColors();
	
	public abstract void setChecksReadOnly(boolean value);
	public abstract boolean areChecksReadOnly();
	
    public void setBookingCalendarDateSelectedEvent(BookingCalendarDateSelected delegate)
    {
        this.bookingCalendarDateSelectedDelegate = delegate;        
    }
    public void setBookingCalendarMonthSelectedEvent(BookingCalendarMonthSelected delegate)
    {
        this.bookingCalendarMonthSelectedDelegate = delegate;        
    }
    public void setBookingCalendarDateCheckedEvent(BookingCalendarDateChecked delegate)
    {
        this.bookingCalendarDateCheckedDelegate = delegate;        
    }
	
	protected boolean isRebooking;	
    protected BookingCalendarDateSelected bookingCalendarDateSelectedDelegate = null;
    protected BookingCalendarMonthSelected bookingCalendarMonthSelectedDelegate = null;
    protected BookingCalendarDateChecked bookingCalendarDateCheckedDelegate = null;
}
