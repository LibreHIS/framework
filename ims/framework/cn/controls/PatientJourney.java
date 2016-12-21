package ims.framework.cn.controls;

import java.util.ArrayList;
import java.util.List;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.PatientJourneyData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.PatientJourneySelectionChanged;
import ims.framework.controls.PatientJourneyClock;
import ims.framework.controls.PatientJourneyClocks;
import ims.framework.controls.PatientJourneyEntries;
import ims.framework.controls.PatientJourneyEntry;
import ims.framework.controls.PatientJourneyItem;
import ims.framework.controls.PatientJourneyMarker;
import ims.framework.controls.PatientJourneyMarkers;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Date;
import ims.framework.utils.StringUtils;

public class PatientJourney extends ims.framework.controls.PatientJourney
{	
	private static final long serialVersionUID = 1L;

	protected int tabIndex;
	protected boolean autoPostBack;
	private PatientJourneyData data;
	private List<PatientJourneyClock> renderedClocks = new ArrayList<PatientJourneyClock>();
		
	public final void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu, boolean autoPostBack)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
	}
	
	@Override
	public void clear()
	{	
		data.clear();		
	}
	@Override
	public void setStartDate(Date startDate)
	{
		data.setStartDate(startDate);
	}
	@Override
	public PatientJourneyEntries getEntries()
	{
		return data.getEntries();
	}
	@Override
	public PatientJourneyMarkers getMarkers()
	{
		return data.getMarkers();
	}
	@Override
	public PatientJourneyClocks getClocks()
	{
		return data.getClocks();
	}
	@Override
	public String getJourneyHeaderText()
	{
		return data.getJourneyHeaderText();
	}
	@Override
	public String getProfileHeaderText()
	{
		return data.getProfileHeaderText();
	}
	@Override
	public void setJourneyHeaderText(String value)
	{
		data.setJourneyHeaderText(value);
	}
	@Override
	public void setProfileHeaderText(String value)
	{
		data.setProfileHeaderText(value);
	}	
	@Override
	public int getSplitterPercentage()
	{
		return 0;
	}
	@Override
	public void setSplitterPercentage(int value)
	{
	}
	@Override
	public PatientJourneyEntry getSelectedEntry()
	{
		return data.getSelectedEntry();
	}	
	@Override
	public void setSelectedEntry(PatientJourneyEntry entry)
	{
		data.setSelectedEntry(entry);
	}
	@Override
	public void scrollToSelection()
	{
		data.scrollToSelection();
	}
	@Override
	public void scrollToCurrentWeek()
	{
		data.scrollToCurrentWeek();
	}
	
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof PatientJourneySelectionChanged)
		{
			PatientJourneySelectionChanged typedEvent = (PatientJourneySelectionChanged)event;
			
			PatientJourneyMarker journeyMarker = null;
			PatientJourneyEntry journeyEntry = null;
			
			PatientJourneyMarkers markersEntries = data.getMarkers();
			for(int x = 0; x < markersEntries.size(); x++)
			{
				System.out.println("PositionIndex: " + markersEntries.get(x).getPositionIndex());
				if((markersEntries.get(x).getPositionIndex()*1000 == typedEvent.getValue()) ||
						(markersEntries.get(x).getPositionIndex()*10000 == typedEvent.getValue()) ||
						(markersEntries.get(x).getPositionIndex() == typedEvent.getValue()))
				{				
					journeyMarker = markersEntries.get(x);
					break;
				}
			}
			
			PatientJourneyEntries journeyEntries = data.getEntries();
			for(int x = 0; x < journeyEntries.size(); x++)
			{
				System.out.println("PositionIndex: " + journeyEntries.get(x).getPositionIndex());
				if((journeyEntries.get(x).getPositionIndex()*1000 == typedEvent.getValue()) ||
						(journeyEntries.get(x).getPositionIndex()*10000 == typedEvent.getValue()) ||
						(journeyEntries.get(x).getPositionIndex() == typedEvent.getValue()))
				{				
					journeyEntry = journeyEntries.get(x);
					break;
				}
			}
			
			if(journeyMarker == null && journeyEntry == null)
				throw new RuntimeException("Unable to find patient journey entry with ID: " + typedEvent.getValue());
			
			if (journeyMarker != null)
				data.setSelectedMarker(journeyMarker);
			else if (journeyEntry != null)
				data.setSelectedEntry(journeyEntry);
			
			data.setSelectionUnchanged();
			
			if(selectionChangedDelegate != null)
				selectionChangedDelegate.handle(journeyMarker != null ? (Object)journeyMarker : (Object)journeyEntry);
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<imsjourney id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);		
		
		if(autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
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
		if(data.getStartDate() == null)
			throw new CodingRuntimeException("Start date not specified.");
		
		renderedClocks.clear();
		
		sb.append("<imsjourney id=\"a");
		sb.append(super.id);
		
		if(data.getEntries().wasChanged() || data.isSelectionChanged())
		{
			sb.append("\" selectedID=\"");
			int index = data.getSelectedEntry() == null ? -1 : data.getEntries().indexOf(data.getSelectedEntry());			
			if (index >=0)
				sb.append(index);
			else
				sb.append("");
			//sb.append(index >= 0 ? index : "");
			
			data.setSelectionUnchanged();
		}
		
		if(data.getScrollToRow() >= 0)
		{
			sb.append("\" scrollToRow=\"");
			sb.append(data.getScrollToRow());
			data.resetScrollToRow();
		}
		
		if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledUnchanged();
				}
			}
			
			sb.append("\" >");
			
			if(data.isHeaderChanged())
			{
				sb.append("<cols>");
				// WDEV-21038 - Do not display title
				//sb.append("<col title=\"" + StringUtils.encodeXML(data.getJourneyHeaderText()) + "\"/>");
				//sb.append("<col title=\"" + StringUtils.encodeXML(data.getProfileHeaderText()) + "\"/>");
				sb.append("</cols>");
				
				data.setHeaderUnchanged();
			}
						
			if(data.getEntries().wasChanged() || data.getMarkers().wasChanged() || data.getClocks().wasChanged())
			{
				if(data.getEntries().size() == 0 && data.getClocks().size() == 0)
				{
					sb.append("<rows/>");	
				}
				else
				{		
					sb.append("<rows>");					
					ArrayList<PatientJourneyItem> items = data.getAllEntries();
					int maxWeekNumber = getMaxWeekNumber(items);
					for(int x = 0; x < items.size(); x++)
					{
						PatientJourneyItem item = items.get(x);
						
						if(item instanceof PatientJourneyMarker)
						{
							PatientJourneyMarker entry = (PatientJourneyMarker)item;
														
							boolean isCurrentWeek = data.isCurrentWeek(entry);
							sb.append("<r marker=\"true\" id=\"" + entry.getWeekNumber() * 1000 + "\" highlight=\"" + (isCurrentWeek ? "true" : "false") + "\" type=\"separator\">");
														
							sb.append("<c text=\"");
							sb.append(StringUtils.encodeXML(entry.getText()));
							if(isCurrentWeek)
							{
								sb.append(" - [Current Week]");
							}
							sb.append("\" />");
							
							// Clock
							// -----------------------
							renderClock(sb, entry, isLastItemForTheWeekNumber(items, x, entry.getWeekNumber()), maxWeekNumber);
							// -----------------------
							
							sb.append("</r>");
						}
						else if(item instanceof PatientJourneyEntry)
						{
							PatientJourneyEntry entry = (PatientJourneyEntry)item;
							
							sb.append("<r id=\"" + entry.getPositionIndex() + "\" week=\"" + entry.getWeekNumber());
							
							if(!entry.isSelectable())
								sb.append("\" selectable=\"false");
							
							sb.append("\">");
							
							sb.append("<c text=\"");
							sb.append(StringUtils.encodeXML(entry.getJourneyText()));
							
							if(!entry.isJourneyWeekNumberVisible())
							{
								sb.append("\" noWeek=\"true");
							}
							else if(entry.hasJourneyWeekNumberLabel())
							{
								sb.append("\" weekText=\"");
								sb.append(StringUtils.encodeXML(entry.getJourneyWeekNumberLabel()));
							}
							
							sb.append("\" tooltip=\"");
							sb.append(StringUtils.encodeXML(entry.getJourneyTooltip()));
							if(entry.getJourneyTextColor() != null)
							{
								sb.append("\" textColor=\"");
								sb.append(entry.getJourneyTextColor());
							}
							if(entry.getJourneyBackColor() != null)
							{
								sb.append("\" backColor=\"");
								sb.append(entry.getJourneyBackColor());
							}
							if(entry.getJourneyImage() != null)
							{
								sb.append("\" img=\"");
								sb.append(entry.getJourneyImage().getImagePath());
							}
							sb.append("\" />");
							
							// Clock
							// -----------------------
									renderClock(sb, entry, isLastItemForTheWeekNumber(items, x, entry.getWeekNumber()), maxWeekNumber);
							// -----------------------
							
							sb.append("<c text=\"");
							sb.append(StringUtils.encodeXML(entry.getProfileText()));
							
							if(!entry.isProfileWeekNumberVisible())
							{
								sb.append("\" noWeek=\"true");
							}
							else if(entry.hasProfileWeekNumberLabel())
							{
								sb.append("\" weekText=\"");
								sb.append(StringUtils.encodeXML(entry.getProfileWeekNumberLabel()));
							}
							
							sb.append("\" tooltip=\"");
							sb.append(StringUtils.encodeXML(entry.getProfileTooltip()));
							if(entry.getProfileTextColor() != null)
							{
								sb.append("\" textColor=\"");
								sb.append(entry.getProfileTextColor());
							}
							if(entry.getProfileBackColor() != null)
							{
								sb.append("\" backColor=\"");
								sb.append(entry.getProfileBackColor());
							}
							if(entry.getProfileImage() != null)
							{
								sb.append("\" img=\"");
								sb.append(entry.getProfileImage().getImagePath());
							}
							sb.append("\" />");
							
							sb.append("</r>");
						}
					}
					sb.append("</rows>");				
				}
				
				data.getEntries().markUnchanged();
				data.getMarkers().markUnchanged();
			}
		}
		else
		{
			sb.append("\" >");
		}
		
		sb.append("</imsjourney>");
	}
	private int getMaxWeekNumber(ArrayList<PatientJourneyItem> items)
	{
		return getMaxWeekNumber(items, true);
	}
	private int getMaxWeekNumber(ArrayList<PatientJourneyItem> items, boolean excludeMarkers)
	{
		int result = 0;		
		for(int x = 0; x < items.size(); x++)
		{
			PatientJourneyItem item = items.get(x);
			
			if(item instanceof PatientJourneyMarker && !excludeMarkers)
			{
				PatientJourneyMarker entry = (PatientJourneyMarker)item;				
				if(entry.getWeekNumber() > result)
					result = entry.getWeekNumber(); 
			}
			else if(item instanceof PatientJourneyEntry)
			{
				PatientJourneyEntry entry = (PatientJourneyEntry)item;				
				if(entry.getWeekNumber() > result)
					result = entry.getWeekNumber();
			}
		}
		
		return result;
	}

	private boolean isLastItemForTheWeekNumber(ArrayList<PatientJourneyItem> items, int currentIndex, int weekNumber)
	{
		if(items == null)
			return true;
		if(currentIndex > items.size() - 2)
			return true;
		
		for(int x = currentIndex + 1; x < items.size(); x++)
		{
			PatientJourneyItem item = items.get(x);
			if(item instanceof PatientJourneyMarker)
			{
				if(((PatientJourneyMarker)item).getWeekNumber() == weekNumber)
					return false;
			}
			else if(item instanceof PatientJourneyEntry)
			{
				if(((PatientJourneyEntry)item).getWeekNumber() == weekNumber)
					return false;
			}
			
		}			
		
		return true;
	}
	private PatientJourneyClock renderClock(StringBuffer sb, PatientJourneyMarker entry, boolean isLastItemForTheWeekNumber, int maxWeekNumber)
	{
		return renderClock(sb, entry.getWeekNumber(), isLastItemForTheWeekNumber, maxWeekNumber);
	}
	private PatientJourneyClock renderClock(StringBuffer sb, PatientJourneyEntry entry, boolean isLastItemForTheWeekNumber, int maxWeekNumber)
	{
		return renderClock(sb, entry.getWeekNumber(), isLastItemForTheWeekNumber, maxWeekNumber);
	}
	private PatientJourneyClock renderClock(StringBuffer sb, int week, boolean isLastItemForTheWeekNumber, int maxWeekNumber)
	{		
		PatientJourneyClock clock = getClock(week); 		
		if(clock == null)
		{
			sb.append("<c/>");			
		}
		else
		{
			boolean firstTime = false;
			if(renderedClocks.indexOf(clock) < 0)
			{
				firstTime = true;
				renderedClocks.add(clock);
			}
				
			sb.append("<c week=\"" + (week - clock.getStartWeekNumber() + 1) + "\" ");
			if(firstTime)
			{
				sb.append("start=\"true\" ");
				sb.append("tooltip=\"" + StringUtils.encodeXML(clock.getTooltip()) + "\" ");
			}
			
			if(isLastItemForTheWeekNumber)
			{
				boolean shouldStop = true;
				for(int x = week + 1; x <= clock.getEndWeekNumber(); x++)
				{
					if(hasEntryForWeekNumber(x))
					{
						PatientJourneyClock nextclock = getClock(x);
						if(clock.equals(nextclock))
						{
							shouldStop = false;
							break;
						}
					}
				}
				
				if((shouldStop) || 
						(clock.getEndWeekNumber() <= week))// ||  clock.getEndWeekNumber() (clock.getEndWeekNumber() > maxWeekNumber && maxWeekNumber == week)))
				{				
					sb.append("stop=\"true\" ");	
						
					if(clock.isEnded())
					{
						sb.append("stopLine=\"true\" ");
					}				
				}
			}
			
			sb.append("/>");
		}
		
		return clock;
	}	
	private boolean hasEntryForWeekNumber(int weekNumber)
	{
		ArrayList<PatientJourneyItem> items = data.getAllEntries();		
		for(int x = 0; x < items.size(); x++)
		{
			PatientJourneyItem item = items.get(x);
			if(item instanceof PatientJourneyMarker)
			{
				if(item.getWeekNumber() == weekNumber)
					return true;
			}	
			else if(item instanceof PatientJourneyEntry)
			{
				if(item.getWeekNumber() == weekNumber)
					return true;
			}			
		}
		
		return false;
	}
	private PatientJourneyClock getClock(int weekNumber)
	{
		for(int x = 0; x < data.getClocks().size(); x++)
		{
			PatientJourneyClock clock = data.getClocks().get(x);
			if(clock.getStartWeekNumber() <= weekNumber && clock.getEndWeekNumber() >= weekNumber)
				return clock;
		}
		
		return null;
	}

	public void restore(IControlData data, boolean isNew)
	{
		this.data = (PatientJourneyData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();		
	}
	public void markUnchanged()
	{
	}
	public boolean wasChanged()
	{
		if(data.isVisibleChanged())
			return true;		
		if(data.isSelectionChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					return true;
				}
			}
			if(data.isHeaderChanged())
				return true;
			if(data.getEntries().wasChanged())
				return true;
			if(data.getMarkers().wasChanged())
				return true;
		}
		return false;
	}
}
