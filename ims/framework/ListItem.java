package ims.framework;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public class ListItem implements IEnhancedItem, IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	
    public ListItem()
    {        
    }
    public ListItem(Object value, String text)
    {
        this(value, text, null, null, false, true, null);
    }
    public ListItem(Object value, String text, String tooltip)
    {
        this(value, text, null, null, false, true, tooltip);
    }
    public ListItem(Object value, String text, boolean checked)
    {
        this(value, text, null, null, checked, true, null);
    }
    public ListItem(Object value, String text, boolean checked, String tooltip)
    {
        this(value, text, null, null, checked, true, tooltip);
    }
    public ListItem(Object value, String text, boolean checked, boolean enabled)
    {
        this(value, text, null, null, checked, enabled, null);
    }
    public ListItem(Object value, String text, boolean checked, boolean enabled, String tooltip)
    {
        this(value, text, null, null, checked, enabled, tooltip);
    }
    public ListItem(Object value, String text, Image image)
    {
        this(value, text, image, null, false, true, null);
    }
    public ListItem(Object value, String text, Image image, String tooltip)
    {
        this(value, text, image, null, false, true, tooltip);
    }
    public ListItem(Object value, String text, Image image, boolean checked)
    {
        this(value, text, image, null, checked, true, null);
    }
    public ListItem(Object value, String text, Image image, boolean checked, String tooltip)
    {
        this(value, text, image, null, checked, true, tooltip);
    }
    public ListItem(Object value, String text, Image image, boolean checked, boolean enabled)
    {
        this(value, text, image, null, checked, enabled, null);
    }
    public ListItem(Object value, String text, Image image, boolean checked, boolean enabled, String tooltip)
    {
        this(value, text, image, null, checked, enabled, tooltip);
    }
    public ListItem(Object value, String text, Color textColor)
    {
        this(value, text, null, textColor, false, true, null);
    }
    public ListItem(Object value, String text, Color textColor, String tooltip)
    {
        this(value, text, null, textColor, false, true, tooltip);
    }
    public ListItem(Object value, String text, Color textColor, boolean checked)
    {
        this(value, text, null, textColor, checked, true, null);
    }
    public ListItem(Object value, String text, Color textColor, boolean checked, String tooltip)
    {
        this(value, text, null, textColor, checked, true, tooltip);
    }
    public ListItem(Object value, String text, Color textColor, boolean checked, boolean enabled)
    {
        this(value, text, null, textColor, checked, enabled, null);
    }
    public ListItem(Object value, String text, Color textColor, boolean checked, boolean enabled, String tooltip)
    {
        this(value, text, null, textColor, checked, enabled, tooltip);
    }
    public ListItem(Object value, String text, Image image, Color textColor)
    {
    	this(value, text, image, textColor, false, true, null);
    }
    public ListItem(Object value, String text, Image image, Color textColor, String tooltip)
    {
    	this(value, text, image, textColor, false, true, tooltip);
    }
    public ListItem(Object value, String text, Image image, Color textColor, boolean checked)
    {
    	this(value, text, image, textColor, checked, true, null);
    }
    public ListItem(Object value, String text, Image image, Color textColor, boolean checked, String tooltip)
    {
    	this(value, text, image, textColor, checked, true, tooltip);
    }
    public ListItem(Object value, String text, Image image, Color textColor, boolean checked, boolean enabled)
    {
    	this(value, text, image, textColor, checked, enabled, null);
    }
    public ListItem(Object value, String text, Image image, Color textColor, boolean checked, boolean enabled, String tooltip)
    {
        this.value = value;
        this.text = text;
        this.image = image;
        this.textColor = textColor;
        this.checked = checked;
        this.enabled = enabled;
        this.tooltip = tooltip;
    }
    public Object getValue()
    {
        return this.value;
    }
    public void setValue(Object value)
    {
    	if(!this.dataWasChanged)
    	{
    		if(this.value == null)
    			this.dataWasChanged = value != null;
    		else
    			this.dataWasChanged = !this.value.equals(value);    			
    	}
    	
        this.value = value;
    }
    public String getText()
    {
        return this.text;
    }
    public void setText(String value)
    {
    	if(!this.dataWasChanged)
    	{
    		if(this.text == null)
    			this.dataWasChanged = value != null;
    		else
    			this.dataWasChanged = !this.text.equals(value);
    	}
    	
        this.text = value;
    }
    public Image getImage()
    {
        return this.image;
    }
    public void setImage(Image value)
    {
    	if(!this.dataWasChanged)
    	{
    		if(this.image == null)
    			this.dataWasChanged = value != null;
    		else
    			this.dataWasChanged = !this.image.equals(value);    			
    	}
    	
        this.image = value;
    }
    public Color getTextColor()
    {
        return this.textColor;
    }
    public void setTextColor(Color value)
    {
    	if(!this.dataWasChanged)
    	{
    		if(this.textColor == null)
    			this.dataWasChanged = value != null;
    		else
    			this.dataWasChanged = !this.textColor.equals(value);    			
    	}
    	
        this.textColor = value;
    }
    public boolean isChecked()
    {
        return this.checked;
    }
    public void setChecked(boolean value)
    {
		if(this.checked != value)
		{
			this.dataWasChanged = true;
			this.checked = value;
		}
	}
    public boolean isEnabled()
    {
        return this.enabled;
    }
    public void setEnabled(boolean value)
    {
    	if(this.enabled != value)
		{
			this.dataWasChanged = true;
			this.enabled = value;
		}        
    }
    public void setTooltip(String value)
    {
    	if(!this.dataWasChanged)
    	{
    		if(this.tooltip == null)
    			this.dataWasChanged = value != null;
    		else
    			this.dataWasChanged = !this.tooltip.equals(value);    			
    	}
    	
        this.tooltip = value;
    }
    public String getTooltip()
    {
    	return this.tooltip == null ? "" : this.tooltip;
    }
    public String toString()
    {
        return this.text == null ? "" : this.text;
    }
    public String getIItemText()
    {
    	return toString();
    }
    public Color getIItemTextColor()
    {
    	return this.textColor;
    }
    public Image getIItemImage()
    {
    	return this.image;
    }
    public boolean equals(Object obj)
    {
        if(obj != null && obj instanceof ListItem && this.value != null)
            return this.value.equals(((ListItem)obj).value);
        return false;
    }
    public int hashCode()
    {
    	return value.hashCode();
    }
    public boolean wasChanged() 
    {
		return this.dataWasChanged;
	}
	public void markUnchanged() 
	{
		this.dataWasChanged = false;
	}
    
	private boolean dataWasChanged = true;
    private Object value;
    private String text;
    private Image image;
    private Color textColor;
    private boolean checked;
    private boolean enabled;
    private String tooltip;
}
