package ims.framework.controls;

import ims.framework.Control;
import ims.framework.ListItem;
import ims.framework.ListItemCollection;
import ims.framework.cn.Menu;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

/**
 * @author mmihalec
 */
public abstract class CheckedListBox extends Control 
{
	private static final long serialVersionUID = 1L;
	
    protected void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu, String tooltip, boolean required, int maxCheckedItems)
    {
        super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);        
        this.tooltip = tooltip;        
        this.required = required;
        this.maxCheckedItems = maxCheckedItems;
    }
    protected void free()
    {
        super.free();
        
        this.tooltip = null;
        this.valueChangedDelegate = null;
    }
    abstract public void newItem(ListItem item);
    abstract public int size();
    abstract public void clear();
    abstract public ListItemCollection getCheckedItems();
    abstract public void setCheckedItems(ListItemCollection values);
    abstract public void setTooltip(String value);
    abstract public void setRequired(boolean value);
    abstract public boolean isRequired(); 
    public int getMaxCheckedItems()
    {
    	return this.maxCheckedItems;
    }
    /**
     * Send -1 for no effect
     * @param value
     */
    public abstract void setMaxCheckedItems(int value);
    public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate;
    }    
    
    protected ValueChanged valueChangedDelegate = null;
    protected String tooltip = null;
    protected boolean required = false;
    protected int maxCheckedItems = -1;
}
