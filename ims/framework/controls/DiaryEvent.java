package ims.framework.controls;

import ims.base.interfaces.IModifiable;
import ims.framework.utils.Color;
import ims.framework.utils.DateTime;
import ims.framework.utils.Image;

import java.io.Serializable;

public interface DiaryEvent extends Serializable, IModifiable
{	
	Object getIdentifier();
	void setIdentifier(Object identifier);
	DateTime getStartDateTime();
	void setStartDateTime(DateTime startDateTime);
	String getText();
	void setText(String text);
	String getTooltip();
	void setTooltip(String tooltip);
	Image getImage();
	void setImage(Image image);
	Color getTextColor();
	void setTextColor(Color textColor);
	Color getBackColor();
	void setBackColor(Color backColor);	
}
