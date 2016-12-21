package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;

public abstract class FilePicker extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free() // free resources
	{
		super.free();
		this.valueChangedDelegate = null;
	}	

	abstract public String getValue();
    abstract public void setValue(String value);
    abstract public void setFileType(String value);
    
    public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate; 
    }    
 
    protected ValueChanged valueChangedDelegate = null;
}
