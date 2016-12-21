package ims.framework.controls;

import java.io.Serializable;

import ims.framework.IControlComparable;
import ims.framework.delegates.ValueChanged;
 
public abstract class RadioButtonBridge implements IControlComparable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected void setContext(RadioButton control)
	{
		this.control = control;
		this.control.setValueChangedEvent(new ValueChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
			{
				if (RadioButtonBridge.this.valueChangedDelegate != null)
					RadioButtonBridge.this.valueChangedDelegate.handle();
			}
		});
	}
	public void setFocus()
	{
		this.control.setFocus();
	}
	public void free()
	{
		this.control = null;
		this.valueChangedDelegate = null;
	}
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	public int getID()
	{
		return this.control.getID();
	}

	protected ValueChanged valueChangedDelegate = null;
	protected RadioButton control;
}
