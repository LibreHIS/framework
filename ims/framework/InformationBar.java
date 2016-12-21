package ims.framework;

import ims.framework.utils.Color;

public abstract class InformationBar
{
	protected String text;
	protected Color textColor;
	
	public String getText()
	{
		return text;
	}
	public void setText(String value)
	{
		text = value;
	}
	public Color getTextColor()
	{
		return textColor;
	}
	public void setTextColor(Color value)
	{
		textColor = value;
	}
	
	public abstract void render(StringBuffer sb); 
}
