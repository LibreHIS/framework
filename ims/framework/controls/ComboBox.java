package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.ValueChanged;
import ims.framework.delegates.ComboBoxValueSet;
import ims.framework.delegates.ComboBoxSearch;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.SortOrder;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

public abstract class ComboBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, SortOrder sortOrder, boolean seachable, int minNumberOfChars, boolean required, int maxVisibleItems, boolean livesearch)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.canBeEmpty = canBeEmpty;
		this.sortOrder = sortOrder;
		this.searchable = seachable;
		this.livesearch = livesearch;
		this.minNumberOfChars = minNumberOfChars;
		this.required = required;
		this.maxVisibleItems = maxVisibleItems; 
 	}
  	protected void free()
	{
		super.free();
		
		this.tooltip = null;
		this.valueChangedDelegate = null;
		this.valueSetDelegate = null;
		this.searchDelegate = null;
	}    
      	
	abstract public void newRow(Object value, String text);
    abstract public void newRow(Object value, String text, Color textColor);
    abstract public void newRow(Object value, String text, Image image, Color textColor);
    abstract public void newRow(Object value, String text, Image image);
    abstract public boolean removeRow(Object value);
	abstract public Object getValue();
    abstract public void setValue(Object value); 
	abstract public String getText();
	abstract public void setEditedText(String text);
	abstract public String getEditedText();
	abstract public int size();
	abstract public void clear();
	abstract public ArrayList getValues();
	abstract public void showOpened();
    abstract public void setTooltip(String value);
    abstract public void setRequired(boolean value);
    abstract public boolean isRequired();    
    abstract public void enableLiveSearch(boolean value);
    abstract public boolean isLiveSearchEnabled();   
    
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

    protected ValueChanged valueChangedDelegate = null;
    protected ComboBoxValueSet valueSetDelegate = null;
    protected ComboBoxSearch searchDelegate = null;
	protected boolean canBeEmpty;
	protected SortOrder sortOrder;
	protected boolean searchable;
	protected boolean livesearch;
	protected int minNumberOfChars = 1;
    protected String tooltip;
    protected boolean required = false;
    protected boolean enableLiveSearch = false;
    protected int maxVisibleItems = -1;
}

