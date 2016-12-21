package ims.framework.controls;

import java.io.Serializable;

import ims.framework.utils.Color;

public class DrawingControlColorBrush implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlColorBrush(int id, String caption, Color color, boolean multipleMarkings, String tooltip)
	{
		this.id = id;
		this.caption = caption;
		this.color = color;
		this.multipleMarkings = multipleMarkings;
		this.tooltip = tooltip;
	}
	
	public int getID()
	{
		return this.id;
	}
	public String getCaption()
	{
		return this.caption;
	}
	public Color getColor()
	{
		return this.color;
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public boolean allowsMultipleMarkings()
	{
		return this.multipleMarkings;
	}	
	
	public boolean equals(Object value)
	{
		if(value == null)
			return false;
		
		if(value instanceof DrawingControlColorBrush)
			return ((DrawingControlColorBrush)value).id == this.id;
		
		return false;
	}
	public int hashCode()
	{
		return super.hashCode();
	}
	
	private int id;
	private String caption;
	private Color color;
	private boolean multipleMarkings = true;
	private String tooltip;
}