package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.DecimalRange;

abstract public class DecimalRangeBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	public abstract DecimalRange getValue();
	public abstract void setValue(DecimalRange value);
	abstract public void setRequired(boolean value);
	abstract public boolean isRequired();
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean required)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.required = required;
	}
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}	
	
	protected ValueChanged valueChangedDelegate = null;
	protected boolean required;
}
