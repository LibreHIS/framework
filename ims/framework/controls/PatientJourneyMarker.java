package ims.framework.controls;

public interface PatientJourneyMarker extends PatientJourneyItem
{
	void setWeekNumber(int value);
	int getWeekNumber();
	public String getText();
	void setText(String value);		
}
