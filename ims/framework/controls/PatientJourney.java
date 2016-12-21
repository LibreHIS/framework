package ims.framework.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.delegates.PatientJourneySelectionChanged;
import ims.framework.utils.Date;

public abstract class PatientJourney extends Control implements IVisualControl
{	
	private static final long serialVersionUID = 1L;
	
	protected PatientJourneySelectionChanged selectionChangedDelegate = null;
	
	protected void free()
	{
		super.free();		
		
		this.selectionChangedDelegate = null;
	}
	public void setSelectionChangedEvent(PatientJourneySelectionChanged delegate)
	{
		this.selectionChangedDelegate = delegate;
	}
	
	public abstract void clear();
	public abstract void setStartDate(Date startDate);
	public abstract PatientJourneyEntries getEntries();
	public abstract PatientJourneyMarkers getMarkers();
	public abstract PatientJourneyClocks getClocks();
	public abstract String getJourneyHeaderText();
	public abstract void setJourneyHeaderText(String value);
	public abstract String getProfileHeaderText();
	public abstract void setProfileHeaderText(String value);
	public abstract int getSplitterPercentage();
	public abstract void setSplitterPercentage(int value);
	public abstract PatientJourneyEntry getSelectedEntry();
	public abstract void setSelectedEntry(PatientJourneyEntry entry);	
	public abstract void scrollToSelection();
	public abstract void scrollToCurrentWeek();	
}
