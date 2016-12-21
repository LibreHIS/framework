package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public abstract class CheckBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, String tooltip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.text = text;
		this.tooltip = tooltip;		
	}

	abstract public boolean getValue();
	abstract public void setValue(Boolean value);
	public abstract void setTooltip(String value);
	
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}

	protected void free() // free resources
	{
		super.free();
		
		this.text = null;
		this.tooltip = null;
		this.valueChangedDelegate = null;
	}    

	protected String text;
	protected String tooltip;
	protected ValueChanged valueChangedDelegate = null;	
}
