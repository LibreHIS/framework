package ims.framework.controls;

import java.io.Serializable;

import ims.framework.IEnhancedItem;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

public class AnswerBoxOptionImpl implements AnswerBoxOption, IEnhancedItem, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public AnswerBoxOptionImpl(int index, Image image, String text)
	{
		this(index, image, text, null);		        
	}
    public AnswerBoxOptionImpl(int index, Image image, String text, Color textColor)
    {
        this.id = index;
        this.image = image;
        this.text = text;               
        this.textColor = textColor;
    }
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof AnswerBoxOptionImpl)
			return this.id == ((AnswerBoxOptionImpl)obj).id;
		return false;
	}
	public int hashCode()
	{
		return super.hashCode();
	}
	public int getID()
	{
		return this.id;
	}
	public String getText()
	{
		return this.text;
	}
	public String getIItemText()
	{
		return this.text;
	}
	public Image getImage()
	{
		return this.image;
	}
	public Image getIItemImage()
	{
		return this.image;
	}
    public Color getTextColor() 
    {
        return this.textColor;
    }
    public Color getIItemTextColor() 
    {
        return this.textColor;
    }
	private int id;
	private Image image;
	private String text;
    private Color textColor;    
}
