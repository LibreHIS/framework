package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Time;

public abstract class TimeControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, boolean required, String tooltip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.canBeEmpty = canBeEmpty;
		this.required = required;
		this.tooltip = tooltip;
	}
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}	

 	public abstract Time getValue();
 	public abstract void setValue(Time value);
 	abstract public void setRequired(boolean value);
 	abstract public boolean isRequired();
 	public abstract void setTooltip(String value);
	
    public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate;
    }
 	
    protected String tooltip;
	protected boolean canBeEmpty;
    protected ValueChanged valueChangedDelegate = null;
    protected boolean required;
}
