package ims.framework.cn.data;

import java.util.ArrayList;

import ims.framework.exceptions.FrameworkInternalException;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.Time;
import ims.framework.utils.TimeSpan;

public class AvailabilityControlData extends ChangeableData implements IControlData
{
	private static final long serialVersionUID = -8171079579583770831L;
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
	public Date getEndDate()
	{
		return this.endDate;
	}
	public void setEndDate(Date value)
	{
		if(!this.dataWasChanged)
		{
			if(this.endDate == null)
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.endDate.equals(value);
		}
		
		this.endDate = value;
	}
	public Date getStartDate()
	{
		return this.startDate;
	}
	public void setStartDate(Date value)
	{
		if(!this.dataWasChanged)
		{
			if(this.startDate == null)			
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.startDate.equals(value);
		}
		
		this.startDate = value;
	}
	public void addExcludedDate(Date date) throws FrameworkInternalException
	{
		this.dataWasChanged = true;
		this.excludedDates.add(date);
		
		if (this.list == null)
		{
			this.list = new ACDate(date);
		}
		else
		{	
			ACDate current = this.list;
			while (current != null)
			{
				if(date.isLessThan(current.getDate()))
				{
					ACDate d = new ACDate(date, new Time(8,0), new Time(20, 0), ACFlag.UNAVAILABLE);
					
					d.setNext(current);
					if(date.isLessThan(this.list.getDate()))
					{
						ACDate tmp = this.list;
						this.list = d;
						this.list.setNext(tmp);
					}
					else
					{
						//Replace the next pointer of the previous object that has "current" as next pointer 
						//with the newly added one.
						ACDate prevObj = getPrevObject(current);
						if(prevObj != null)
							prevObj.setNext(d);
					}
					return;
				}
				else
				if(date.equals(current.getDate()))
				{
					current.setPeriods(new ACTime(new Time(8,0), new Time(20, 0), ACFlag.UNAVAILABLE));
					return;
				}
				else
				{
					if(current.getNext() == null)
					{
						ACDate d = new ACDate(date, new Time(8,0), new Time(20, 0), ACFlag.UNAVAILABLE);
						
						current.setNext(d);
						return;
					}
					
					current = current.getNext();					
				}
			}
			
/*			ACDate previous = null;
			while (current != null)
			{
				int diff = TimeSpan.getTimeSpan(date, current.getDate()).getDays();
				if (diff == 0)
				{
					current.setPeriods(new ACTime(new Time(8,0), new Time(20, 0), ACFlag.UNAVAILABLE));
					return;
				}
				else if (diff < 0)
				{
					ACDate d = new ACDate(date);
					d.setNext(current);
					if (previous != null)
						previous.setNext(d);
					return;
				}
				previous = current;
				current = current.getNext();
			}
			previous.setNext(new ACDate(date));
*/		
		}
	}
	public void addBookedTime(Date date, Time startTime, Time endTime) throws FrameworkInternalException
	{
		if(this.excludedDates.contains(date))
		{
			throw new RuntimeException("Date was excluded");
		}
		
		this.dataWasChanged = true;		
		if(this.list == null)
		{
			this.list = new ACDate(date, startTime, endTime, ACFlag.BOOKED);
		}
		else
		{	
			ACDate current = this.list;
			while (current != null)
			{
				if(date.isLessThan(current.getDate()))
				{
					ACDate d = new ACDate(date, startTime, endTime, ACFlag.BOOKED);
					
					d.setNext(current);
					if(date.isLessThan(this.list.getDate()))
					{
						ACDate tmp = this.list;
						this.list = d;
						this.list.setNext(tmp);
					}
					else
					{
						//Replace the next pointer of the previous object that has "current" as next pointer 
						//with the newly added one.
						ACDate prevObj = getPrevObject(current);
						if(prevObj != null)
							prevObj.setNext(d);
					}
					return;
				}
				else
				if(date.equals(current.getDate()))
				{
					current.addPeriod(startTime, endTime, ACFlag.BOOKED);
					return;
				}
				else
				{
					if(current.getNext() == null)
					{
						ACDate d = new ACDate(date, startTime, endTime, ACFlag.BOOKED);
						
						current.setNext(d);
						return;
					}
					
					current = current.getNext();
				}
			}
/*			ACDate previous = null;
			while (current != null)
			{
				int diff = TimeSpan.getTimeSpan(date, current.getDate()).getDays();
				if (diff == 0)
				{
					current.addPeriod(startTime, endTime, ACFlag.BOOKED);
					return;
				}
				else if (diff < 0)
				{
					ACDate d = new ACDate(date);
					d.setNext(current);
					if (previous != null)
						previous.setNext(d);
					return;
				}
				previous = current;
				current = current.getNext();
			}
			previous.setNext(new ACDate(date, startTime, endTime, ACFlag.BOOKED));
*/
		}
	}
	public void addUnavailableTime(Date date, Time startTime, Time endTime) throws FrameworkInternalException
	{
		if(this.excludedDates.contains(date))
		{
			throw new RuntimeException("Date was excluded");
		}
		
		this.dataWasChanged = true;
		if(this.list == null)
		{
			this.list = new ACDate(date, startTime, endTime, ACFlag.UNAVAILABLE);
		}
		else
		{	
			ACDate current = this.list;
			while (current != null)
			{
				if(date.isLessThan(current.getDate()))
				{
					ACDate d = new ACDate(date, startTime, endTime, ACFlag.UNAVAILABLE);
					
					d.setNext(current);
					if(date.isLessThan(this.list.getDate()))
					{
						//Set the new added date to be the first
						ACDate tmp = this.list;
						this.list = d;
						this.list.setNext(tmp);
					}
					else
					{
						//Replace the next pointer of the previous object that has "current" as next pointer to 
						//with the newly added one.
						ACDate prevObj = getPrevObject(current);
						if(prevObj != null)
							prevObj.setNext(d);
					}
					return;
				}
				else
				if(date.equals(current.getDate()))
				{
					current.addPeriod(startTime, endTime, ACFlag.UNAVAILABLE);
					return;
				}
				else
				{
					if(current.getNext() == null)
					{
						ACDate d = new ACDate(date, startTime, endTime, ACFlag.UNAVAILABLE);
						
						current.setNext(d);
						return;
					}
					
					current = current.getNext();
				}
			}
/*			ACDate previous = null;
			while (current != null)
			{
				int diff = TimeSpan.getTimeSpan(date, current.getDate()).getDays();
				if (diff == 0)
				{
					current.addPeriod(startTime, endTime, ACFlag.UNAVAILABLE);
					return;
				}
				else if (diff < 0)
				{
					ACDate d = new ACDate(date);
					d.setNext(current);
					if (previous != null)
						previous.setNext(d);
					return;
				}
				previous = current;
				current = current.getNext();
			}
			previous.setNext(new ACDate(date, startTime, endTime, ACFlag.UNAVAILABLE));
*/
		}
	}
	
	//Find the object that has "current" as the next pointer  
	private ACDate getPrevObject(ACDate current)
	{
		ACDate acDate = this.list;
		if(current != null)
			while(acDate.getNext() != null)
			{
				if(acDate.getNext().equals(current))
					return acDate;
				
				acDate = acDate.getNext();
			}
		
		return null;
	}
	public void addTooltip(Date date, String tooltip)
	{
		this.dataWasChanged = true;
		if(this.list == null)
		{
			this.list = new ACDate(date, tooltip);
		}
		else
		{	
			ACDate current = this.list;
			ACDate previous = null;
			while (current != null)
			{
				int diff = TimeSpan.getTimeSpan(date, current.getDate()).getDays();
				if (diff == 0)
				{
					current.setTooltip(tooltip);
					return;
				}
				else if (diff < 0)
				{
					ACDate d = new ACDate(date, tooltip);
					d.setNext(current);
					if (previous != null)
						previous.setNext(d);
					return;
				}
				previous = current;
				current = current.getNext();
			}
			previous.setNext(new ACDate(date, tooltip));
		}
	}
	public void clear()
	{
		this.dataWasChanged = true;
		this.startDate = new Date();
		this.endDate = new Date();
		this.list = null;
		this.excludedDates.clear();
	}
	public void render(StringBuffer sb)
	{
		boolean[] pattern = new boolean[721]; // 20.00 is also a minute
		for (int i = 0; i < pattern.length; ++i)
			pattern[i] = true;
		
		ACDate currentDate = this.list;
		while (currentDate != null)
		{
			if (!this.excludedDates.contains(currentDate))
			{
				ACTime currentTime = currentDate.getPeriods();
				while (currentTime != null)
				{
					if (!currentTime.getFlag().equals(ACFlag.AVAILABLE))
					{
						int start = currentTime.getStartTime().getHour() * 60 + currentTime.getStartTime().getMinute() - 480;
						int end = currentTime.getEndTime().getHour() * 60 + currentTime.getEndTime().getMinute() - 480;
						
						if ((start>0)&&(end>0))
						{
							for (int i = start; i < end; ++i)
								pattern[i] = false;
						}
					}
					currentTime = currentTime.getNext();
				}
			}
			currentDate = currentDate.getNext();
		}
		
		// Render
		currentDate = this.list;
		while (currentDate != null)
		{
			int diff = TimeSpan.getTimeSpan(this.startDate, currentDate.getDate()).getDays();
			if (diff <= 0)
			{	
				diff = TimeSpan.getTimeSpan(this.endDate, currentDate.getDate()).getDays();
				if (diff < 0)
					break;
				sb.append("<date value=\"");
				sb.append(currentDate.getDate().toString(DateFormat.RUSSIAN));
				sb.append("\" tooltip=\"");
				sb.append(ims.framework.utils.StringUtils.encodeXML(currentDate.getTooltip()));
				sb.append("\">");
				ACTime currentTime = currentDate.getPeriods();
				while (currentTime != null)
				{
					if (currentTime.getFlag().equals(ACFlag.AVAILABLE))
					{
						int start = currentTime.getStartTime().getHour() * 60 + currentTime.getStartTime().getMinute() - 480;
						int end = currentTime.getEndTime().getHour() * 60 + currentTime.getEndTime().getMinute() - 480;
						
						if ((start>0)&&(end>0))
						{

							int i = start;
							while (i < end)
							{
								sb.append("<p type=\"");
								boolean p = pattern[i];
								sb.append(p ? "pattern" : "avail");
								sb.append("\" time=\"");
								int k = i + 480;
								sb.append(new Time(k / 60, k % 60).toString());
								while (i < end)
								{
									if (pattern[i] != p)
										break;
									i++;
								}
								sb.append('-');
								k = i + 480;
								sb.append(new Time(k / 60, k % 60).toString());
								sb.append("\"/>");
							}
						}
					}
					else
					{	
						sb.append("<p type=\"");
						if (currentTime.getFlag().equals(ACFlag.UNAVAILABLE))
							sb.append("unavail");
						else if (currentTime.getFlag().equals(ACFlag.BOOKED))
							sb.append("booked");
						sb.append("\" time=\"");
						sb.append(currentTime.getStartTime().toString());
						sb.append('-');
						sb.append(currentTime.getEndTime().toString());
						sb.append("\"/>");
					}
					currentTime = currentTime.getNext();
				}
				sb.append("</date>");
			}
			currentDate = currentDate.getNext();
		}
	}
	private boolean enabled = true;
	private boolean visible = true;
	private Date startDate = new Date();
	private Date endDate = new Date();
	private ACDate list = null;
	private ArrayList<Date> excludedDates = new ArrayList<Date>();
}

class ACDate
{
	ACDate(Date date)
	{
		this.date = date;
		this.tooltip = "";
		this.periods = new ACTime(new Time(8,0), new Time(20, 0), ACFlag.UNAVAILABLE);
		this.next = null;
	}
	ACDate(Date date, String tooltip) // used by addTooltip
	{
		this.date = date;
		this.tooltip = tooltip;
		this.periods = new ACTime(new Time(8,0), new Time(20, 0), ACFlag.AVAILABLE);
		this.next = null;
	}
	ACDate(Date date, Time startTime, Time endTime, ACFlag flag) throws FrameworkInternalException
	{
		this.date = date;
		this.tooltip = "";
		this.addNewPeriod(startTime, endTime, flag);
		this.next = null;
	}
	Date getDate()
	{
		return this.date;
	}
	String getTooltip()
	{
		return this.tooltip;
	}
	void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}
	ACDate getNext()
	{
		return this.next;
	}
	void setNext(ACDate next)
	{
		this.next = next;
	}
	ACTime getPeriods()
	{
		return this.periods;
	}
	void setPeriods(ACTime periods)
	{
		this.periods = periods;
	}
	void addPeriod(Time startTime, Time endTime, ACFlag flag) throws FrameworkInternalException 
	{
		this.checkTime(startTime, endTime);
		
		ACTime current = this.periods;
		ACTime previous = null;
		while (current != null)
		{
			int startDiff = TimeSpan.getTimeSpan(startTime, current.getStartTime()).getMinutes(); 
			if (startDiff >= 0)
			{
				int diff = TimeSpan.getTimeSpan(current.getEndTime(), startTime).getMinutes();
				if (diff > 0) // starting point is in "current" period
				{
					if (flag.equals(current.getFlag()))
					{
						diff = TimeSpan.getTimeSpan(current.getEndTime(), endTime).getMinutes();
						if (diff >= 0)
							return; // the new period is entirely inside "current" period, so do nothing
						Time newStartTime = current.getStartTime();
						Time newEndTime = endTime;
						
						while (diff < 0)
						{
							current = current.getNext(); // current can't be null
							diff = TimeSpan.getTimeSpan(current.getEndTime(), endTime).getMinutes();
						}

						if (diff > 0 && current.getFlag().equals(flag))
							newEndTime = current.getEndTime();
						ACTime d = new ACTime(newStartTime, newEndTime, flag);
						if (previous != null)
							previous.setNext(d);
						else
							this.periods = d;
						if (diff > 0 && !current.getFlag().equals(flag))
						{
							ACTime rest = new ACTime(endTime, current.getEndTime(), current.getFlag());
							rest.setNext(current.getNext());
							d.setNext(rest);
							return;
						}
						d.setNext(current.getNext());
						return;
					}
					
					ACTime d = null;
					if (startDiff > 0)
					{	
						d = new ACTime(current.getStartTime(), startTime, current.getFlag());
						if (previous != null)
							previous.setNext(d);
						else
							this.periods = d;
					}
					
					Time newStartTime = startTime;
					Time newEndTime = endTime;
					
					diff = TimeSpan.getTimeSpan(current.getEndTime(), endTime).getMinutes();
					while (diff < 0)
					{
						current = current.getNext();
						diff = TimeSpan.getTimeSpan(current.getEndTime(), endTime).getMinutes();
					}

					if (diff > 0 && current.getFlag().equals(flag))
						newEndTime = current.getEndTime();
					ACTime newPeriod = new ACTime(newStartTime, newEndTime, flag);
					if (startDiff > 0)
						d.setNext(newPeriod);
					else 
					{
						if (previous != null)
							previous.setNext(newPeriod);
						else
							this.periods = newPeriod;
					}
					
					if (diff > 0 && !current.getFlag().equals(flag))
					{
						ACTime rest = new ACTime(endTime, current.getEndTime(), current.getFlag());
						rest.setNext(current.getNext());
						newPeriod.setNext(rest);
						return;
					}
					newPeriod.setNext(current.getNext());
					return;					
				}
			}
			previous = current;
			current = current.getNext();
		}
	}
	private void addNewPeriod(Time startTime, Time endTime, ACFlag flag) throws FrameworkInternalException 
	{
		this.checkTime(startTime, endTime);

		int diffS = TimeSpan.getTimeSpan(startTime, new Time(8,0)).getMinutes();
		int diffE = TimeSpan.getTimeSpan(new Time(20,0), endTime).getMinutes();
		
		if (diffS > 0)
			this.periods = new ACTime(new Time(8,0), startTime, ACFlag.AVAILABLE);
		
		ACTime d = new ACTime(startTime, endTime, flag);
		if (this.periods != null)
			this.periods.setNext(d);
		else
			this.periods = d;
		
		if (diffE > 0)
		{
			ACTime t = new ACTime(endTime, new Time(20, 0), ACFlag.AVAILABLE);
			d.setNext(t);
		}
	}
	private void checkTime(Time startTime, Time endTime) throws ims.framework.exceptions.FrameworkInternalException
	{
		int diff = TimeSpan.getTimeSpan(endTime, startTime).getMinutes();
		if (diff <= 0)
			throw new FrameworkInternalException("End time must be greater then start time");
		
		//diff = TimeSpan.getTimeSpan(startTime, new Time(8,0)).getMinutes();
		//if (diff < 0 || diff > 719)
			//throw new RuntimeException("Start time must be between 8.00 and 19.59");
		
		//diff = TimeSpan.getTimeSpan(new Time(20,0), endTime).getMinutes();
		//if (diff < 0 || diff > 719)
			//throw new RuntimeException("End time must be between 8.01 and 20.00");
	}
	private Date date;
	private String tooltip;
	private ACTime periods; // the top of the list of periods
	private ACDate next; // next date in the list
}

class ACTime
{
	ACTime(Time startTime, Time endTime, ACFlag flag)
	{
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
	}
	Time getStartTime()
	{
		return this.startTime;
	}
	Time getEndTime()
	{
		return this.endTime;
	}
	ACTime getNext()
	{
		return this.next;
	}
	void setNext(ACTime next)
	{
		this.next = next;
	}
	ACFlag getFlag()
	{
		return this.flag;
	}
	private Time startTime;
	private Time endTime;
	private ACFlag flag;
	private ACTime next = null;
}

class ACFlag
{
	public static final ACFlag AVAILABLE = new ACFlag(0);
	public static final ACFlag UNAVAILABLE = new ACFlag(1);
	public static final ACFlag BOOKED = new ACFlag(2);
	
	private ACFlag(int id)
	{
		this.id = id;
	}
	protected int id;
}