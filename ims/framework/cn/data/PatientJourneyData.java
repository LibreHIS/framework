package ims.framework.cn.data;

import java.util.ArrayList;

import ims.framework.controls.PatientJourneyItem;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.IIDGenerator;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.Image;

public class PatientJourneyData implements IControlData
{
	private static final long serialVersionUID = 1L;
	
	public void clear()
	{
		setSelectedEntry(null);
		entries.clear();
		markers.clear();
		clocks.clear();
		idGenerator.reset();
		resetScrollToRow();
	} 
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public ims.framework.controls.PatientJourneyEntries getEntries()
	{
		return entries;
	}
	public ims.framework.controls.PatientJourneyMarkers getMarkers()
	{
		return markers;
	}
	public ims.framework.controls.PatientJourneyClocks getClocks()
	{
		return clocks;
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
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setJourneyHeaderText(String value)
	{
		if(value == null)
			value = "";
		
		if(!headerChanged)
			headerChanged = journeyHeaderText != value;
		
		journeyHeaderText = value;
	}
	public String getJourneyHeaderText()
	{
		return journeyHeaderText;
	}
	public void setProfileHeaderText(String value)
	{
		if(value == null)
			value = "";
		
		if(!headerChanged)
			headerChanged = profileHeaderText != value;
		
		profileHeaderText = value;
	}	
	public String getProfileHeaderText()
	{
		return profileHeaderText;
	}
	public PatientJourneyEntry getSelectedEntry()
	{		
		if(selection < 0)
			return null;
		
		return entries.get(selection);
	}		
	public void setSelectedEntry(ims.framework.controls.PatientJourneyEntry entry)
	{	
		int index = entries.indexOf(entry);
		
		if(!selectionChanged)
			selectionChanged = selection != index;
		
		selection = index;
	}
	public PatientJourneyMarker getSelectedMarker()
	{		
		if(selection < 0)
			return null;
		
		return markers.get(selection);
	}		
	public void setSelectedMarker(ims.framework.controls.PatientJourneyMarker entry)
	{	
		int index = markers.indexOf(entry);
		
		if(!selectionChanged)
			selectionChanged = selection != index;
		
		selection = index;
	}
	public void setSelectionUnchanged()
	{
		this.selectionChanged = false;
	}
	public boolean isSelectionChanged()
	{
		return selectionChanged;
	}
	public void setHeaderUnchanged()
	{
		this.headerChanged = false;
	}
	public boolean isHeaderChanged()
	{
		return headerChanged;
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	private Date startDate;
	private PatientJourneyEntries entries = new PatientJourneyEntries();
	private PatientJournerMarkers markers = new PatientJournerMarkers();
	private PatientJournerClocks clocks = new PatientJournerClocks();
	private String journeyHeaderText = "";
	private String profileHeaderText = "";
	private int selection = -1; 
	private int scrollSelection = -1;
	private IIDGenerator idGenerator = new IDGenerator();
	
	private boolean headerChanged = false;
	private boolean selectionChanged = false;
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	
	public class PatientJournerMarkers implements ims.framework.controls.PatientJourneyMarkers
	{
		private static final long serialVersionUID = 1L;
		
		private boolean wasChanged = false;
		private ArrayList<PatientJourneyMarker> list = new ArrayList<PatientJourneyMarker>();		
		
		public void add(PatientJourneyMarker item)
		{
			if(item == null)
				throw new CodingRuntimeException("Invalid patient journey marker");
			
			list.add(item);		
			wasChanged = true;
		}
		public void clear()
		{
			if(!wasChanged)
				wasChanged = list.size() > 0;
				
			list.clear();
			list.trimToSize();
		}
		public boolean remove(PatientJourneyMarker item)
		{		
			boolean removed = list.remove(item);
			if(removed && !wasChanged)
				wasChanged = true;
			
			return removed;
		}
		public boolean contains(PatientJourneyMarker item)	
		{
			return list.contains(item);
		}
		public PatientJourneyMarker get(int index)
		{
			return list.get(index);
		}
		public PatientJourneyMarker getByIdentifier(Object identifier)
		{
			if(identifier == null)
				return null;
			
			for (PatientJourneyMarker item : list)
			{
				if(item.getIdentifier() != null && item.getIdentifier().equals(identifier))
					return item;				
			}
			
			return null;
		}
		public int size()
		{
			return list.size();
		}
		public void markUnchanged()
		{
			wasChanged = false;
			for (PatientJourneyMarker item : list)
			{
				item.markUnchanged();
			}
		}
		public boolean wasChanged()
		{
			if(wasChanged)
				return true;
			
			for (PatientJourneyMarker item : list)
			{
				if(item.wasChanged())
					return true;
			}
			
			return false;
		}
		public PatientJourneyMarker newMarker(int weekNumber, String text)
		{
			PatientJourneyMarker newMarker = newMarker(weekNumber);
			newMarker.setText(text);			
			return newMarker;
		}
		public PatientJourneyMarker newMarker(int weekNumber)
		{
			if(weekNumber < 0)
				throw new CodingRuntimeException("Invalid week");
			
			PatientJourneyMarker newMarker = new PatientJourneyMarker(weekNumber);
			newMarker.positionIndex = idGenerator.nextId();
			list.add(newMarker);
			return newMarker;
		}
		public int indexOf(ims.framework.controls.PatientJourneyMarker entry)
		{
			if(entry == null)
				return -1;
			
			return list.indexOf(entry);
		}
		public boolean remove(ims.framework.controls.PatientJourneyMarker entry)
		{
			return list.remove(entry);
		}	
	}
	public class PatientJourneyEntries implements ims.framework.controls.PatientJourneyEntries
	{
		private static final long serialVersionUID = 1L;
		private boolean wasChanged = false;
		private ArrayList<PatientJourneyEntry> list = new ArrayList<PatientJourneyEntry>();
		
		public void add(PatientJourneyEntry item)
		{
			if(item == null)
				throw new CodingRuntimeException("Invalid patient journey entry");
			
			list.add(item);		
			wasChanged = true;
		}
		public void clear()
		{
			if(!wasChanged)
				wasChanged = list.size() > 0;
				
			list.clear();
			list.trimToSize();
		}
		public boolean remove(PatientJourneyEntry item)
		{		
			boolean removed = list.remove(item);
			if(removed && !wasChanged)
				wasChanged = true;
			
			return removed;
		}
		public boolean contains(PatientJourneyEntry item)	
		{
			return list.contains(item);
		}
		public PatientJourneyEntry get(int index)
		{
			return list.get(index);
		}
		public PatientJourneyEntry getByIdentifier(Object identifier)
		{
			if(identifier == null)
				return null;
			
			for (PatientJourneyEntry item : list)
			{
				if(item.getIdentifier() != null && item.getIdentifier().equals(identifier))
					return item;				
			}
			
			return null;
		}
		public int size()
		{
			return list.size();
		}
		public void markUnchanged()
		{
			wasChanged = false;
			for (PatientJourneyEntry item : list)
			{
				item.markUnchanged();
			}
		}
		public boolean wasChanged()
		{
			if(wasChanged)
				return true;
			
			for (PatientJourneyEntry item : list)
			{
				if(item.wasChanged())
					return true;
			}
			
			return false;
		}
		public PatientJourneyEntry newEntry(int weekNumber)
		{
			if(weekNumber < 0)
				throw new CodingRuntimeException("Invalid week");
			
			PatientJourneyEntry newEntry = new PatientJourneyEntry(weekNumber);
			newEntry.positionIndex = idGenerator.nextId();
			list.add(newEntry);
			return newEntry;
		}
		public int indexOf(ims.framework.controls.PatientJourneyEntry entry)
		{
			if(entry == null)
				return -1;
			
			return list.indexOf(entry);
		}
		public boolean remove(ims.framework.controls.PatientJourneyEntry entry)
		{
			return list.remove(entry);
		}			
	}
	public final class PatientJourneyEntry implements ims.framework.controls.PatientJourneyEntry
	{ 
		private static final long	serialVersionUID	= 1L;
		private boolean wasChanged = true;
		 
		private int positionIndex;
		private int weekNumber;
		private Object identifier;
		private String journeyText; 
		private String profileText;
		private String journeyTooltip; 
		private String profileTooltip;
		private Image journeyImage;
		private Image profileImage;
		private Color journeyTextColor;
		private Color profileTextColor;
		private Color journeyBackColor;
		private Color profileBackColor;
		private boolean showJourneyWeekNumber = true;
		private boolean showProfileWeekNumber = true;
		private boolean selectable = true;
		private String journeyWeekNumberLabel = null;
		private String profileWeekNumberLabel = null;		
		
		public PatientJourneyEntry(int weekNumber)
		{
			this(weekNumber, "", "");
		}
		public PatientJourneyEntry(int weekNumber, String journeyText, String profileText)
		{
			this(weekNumber, null, journeyText, profileText, null, null); 
		}
		public PatientJourneyEntry(int weekNumber, Object identifier, String journeyText, String profileText, Image journeyImage, Image profileImage)
		{
			this.weekNumber = weekNumber;
			this.identifier = identifier;			
			this.journeyText = journeyText;
			this.profileText = profileText; 
			this.journeyImage = journeyImage;
			this.profileImage = profileImage;
		}
			
		public int getPositionIndex()
		{
			return positionIndex;
		}
		public Object getIdentifier()
		{
			return identifier;
		}
		public void setIdentifier(Object value)
		{
			if(!wasChanged)
			{
				if(identifier == null)
					wasChanged = value != null;
				else
					wasChanged = !identifier.equals(value);
			}
			
			identifier = value;
		}
		public int getWeekNumber()
		{
			return weekNumber;
		}
		public void setWeekNumber(int value)
		{
			if(weekNumber < 0)
				throw new CodingRuntimeException("Invalid week");
			
			if(!wasChanged)
				wasChanged = weekNumber != value;
			weekNumber = value;
		}
		public String getJourneyText()
		{
			return journeyText;
		}
		public void setJourneyText(String value)
		{
			if(value == null)
				throw new CodingRuntimeException("Invalid patient journey text");
			
			if(!wasChanged)
				wasChanged = !journeyText.equals(value);
			journeyText = value;
		}
		public String getProfileText()
		{
			return profileText;
		}
		public void setProfileText(String value)
		{
			if(value == null)
				throw new CodingRuntimeException("Invalid patient profile text");
			
			if(!wasChanged)
				wasChanged = !profileText.equals(value);		
			profileText = value;
		}
		public String getJourneyTooltip()
		{
			return journeyTooltip;
		}
		public void setJourneyTooltip(String value)
		{
			if(!wasChanged)
				wasChanged = !journeyTooltip.equals(value);
			journeyTooltip = value;
		}
		public String getProfileTooltip()
		{
			return profileTooltip;
		}
		public void setProfileTooltip(String value)
		{
			if(!wasChanged)
				wasChanged = !profileTooltip.equals(value);		
			profileTooltip = value;
		}	
		public Image getJourneyImage()
		{
			return journeyImage;
		}
		public void setJourneyImage(Image value)
		{
			if(!wasChanged)
			{
				if(journeyImage == null)
					wasChanged = value != null;
				else
					wasChanged = !journeyImage.equals(value);
			}
			
			journeyImage = value;
		}
		public Image getProfileImage()
		{
			return profileImage;
		}
		public void setProfileImage(Image value)
		{
			if(!wasChanged)
			{
				if(profileImage == null)
					wasChanged = value != null;
				else
					wasChanged = !profileImage.equals(value);
			}
			
			profileImage = value;
		}
		public Color getJourneyTextColor()
		{
			return journeyTextColor;
		}
		public void setJourneyTextColor(Color value)
		{
			if(!wasChanged)
			{
				if(journeyTextColor == null)
					wasChanged = value != null;
				else
					wasChanged = !journeyTextColor.equals(value);
			}
			
			journeyTextColor = value;
		}
		public Color getProfileTextColor()
		{
			return profileTextColor;
		}
		public void setProfileTextColor(Color value)
		{
			if(!wasChanged)
			{
				if(profileTextColor == null)
					wasChanged = value != null;
				else
					wasChanged = !profileTextColor.equals(value);
			}
			
			profileTextColor = value;
		}	
		public Color getJourneyBackColor()
		{
			return journeyBackColor;
		}
		public void setJourneyBackColor(Color value)
		{
			if(!wasChanged)
			{
				if(journeyBackColor == null)
					wasChanged = value != null;
				else
					wasChanged = !journeyBackColor.equals(value);
			}
			
			journeyBackColor = value;
		}
		public Color getProfileBackColor()
		{
			return profileBackColor;
		}
		public void setProfileBackColor(Color value)
		{
			if(!wasChanged)
			{
				if(profileBackColor == null)
					wasChanged = value != null;
				else
					wasChanged = !profileBackColor.equals(value);
			}
			
			profileBackColor = value;
		}
		public void setSelectable(boolean value)
		{
			if(!wasChanged)
				wasChanged = selectable != value;
			
			selectable = value;
		}
		public boolean isSelectable()
		{
			return selectable;
		}
		public boolean isJourneyWeekNumberVisible()
		{
			return showJourneyWeekNumber;
		}
		public boolean isProfileWeekNumberVisible()
		{
			return showProfileWeekNumber;
		}
		public void setJourneyWeekNumberVisible(boolean value)
		{
			if(!wasChanged)
				wasChanged = showJourneyWeekNumber != value;
			
			showJourneyWeekNumber = value;			
		}
		public void setProfileWeekNumberVisible(boolean value)
		{
			if(!wasChanged)
				wasChanged = showProfileWeekNumber != value;
			
			showProfileWeekNumber = value;				
		}	
		public boolean hasJourneyWeekNumberLabel()
		{
			return journeyWeekNumberLabel != null;
		}
		public String getJourneyWeekNumberLabel()
		{
			return journeyWeekNumberLabel == null ? String.valueOf(weekNumber) : journeyWeekNumberLabel;
		}
		public boolean hasProfileWeekNumberLabel()
		{
			return profileWeekNumberLabel != null;
		}
		public String getProfileWeekNumberLabel()
		{
			return profileWeekNumberLabel == null ? String.valueOf(weekNumber) : profileWeekNumberLabel;
		}
		public void setJourneyWeekNumberLabel(String value)
		{
			if(!wasChanged)
				wasChanged = journeyWeekNumberLabel != value;
			
			journeyWeekNumberLabel = value;
		}
		public void setProfileWeekNumberLabel(String value)
		{
			if(!wasChanged)
				wasChanged = profileWeekNumberLabel != value;
			
			profileWeekNumberLabel = value;
		}	
		public void markUnchanged()
		{
			wasChanged = false;
		}
		public boolean wasChanged()
		{
			return wasChanged;
		}		
	}
	
	public final class PatientJourneyMarker implements ims.framework.controls.PatientJourneyMarker
	{
		private static final long	serialVersionUID	= 1L;
		private boolean wasChanged = true;
		
		private int positionIndex;
		private int weekNumber;
		private Object identifier;
		private String text; 
		private String tooltip; 
		private Image image;
		private Color textColor;
		private Color backColor;
		
		public int getPositionIndex()
		{
			return positionIndex;
		}
		public PatientJourneyMarker(int weekNumber)
		{
			this(weekNumber, "");
		}
		public PatientJourneyMarker(int weekNumber, String text)
		{
			this(weekNumber, null, text, null); 
		}
		public PatientJourneyMarker(int weekNumber, Object identifier, String text, Image image)
		{
			this.weekNumber = weekNumber;
			this.identifier = identifier;			
			this.text = text;
			this.image = image;
		}
			
		public Object getIdentifier()
		{
			return identifier;
		}
		public void setIdentifier(Object value)
		{
			if(!wasChanged)
			{
				if(identifier == null)
					wasChanged = value != null;
				else
					wasChanged = !identifier.equals(value);
			}
			
			identifier = value;
		}
		public int getWeekNumber()
		{
			return weekNumber;
		}
		public void setWeekNumber(int value)
		{
			if(weekNumber < 0)
				throw new CodingRuntimeException("Invalid week");
			
			if(!wasChanged)
				wasChanged = weekNumber != value;
			weekNumber = value;
		}
		public String getText()
		{
			return text;
		}
		public void setText(String value)
		{
			if(value == null)
				throw new CodingRuntimeException("Invalid patient journey marker text");
			
			if(!wasChanged)
				wasChanged = !text.equals(value);
			text = value;
		}
		public String getTooltip()
		{
			return tooltip;
		}
		public void setTooltip(String value)
		{
			if(!wasChanged)
				wasChanged = !tooltip.equals(value);
			tooltip = value;
		}
		public Image getImage()
		{
			return image;
		}
		public void setImage(Image value)
		{
			if(!wasChanged)
			{
				if(image == null)
					wasChanged = value != null;
				else
					wasChanged = !image.equals(value);
			}
			
			image = value;
		}
		public Color getTextColor()
		{
			return textColor;
		}
		public void setTextColor(Color value)
		{
			if(!wasChanged)
			{
				if(textColor == null)
					wasChanged = value != null;
				else
					wasChanged = !textColor.equals(value);
			}
			
			textColor = value;
		}
		public Color getBackColor()
		{
			return backColor;
		}
		public void setBackColor(Color value)
		{
			if(!wasChanged)
			{
				if(backColor == null)
					wasChanged = value != null;
				else
					wasChanged = !backColor.equals(value);
			}
			
			backColor = value;
		}
		public void markUnchanged()
		{
			wasChanged = false;
		}
		public boolean wasChanged()
		{
			return wasChanged;
		}
	}

	public final class PatientJourneyClock implements ims.framework.controls.PatientJourneyClock	
	{
		private static final long serialVersionUID = 1L;
		private boolean wasChanged = true;
		private Object identifier;
		private int startWeekNumber;
		private int endWeekNumber;		
		private String tooltip;
		private boolean isEnded;
		
		public PatientJourneyClock(int startWeekNumber, int endWeekNumber)
		{
			this.startWeekNumber = startWeekNumber;
			this.endWeekNumber = endWeekNumber;			
		}
		
		public int getEndWeekNumber()
		{
			return endWeekNumber;
		}
		public Object getIdentifier()
		{
			return identifier;
		}
		public int getStartWeekNumber()
		{
			return startWeekNumber;
		}
		public String getTooltip()
		{
			return tooltip;
		}
		public void setEndWeekNumber(int value)
		{
			endWeekNumber = value;	
		}
		public void setIdentifier(Object value)
		{
			identifier = value;
		}
		public void setStartWeekNumber(int value)
		{
			startWeekNumber = value;
		}
		public void setTooltip(String value)
		{
			tooltip = value;
		}
		public void setIsEnded(boolean value)
		{
			isEnded = value;
		}
		public boolean isEnded()
		{
			return isEnded;
		}
		public void markUnchanged()
		{
			wasChanged = false;
		}
		public boolean wasChanged()
		{
			return wasChanged;
		}
	}
	
	public class PatientJournerClocks implements ims.framework.controls.PatientJourneyClocks
	{
		private static final long serialVersionUID = 1L;
		
		private boolean wasChanged = false;
		private ArrayList<PatientJourneyClock> list = new ArrayList<PatientJourneyClock>();
		
		public void clear()
		{
			list.clear();
		}
		public ims.framework.controls.PatientJourneyClock get(int index)
		{
			return list.get(index);
		}
		public ims.framework.controls.PatientJourneyClock getByIdentifier(Object identifier)
		{
			if(identifier == null)
				return null;
			
			for (PatientJourneyClock item : list)
			{
				if(item.getIdentifier() != null && item.getIdentifier().equals(identifier))
					return item;				
			}
			
			return null;
		}		
		public int indexOf(ims.framework.controls.PatientJourneyClock clock)
		{
			return list.indexOf(clock);
		}
		public ims.framework.controls.PatientJourneyClock newClock(int startWeekNumber, int endWeekNumber)
		{
			return newClock(startWeekNumber, endWeekNumber, false);
		}
		public ims.framework.controls.PatientJourneyClock newClock(int startWeekNumber, int endWeekNumber, boolean isEnded)
		{
			PatientJourneyClock clock = new PatientJourneyClock(startWeekNumber, endWeekNumber);
			clock.setIsEnded(isEnded);
			list.add(clock);
			return clock;
		}
		public boolean remove(ims.framework.controls.PatientJourneyClock clock)
		{
			return list.remove(clock);
		}
		public int size()
		{
			return list.size();
		}
		public void markUnchanged()
		{
			wasChanged = false;
			for (PatientJourneyClock item : list)
			{
				item.markUnchanged();
			}			
		}
		public boolean wasChanged()
		{
			if(wasChanged)
				return true;
			
			for (PatientJourneyClock item : list)
			{
				if(item.wasChanged())
					return true;
			}
			
			return false;
		}	
	}
	
	public ArrayList<PatientJourneyItem> getAllEntries()
	{		
		ArrayList<PatientJourneyItem> list = new ArrayList<PatientJourneyItem>();
		
		for(int x = 0; x < markers.size(); x++)
		{
			list.add(markers.get(x));
		}
		for(int x = 0; x < entries.size(); x++)
		{
			list.add(entries.get(x));
		}		
		
		return sortItemsByPositionIndex(list);		
	}
	private ArrayList<PatientJourneyItem> sortItemsByPositionIndex(ArrayList<PatientJourneyItem> list)
	{
		for(int x = 0; x < list.size(); x++)
		{
			for(int y = 0; y < list.size(); y++)
			{
				if(x != y)
				{
					PatientJourneyItem item1 = list.get(x);
					PatientJourneyItem item2 = list.get(y);
					
					if(item1.getPositionIndex() < item2.getPositionIndex())
					{
						list.set(x, item2);
						list.set(y, item1);
					}
				}
			}
		}
		
		return list;
	}
	public class IDGenerator implements IIDGenerator
	{
		private static final long serialVersionUID = 1L;
		
		private int id = 1;		
		
		public int nextId()
		{
			return id++;
		}
		public void reset()
		{
			id = 1;
		}
	}

	public void scrollToSelection()
	{
		if(selection > 0)
		{
			scrollSelection = getEntries().get(selection).getPositionIndex();
		}
		else			
		{
			scrollSelection = -1;
		}
	}
	public void scrollToCurrentWeek()
	{		
		scrollSelection = getCurrentWeekNumber() * 1000;
	}
	public int getScrollToRow()
	{
		return scrollSelection;
	}
	public void resetScrollToRow()
	{
		scrollSelection = -1;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date date)
	{
		startDate = date;		
	}
	public boolean isCurrentWeek(ims.framework.controls.PatientJourneyMarker entry)
	{
		return entry.getWeekNumber() == getCurrentWeekNumber();			
	}
	public boolean isCurrentWeek(ims.framework.controls.PatientJourneyEntry entry)
	{
		return entry.getWeekNumber() == getCurrentWeekNumber();			
	}
	private int getCurrentWeekNumber()
	{
		if(startDate == null)
			throw new ims.framework.exceptions.CodingRuntimeException("Invalid start date");
		
		Date targetDate = new Date();
		
		if(targetDate == null)
			throw new ims.framework.exceptions.CodingRuntimeException("Invalid target date");

		long curr = targetDate.getDate().getTime();
		long start = startDate.getDate().getTime();
		long noDays = (curr - start) / (1000 * 60 * 60 * 24);

		return ((int)noDays / 7) + 1;
	}	
}
