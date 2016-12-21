package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;

import ims.framework.IControlComparable;
import ims.framework.delegates.ValueChanged;

public abstract class RecordBrowserBridge implements IControlComparable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected void setContext(RecordBrowser control)
	{
		this.control = control;
		this.control.setValueChangedEvent(new ValueChanged()
		{			
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
			{
				if (RecordBrowserBridge.this.valueChangedDelegate != null)
					RecordBrowserBridge.this.valueChangedDelegate.handle();
			}
		});		
	}
	public void free()
	{
		this.control = null;
		this.valueChangedDelegate = null;		
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
	public int getSelectedIndex()
	{
		return this.control.getSelectedIndex();
	}
	public ArrayList getValues()
	{
		return this.control.getValues();
	}
	public String getRecordState()
	{
		return getRecordState("Record", "Records");
	}
	public String getRecordState(String prefixSingular, String prefixPlural)
	{
		if(this.control.size() == 0)
			return "No " + prefixPlural;
		
		return prefixSingular + " " + (this.control.getSelectedIndex() + 1) + " of " + this.control.size(); 
	}
	public void clear()
	{
		this.control.clear();
	}
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	public int getID()
	{
		return this.control.getID();
	}
	
	protected RecordBrowser control;
	private ValueChanged valueChangedDelegate = null;	
}
