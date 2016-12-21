package ims.framework.controls;

import java.io.Serializable;

import ims.framework.IControlComparable;
import ims.framework.delegates.ValueChanged;

/**
 * @author mmihalec
 */ 
public abstract class CheckedListBoxBridge implements IControlComparable, Serializable
{
	private static final long serialVersionUID = 1L;
	
    protected void setContext(CheckedListBox control)
    {
        this.control = control;
        this.control.setValueChangedEvent(new ValueChanged()
        {
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
            {
                if(CheckedListBoxBridge.this.valueChangedDelegate != null)
                    CheckedListBoxBridge.this.valueChangedDelegate.handle();
            }
        });
    }
    public void free()
    {
        this.control = null;
        this.valueChangedDelegate = null;        
    }
    public int getID()
    {
        return this.control.getID();
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
    public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate;
    }
    public int getMaxCheckedItems()
    {
    	return this.control.getMaxCheckedItems();
    }
    public void setMaxCheckedItems(int value)
    {
    	this.control.setMaxCheckedItems(value);
    }
    public void setRequired(boolean value)
    {
    	this.control.setRequired(value);
    }
    public boolean isRequired()
    {
    	return this.control.isRequired();
    }
    
    protected CheckedListBox control;
    private ValueChanged valueChangedDelegate = null;
}
