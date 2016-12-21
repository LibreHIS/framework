package ims.framework.cn.data;

import java.util.ArrayList;
import java.util.HashMap;

import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DayOfWeek;

public class BookingCalendarData extends ChangeableData implements IControlData
{
	public class DateColor
	{
		private Date date;
		private Color color;
		private Color textColor;
		private Color backgroundColor;
		
		public DateColor(Date date, Color color)
		{
			this.date = date;
			this.color = color;			
		}
		public DateColor(Date date, Color backColor, Color textColor)
		{
			this.date = date;
			this.backgroundColor = backColor;			
			this.textColor = textColor;
		}
		
		public Date getDate()
		{
			return date;
		}
		public Color getColor()
		{
			return color;
		}
		public Color getTextColor()
		{
			return textColor;
		}
		public Color getBackColor()
		{
			return backgroundColor;
		}
		public boolean equals(Object obj)
		{
			if(obj instanceof Date)
				return ((Date)obj).equals(date);
			return false;
		}		
	}
	
	private static final long serialVersionUID = -7553529331979049099L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	public Date getCurrentMonth()
	{
		return this.currentMonth;
	}
	public void setCurrentMonth(Date value)
	{
		if(!this.dataWasChanged)
		{
			if(this.currentMonth == null)
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.currentMonth.equals(value);
		}
		
		this.currentMonth = value;
	}
	public Date getSelectedDate()
	{
		return this.selectedDate;
	}
	public void setSelectedDate(Date value)
	{
		if(!this.dataWasChanged)
		{
			if(this.selectedDate == null)
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.selectedDate.equals(value);
		}
		
		this.selectedDate = value;
	}
	
	public void addConflict(Date dt)
	{
		if(dt == null)
		{
			throw new NullPointerException("Conflict date can not be null");
		}
		
		if(!this.conflicts.contains(dt))
		{
			this.dataWasChanged = true;
			this.conflicts.add(dt);
		}
	}
	public void clearConflicts()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.conflicts.size() != 0;
		
		this.conflicts.clear();
	}

	public void addDisabledDayOfWeek(DayOfWeek value)
	{
		if(value == null)
		{
			throw new NullPointerException("Disabled day of week can not be null");
		}
		if(!this.disabledDaysOfWeek.contains(value))
		{
			this.dataWasChanged = true;
			this.disabledDaysOfWeek.add(value);
		}
	}
	public void clearDisabledDaysOfWeek()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.disabledDaysOfWeek.size() != 0;
		
		this.disabledDaysOfWeek.clear();
	}	
	public void addNoSessionDates(Date value)
	{
		if(value == null)
		{
			throw new NullPointerException("'No session' date can not be null");
		}
		if(!this.noSessionDates.contains(value))
		{
			this.dataWasChanged = true;
			this.noSessionDates.add(value);
		}
	}
	public void clearNoSessionDates()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.noSessionDates.size() != 0;
		
		this.noSessionDates.clear();
	}
	
	public void addNoSlotDate(Date value)
	{
		if(value == null)
		{
			throw new NullPointerException("'No slot' date can not be null");
		}
		if(!this.noSlotDates.contains(value))
		{
			this.dataWasChanged = true;
			this.noSlotDates.add(value);
		}
	}
	public void clearNoSlotDates()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.noSlotDates.size() != 0;
		
		this.noSlotDates.clear();
	}
	public ArrayList<Date> getNoSlotDates()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for(int i = 0; i < this.noSlotDates.size(); ++i)
		{
			result.add(this.noSlotDates.get(i));
		}		
		return result;
	}	
	public void addPercentage(Date dt, int percent)
	{
		if(!this.disabledDaysOfWeek.contains(dt.getDayOfWeek()))
		{
			this.dataWasChanged = true;
			this.percentage.put(dt.toString(), new Integer(percent));
		}
	}
	public void clearPercentages()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.percentage.size() != 0;
		
		this.percentage.clear();
	}	
	public void check(Date value)
	{
		if(value == null)
		{
			throw new NullPointerException("Checked date can not be null");
		}		
		if(!this.checked.contains(value))
		{
			this.dataWasChanged = true;
			this.checked.add(value);
		}
	}
	public void uncheck(Date value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.checked.contains(value);
		
		this.checked.remove(value);
	}
	public void clearChecks()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.checked.size() != 0;
		
		this.checked.clear();
	}
	public ArrayList<Date> getSelectedDates()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for (int i = 0; i < this.checked.size(); ++i)
			result.add(this.checked.get(i));
		return result;
	}	
	public void checkBooked(Date value)
	{
		if(value == null)
		{
			throw new NullPointerException("Booked date can not be null");
		}
		if(!this.rebooked.contains(value))
		{
			this.dataWasChanged = true;
			this.rebooked.add(value);
		}
	}
	public void clearBookedChecks()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.rebooked.size() != 0;
		
		this.rebooked.clear();
	}	
	public Date stateChanged(int value)
	{
		this.dataWasChanged = true;
		
		if(value == -1)
			this.currentMonth.addMonth(-1);
		else if(value == -2)
			this.currentMonth.addMonth(1);
		else if(value == -3)
			this.currentMonth.addYear(1);
		else if(value == -4)
			this.currentMonth.addYear(-1);
		else if(value <= -10)
			this.currentMonth = new Date(this.currentMonth.getYear(), -value - 9, 1);
		else if(value < 32)
			this.selectedDate = new Date(this.currentMonth.getYear(), this.currentMonth.getMonth(), value);
		else if(value < 2000)
		{
			Date date = new Date(this.currentMonth.getYear(), this.currentMonth.getMonth(), value - 1000);
			this.checked.add(date);
			return date;
		}
		else
		{	
			Date date = new Date(this.currentMonth.getYear(), this.currentMonth.getMonth(), value - 2000);
			this.checked.remove(date);
			return date;
		}
		return null;
	}
	public ArrayList<DayOfWeek> getDisabledDaysOfWeek()
	{
		return this.disabledDaysOfWeek;
	}
	public ArrayList<Date> getNoSessionDays()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for (int i = 0; i < this.noSessionDates.size(); ++i)
		{
			Date d = this.noSessionDates.get(i);
			if (d.getMonth() == this.currentMonth.getMonth() && d.getYear() == this.currentMonth.getYear())
				result.add(d);
		}
		return result;
	}
	public ArrayList<Date> getConflictDays()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for (int i = 0; i < this.conflicts.size(); ++i)
		{
			Date d = this.conflicts.get(i);
			if (d.getMonth() == this.currentMonth.getMonth() && d.getYear() == this.currentMonth.getYear())
				result.add(d);
		}
		return result;
	}
	public ArrayList<Date> getNoSlotsDays()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for (int i = 0; i < this.noSlotDates.size(); ++i)
		{
			Date d = this.noSlotDates.get(i);
			if (d.getMonth() == this.currentMonth.getMonth() && d.getYear() == this.currentMonth.getYear())
				result.add(d);
		}
		return result;
	}
	public ArrayList<Integer> getPercentage()
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		Date start = new Date(this.currentMonth.getYear(), this.currentMonth.getMonth(), 1);
		Date d = new Date(start);
		while (d.getMonth() == start.getMonth())
		{
			result.add(this.percentage.containsKey(d.toString()) ? this.percentage.get(d.toString()) : new Integer(0));
			d.addDay(1);
		}
		return result;
	}
	public ArrayList<Date> getBookedDays()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for (int i = 0; i < this.checked.size(); ++i)
		{
			Date d = this.checked.get(i);
			if (d.getMonth() == this.currentMonth.getMonth() && d.getYear() == this.currentMonth.getYear())
				result.add(d);
		}
		return result;
	}
	public ArrayList<Date> getRebookedDays()
	{
		ArrayList<Date> result = new ArrayList<Date>();
		for (int i = 0; i < this.rebooked.size(); ++i)
		{
			Date d = this.rebooked.get(i);
			if (d.getMonth() == this.currentMonth.getMonth() && d.getYear() == this.currentMonth.getYear())
				result.add(d);
		}
		return result;
	}
	public boolean areChecksReadOnly()
	{
		return checksReadOnly;
	}
	public void setChecksReadOnly(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.checksReadOnly != value;
		
		this.checksReadOnly = value;
	}	
	
	@Deprecated
	public void setBackColor(Date dt, Color backColor)
	{
		DateColor dateColor = new DateColor(dt, backColor);
		if(backColor == null)
		{
			boolean result = datesBackColor.remove(dateColor);
			if(result)
				this.dataWasChanged = true;			
		}
		else
		{
			if(datesBackColor.contains(dateColor))
				datesBackColor.remove(dateColor);
			datesBackColor.add(dateColor);
			this.dataWasChanged = true;
		}
	}
	public void clearBackColors()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = datesBackColor.size() > 0;
			
		datesBackColor.clear();
	}	
	
	public void setDateColor(Date dt, Color backgroundColor, Color textColor)
	{
		DateColor dateColor = new DateColor(dt, backgroundColor, textColor);
		
		if(backgroundColor == null)
		{
			if(datesBackColor.contains(dateColor))
			{
				DateColor currentDateColor = datesBackColor.get(datesBackColor.indexOf(dateColor));
				currentDateColor.backgroundColor = Color.White;
			}
				
			this.dataWasChanged = true;			
		}
		if(textColor == null)
		{
			if(datesBackColor.contains(dateColor))
			{
				DateColor currentDateColor = datesBackColor.get(datesBackColor.indexOf(dateColor));
				currentDateColor.textColor = null;
			}
				
			this.dataWasChanged = true;		
		}
		else
		{
			if(datesBackColor.contains(dateColor))
				datesBackColor.remove(dateColor);
			datesBackColor.add(dateColor);
			this.dataWasChanged = true;
		}
	}
	public void clearDateColor()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = datesBackColor.size() > 0;
			
		datesBackColor.clear();
	}	
	public ArrayList<DateColor> getDatesBackColor()
	{
		return datesBackColor;
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	private Date currentMonth = new Date();
	private Date selectedDate = null;
	private ArrayList<Date> conflicts = new ArrayList<Date>();
	private ArrayList<DayOfWeek> disabledDaysOfWeek = new ArrayList<DayOfWeek>();
	private ArrayList<Date> noSessionDates = new ArrayList<Date>();
	private ArrayList<Date> noSlotDates = new ArrayList<Date>();
	private HashMap<String, Integer> percentage = new HashMap<String, Integer>();
	private ArrayList<Date> checked = new ArrayList<Date>();
	private ArrayList<Date> rebooked = new ArrayList<Date>();
	private ArrayList<DateColor> datesBackColor = new ArrayList<DateColor>();
	
	private boolean checksReadOnly = false;	
}
