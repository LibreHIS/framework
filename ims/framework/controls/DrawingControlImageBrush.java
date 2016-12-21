package ims.framework.controls;

import java.io.Serializable;

import ims.framework.utils.Image;

public class DrawingControlImageBrush implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlImageBrush(int id, String caption, Image image, boolean multipleMarkings, String tooltip)
	{
		this.id = id;
		this.caption = caption;
		this.image = image;
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
	public Image getImage()
	{
		return this.image;
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
		
		if(value instanceof DrawingControlImageBrush)
			return ((DrawingControlImageBrush)value).id == this.id;
		
		return false;
	}
	public int hashCode()
	{
		return super.hashCode();
	}
	
	private int id;
	private String caption;
	private Image image;
	private boolean multipleMarkings = true;
	private String tooltip;
}

