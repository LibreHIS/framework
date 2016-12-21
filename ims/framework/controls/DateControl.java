package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Date;

public abstract class DateControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, ims.framework.Menu menu, boolean required, String tooltip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.canBeEmpty = canBeEmpty;
		this.required = required;
		this.tooltip = tooltip;
	}
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}
	public void setContextMenu(ims.framework.Menu menu)
	{
		this.menu = menu;
	}
	public ims.framework.Menu getContextMenu()
	{
		return this.menu;
	}
	
 	public abstract Date getValue();
 	public abstract void setValue(Date value);
 	public abstract Date getMinValue();
 	public abstract void setMinValue(Date value);
 	public abstract Date getMaxValue();
 	public abstract void setMaxValue(Date value); 	
 	public abstract void setNoFutureDates();
	public abstract void setRequired(boolean value);
	public abstract boolean isRequired();
	public abstract void setTooltip(String value);
	
    public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate;
    }
 	
    protected String tooltip;
	protected boolean canBeEmpty;
    protected ValueChanged valueChangedDelegate = null;
    protected boolean required = false;
}
