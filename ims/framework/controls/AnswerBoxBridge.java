package ims.framework.controls;

import java.io.Serializable;

import ims.framework.IControlComparable;
import ims.framework.delegates.AnswerBoxValueSet;
import ims.framework.delegates.ValueChanged;

abstract public class AnswerBoxBridge implements IControlComparable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public void setFocus()
	{
		this.control.setFocus();
	}
	public boolean isEnabled()
	{
		return this.control.isEnabled();
	}
	public void setEnabled(boolean value)
	{
		this.control.setEnabled(value);
	}
	public boolean getVisible()
	{
		return this.control.isVisible();
	}
	public void setVisible(boolean value)
	{
		this.control.setVisible(value);
	}
	public void setRequired(boolean value)
	{
		this.control.setRequired(value);
	}
	public boolean isRequired()
	{
		return this.control.isRequired();
	}
	
	protected void setContext(AnswerBox control)
	{
		this.control = control;
		this.control.setValueChangedEvent(new ValueChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
			{
				if (AnswerBoxBridge.this.valueChangedDelegate != null)
					AnswerBoxBridge.this.valueChangedDelegate.handle();
			}
		});
		this.control.setValueSetEvent(new AnswerBoxValueSet()
		{
			public void handle(Object value)
			{
				if (AnswerBoxBridge.this.valueSetDelegate != null)
					AnswerBoxBridge.this.valueSetDelegate.handle(value);
			}
		});
	} 
	public void free()
	{
		this.control = null;
		this.valueChangedDelegate = null;
		this.valueSetDelegate = null;
	}
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}	
	public void setValueSetEvent(AnswerBoxValueSet delegate)
	{
		this.valueSetDelegate = delegate;
	}	
	public int getID()
	{
		return this.control.getID();
	}

	protected ValueChanged valueChangedDelegate = null;
	private AnswerBoxValueSet valueSetDelegate = null;
	protected AnswerBox control;
}
