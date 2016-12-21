package ims.framework.controls;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.framework.utils.Image;

public final class ActionButton implements Serializable, IModifiable
{	
	private static final long serialVersionUID = 1L;
	protected int id;
	protected String text;
	protected String description;
	protected Image image;
	private boolean wasChanged = true;
	
	public ActionButton()
	{
		this(0, "<None>");
	}
	public ActionButton(int id, String text)
	{
		this(id, text, text);
	}
	public ActionButton(int id, String text, String description)
	{
		this(id, text, description, null);
	}
	public ActionButton(int id, String text, String description, Image image)
	{
		this.id = id;
		this.text = text;
		this.description = description;
		this.image = image;
	}
	
	public int getID()
	{
		return id;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String value)
	{
		text = value;
	}
	public Image getImage()
	{
		return image;
	}
	public void setImage(Image value)
	{
		image = value;
	}			
	public void markUnchanged()
	{
		wasChanged = false;
	}
	public boolean wasChanged()
	{
		return wasChanged;
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof ActionButton)
			return ((ActionButton)obj).getID() == getID();
		return false;
	}
}
