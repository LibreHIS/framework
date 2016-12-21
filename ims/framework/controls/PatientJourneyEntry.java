package ims.framework.controls;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

public interface PatientJourneyEntry extends PatientJourneyItem
{
	Object getIdentifier();
	void setIdentifier(Object value);
	void setWeekNumber(int value);
	int getWeekNumber();
	public String getJourneyText();
	void setJourneyText(String value);
	String getProfileText();
	void setProfileText(String value);
	String getJourneyTooltip();
	void setJourneyTooltip(String value);
	String getProfileTooltip();
	void setProfileTooltip(String value);
	Image getJourneyImage();
	void setJourneyImage(Image value);
	Image getProfileImage();
	void setProfileImage(Image value);
	Color getJourneyTextColor();
	void setJourneyTextColor(Color value);
	Color getProfileTextColor();
	void setProfileTextColor(Color value);
	Color getJourneyBackColor();
	void setJourneyBackColor(Color value);
	Color getProfileBackColor();
	void setProfileBackColor(Color value);
	void setSelectable(boolean value);
	boolean isSelectable();	
	void setJourneyWeekNumberVisible(boolean value);
	boolean isJourneyWeekNumberVisible();
	void setProfileWeekNumberVisible(boolean value);
	boolean isProfileWeekNumberVisible();
	void setJourneyWeekNumberLabel(String value);
	String getJourneyWeekNumberLabel();
	boolean hasJourneyWeekNumberLabel();
	void setProfileWeekNumberLabel(String value);	
	String getProfileWeekNumberLabel();
	boolean hasProfileWeekNumberLabel();	
}
