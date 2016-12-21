package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

public abstract class RecordBrowser extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}
	
	abstract public void newRow(Object value, String text);
    abstract public void newRow(Object value, String text, Color textColor);
    abstract public void newRow(Object value, String text, Image image, Color textColor);
    abstract public void newRow(Object value, String text, Image image);   
    abstract public void newRow(int index, Object value, String text);
    abstract public void newRow(int index, Object value, String text, Color textColor);
    abstract public void newRow(int index, Object value, String text, Image image, Color textColor);
    abstract public void newRow(int index, Object value, String text, Image image);
    
	abstract public Object getValue();
    abstract public void setValue(Object value);
    abstract public int size();
	abstract public void clear();
	abstract public void setTooltip(String value);
	abstract public int getSelectedIndex();
	abstract public ArrayList getValues();
	
	public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate;
    }
	
	protected ValueChanged valueChangedDelegate = null;	
}
