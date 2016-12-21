package ims.framework.controls;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.framework.IEnhancedItem;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public class DynamicGridCellItem implements IEnhancedItem, IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public DynamicGridCellItem(Object value, Image image, Color textColor, boolean checked)
	{
		this(value, null, image, textColor, checked, null);
	}
	public DynamicGridCellItem(Object value, String text, Image image, Color textColor, boolean checked)
	{
		this(value, text, image, textColor, checked, null);
	}
    public DynamicGridCellItem(Object value, String text, Image image, Color textColor, boolean checked, String tooltip)
    {
        this.value = value;
        this.text = text;
        this.image = image;
        this.textColor = textColor;
        this.checked = checked;
        this.tooltip = tooltip;
    }
    public Object getValue()
    {
        return this.value;
    }
    public void setValue(Object value)
    {
    	if(!this.wasChanged)
    	{
    		if(this.value == null)
    			this.wasChanged = value != null;
    		else
    			this.wasChanged = !this.value.equals(value);
    	}
    	
        this.value = value;
    }
    public Image getImage()
    {
        return this.image;
    }
    public Image getIItemImage()
    {
        return this.image;
    }
    public void setImage(Image image)
    {
    	if(!this.wasChanged)
    	{
    		if(this.image == null)
    			this.wasChanged = image != null;
    		else
    			this.wasChanged = !this.image.equals(image);
    	}
    	
        this.image = image;
    }
    public void setTextColor(Color color)
    {
    	if(!this.wasChanged)
    	{
    		if(this.textColor == null)
    			this.wasChanged = color != null;
    		else
    			this.wasChanged = !this.textColor.equals(color);
    	}
    	
    	this.textColor = color;
    }    
    public Color getTextColor()
    {
        return this.textColor;
    }
    public void setMarkerColor(Color color)
    {
    	if(!this.wasChanged)
    	{
    		if(this.markerColor == null)
    			this.wasChanged = color != null;
    		else
    			this.wasChanged = !this.markerColor.equals(color);
    	}
    	
    	this.markerColor = color;
    }
    public Color getMarkerColor()
    {
        return this.markerColor;
    }
    public void setChecked(boolean value)
    {
    	if(!this.wasChanged)
    		this.wasChanged = this.checked != value;
    	
    	this.checked = value;
    }
    public boolean isChecked()
    {
    	return this.checked;    	
    }
    public void setIdentifier(Object value)
    {
    	if(value == null)
            throw new RuntimeException("Invalid cell item identifier");
    	
    	this.identifier = value;
    }
    public Object getIdentifier()
    {
    	return this.identifier;
    }
    public Color getIItemTextColor()
    {
        return this.textColor;
    }
    public boolean equals(Object obj)
    {
        if(obj != null && obj instanceof DynamicGridCellItem && this.value != null)
            return this.value.equals(((DynamicGridCellItem)obj).value);
        return false;
    }
    public int hashCode()
	{
		return super.hashCode();
	}
    public boolean valueEquals(Object value)
    {
        if(this.value != null)
            return this.value.equals(value);
        return false;
    }    
    public String toString()
    {
        if(this.value == null)
            return "";
        return this.value.toString();
    }
    public String getIItemText()
    {
    	return this.text != null && this.text.trim().length() > 0 ? this.text : toString();
    }
    public String getTooltip()
    {
    	return this.tooltip;
    }
    public void setTooltip(String value)
    {
    	if(!this.wasChanged)
    	{
    		if(this.tooltip == null)
    			this.wasChanged = value != null;
    		else
    			this.wasChanged = !this.tooltip.equals(value);
    	}
    	
    	this.tooltip = value;
    }
    public String getText()
    { 
    	return this.text;
    }
    public void setText(String value)
    {
    	if(!this.wasChanged)
    	{
    		if(this.text == null)
    			this.wasChanged = value != null;
    		else
    			this.wasChanged = !this.text.equals(value);
    	}
    	
    	this.text = value;
    }
    public boolean wasChanged()
	{
		return this.wasChanged;
	}
	public void markUnchanged()
	{
		this.wasChanged = false;
	}
        
    private Object value;
    private String text;
    private Image image;
    private Color textColor;	  
    private Color markerColor; 
    private boolean checked = false;
    private String tooltip;
    private Object identifier;
    private boolean wasChanged = true;
}
