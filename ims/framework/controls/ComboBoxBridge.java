package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;

import ims.framework.IControlComparable;
import ims.framework.delegates.ValueChanged;
import ims.framework.delegates.ComboBoxValueSet;
import ims.framework.delegates.ComboBoxSearch;

public abstract class ComboBoxBridge implements IControlComparable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected void setContext(ComboBox control)
	{
		this.control = control;
		this.control.setValueChangedEvent(new ValueChanged()
		{			
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
			{
				if (ComboBoxBridge.this.valueChangedDelegate != null)
					ComboBoxBridge.this.valueChangedDelegate.handle();
			}
		});
		this.control.setValueSetEvent(new ComboBoxValueSet()
		{
			private static final long serialVersionUID = 1L;

			public void handle(Object value)  //throws ims.framework.exceptions.PresentationLogicException
			{
				if (ComboBoxBridge.this.valueSetDelegate != null)
					ComboBoxBridge.this.valueSetDelegate.handle(value);
				}
		});
		this.control.setSearchEvent(new ComboBoxSearch()
		{
			private static final long serialVersionUID = 1L;

			public void handle(String value) throws ims.framework.exceptions.PresentationLogicException
			{
				if (ComboBoxBridge.this.searchDelegate != null)
					ComboBoxBridge.this.searchDelegate.handle(value);
				}
		});
	}
	public void free()
	{
		this.control = null;
		this.valueChangedDelegate = null;
		this.valueSetDelegate = null;
		this.searchDelegate = null;
	}

	public void setFocus()
	{
		this.control.setFocus();
	}
	public boolean isEnabled()
	{
		return this.control.isEnabled();
	}
    public void setTooltip(String value)
    {
        this.control.setTooltip(value);
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
	public int size()
	{
		return this.control.size();
	}
	public void clear()
	{
		this.control.clear();
	}
	public ArrayList getValues()
	{
		return this.control.getValues();
	}
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	public void setValueSetEvent(ComboBoxValueSet delegate)
	{
		this.valueSetDelegate = delegate;
	}
	public void setSearchEvent(ComboBoxSearch delegate)
	{
		this.searchDelegate = delegate;
	}
	public void showOpened()
	{
		this.control.showOpened();
	}
	public int getID()
	{
		return this.control.getID();
	}
	public void setRequired(boolean value)
	{
		this.control.setRequired(value);
	}
	public boolean isRequired()
	{
		return this.control.isRequired();
	}
	public void enableLiveSearch(boolean value)
	{
		this.control.enableLiveSearch(value);
	}
	public boolean isLiveSearchEnabled()
	{
		return this.control.isLiveSearchEnabled();
	}
	
	protected ComboBox control;
	private ValueChanged valueChangedDelegate = null;
	private ComboBoxValueSet valueSetDelegate = null;
	private ComboBoxSearch searchDelegate = null;
}
