package ims.framework.controls;

public interface PatientJourneyClock 
{
	Object getIdentifier();
	void setIdentifier(Object value);
	void setStartWeekNumber(int value);
	int getStartWeekNumber();
	void setEndWeekNumber(int value);
	int getEndWeekNumber();	
	void setIsEnded(boolean value);
	boolean isEnded();
	String getTooltip();	
	void setTooltip(String value);
}
