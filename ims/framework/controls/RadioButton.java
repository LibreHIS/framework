package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;

public abstract class RadioButton extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}	

	abstract public int getValue();
	abstract public void setValue(int value);
	abstract public void setEnabled(int option, boolean value);	
	abstract public boolean isEnabled(int option);
	abstract public void setVisible(int option, boolean value);	
	abstract public boolean isVisible(int option);	
	public abstract void addButton(int index, int x, int y, int width, String text, int tabIndex);
	public abstract void setText(int index, String text);

	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}

	protected ValueChanged valueChangedDelegate = null;
	
}
