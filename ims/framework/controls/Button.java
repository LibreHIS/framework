package ims.framework.controls;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.delegates.Click;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

public abstract class Button extends Control
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String toolTip, boolean isDefaultButton, boolean causesValidation, Menu menu, Color backgroundColor, Color textColor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.tooltip = toolTip;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.isDefaultButton = isDefaultButton; 
		this.causesValidation = causesValidation;
	}
    public void setClickEvent(Click delegate)
    {
        this.clickDelegate = delegate;        
    }
    public abstract void setTooltip(String value);
    public abstract void setBackgroundColor(Color value);
    public abstract void clearBackgroundColor();
    public abstract void setTextColor(Color value);
    public abstract void clearTextColor();
    public abstract void setImage(Image image);
    public abstract void setUploadButton(boolean value);
    public abstract boolean isUploadButton();
    public abstract void setPostbackRequirePdsAuthentication(boolean value);
    public final void setAsDefaultButton()
    {
    	if(this.defaultControlDelegate != null)
			this.defaultControlDelegate.handle(this);
    }
	protected void free() // free resources
	{
		super.free();
		
		this.tooltip = null;
		this.backgroundColor = null;
		this.textColor = null;
		this.clickDelegate = null;
	}    
    protected Click clickDelegate = null;
    abstract public void setText(String text);    
    abstract public String getText();
    protected String tooltip;
    protected Color backgroundColor;
    protected Color textColor;
    protected boolean isDefaultButton = false;
    protected boolean causesValidation = true;
    protected boolean postbackRequirePdsAuthentication = false;
}
