package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Color;

public abstract class IntBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, boolean allowSign, boolean required, int maxLength)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.canBeEmpty = canBeEmpty;
		this.allowSign = allowSign;
		this.required = required;
		this.maxLength = maxLength; 
	}
	protected void free()
	{
		super.free();
		
		this.tooltip = null;
		this.valueChangedDelegate = null;
	}
	public abstract Integer getValue();
	public abstract void setValue(Integer value);
	public abstract void setTextColor(Color color);
	public abstract void setTooltip(String value);
	abstract public void setRequired(boolean value);
	abstract public boolean isRequired();

	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}

	protected boolean canBeEmpty;
	protected ValueChanged valueChangedDelegate = null;
	protected boolean allowSign;
	protected int maxLength = 9;
	protected String tooltip;
	protected boolean required;
}
