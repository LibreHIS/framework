package ims.framework.controls;

import ims.framework.Control;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public abstract class PortalDesigner extends Control
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String caption, String toolTip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.caption = caption;
		this.tooltip = toolTip;
	}
    
	public abstract void setCaption(String value);
	public abstract String getCaption();
	public abstract void setTooltip(String value);
	public abstract String getTooltip();
    protected void free() // free resources
	{
		super.free();
	
		this.caption = null;
		this.tooltip = null;		
	}    
    protected String caption;
    protected String tooltip;    
}
