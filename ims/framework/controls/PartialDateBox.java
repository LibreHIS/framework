package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.PartialDate;

abstract public class PartialDateBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean required)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.required = required;
	}
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	
	public abstract PartialDate getValue();
	public abstract void setValue(PartialDate value);
	public abstract PartialDate getMinValue();
	public abstract void setMinValue(PartialDate value);
	public abstract PartialDate getMaxValue();
	public abstract void setMaxValue(PartialDate value);
	abstract public void setRequired(boolean value);
	abstract public boolean isRequired();
	
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}	
	protected ValueChanged valueChangedDelegate = null;
	protected boolean required;
}
