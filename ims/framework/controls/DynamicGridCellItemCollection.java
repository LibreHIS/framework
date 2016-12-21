package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;

import ims.base.interfaces.IModifiable;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public class DynamicGridCellItemCollection implements IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public DynamicGridCellItemCollection(DynamicGridCell cell)
	{
		this.cell = cell;
	}
	
    public int size()
    {
        return this.items.size();
    }
    public int indexOf(Object value)
    {
        int index = -1;
        int noItems = this.items.size();
        for(int x = 0; x < noItems; x++)
        {
            DynamicGridCellItem item = this.items.get(x);
            if(item != null && item.valueEquals(value))
            {
                index = x;
                break;
            }
        }
        return index;
    }
	public DynamicGridCellItem get(int index)
	{
	    return this.items.get(index);
	}	
	public DynamicGridCellItem newItem()
	{
	    return newItem(null, null, null, null, false);
	}
	/**
	 * @deprecated replaced by newItem(Object value, String text)
	 */
	public DynamicGridCellItem newItem(Object value)
	{
	    return newItem(value, null, null, null, false);
	}
	public DynamicGridCellItem newItem(Object value, String text)
	{
	    return newItem(value, text, null, null, false);
	}
	/**
	 * @deprecated replaced by newItem(Object value, String text, boolean checked)
	 */  
	public DynamicGridCellItem newItem(Object value, boolean checked)
	{
	    return newItem(value, null, null, null, checked);
	}
	public DynamicGridCellItem newItem(Object value, String text, boolean checked)
	{
	    return newItem(value, text, null, null, checked);
	}
	/**
	 * @deprecated replaced by newItem(Object value, String text, Image image)
	 */  
	public DynamicGridCellItem newItem(Object value, Image image)
	{
		return newItem(value, image, null, false);
	}
	public DynamicGridCellItem newItem(Object value, String text, Image image)
	{
		return newItem(value, text, image, null, false);
	}
	/**
	 * @deprecated replaced by newItem(Object value, String text, Image image, boolean checked)
	 */   
	public DynamicGridCellItem newItem(Object value, Image image, boolean checked)
	{
		return newItem(value, null, image, null, checked);
	}
	public DynamicGridCellItem newItem(Object value, String text, Image image, boolean checked)
	{
		return newItem(value, text, image, null, checked);
	}
	/**
	 * @deprecated replaced by newItem(Object value, String text, Image image, Color textColor)
	 */ 
	public DynamicGridCellItem newItem(Object value, Image image, Color textColor)
	{
		return newItem(value, null, image, textColor, false);
	}
	public DynamicGridCellItem newItem(Object value, String text, Image image, Color textColor)
	{
		return newItem(value, text, image, textColor, false);
	}
	/**
	 * @deprecated replaced by newItem(Object value, String text, Image image, Color textColor, boolean checked)
	 */ 
	public DynamicGridCellItem newItem(Object value, Image image, Color textColor, boolean checked)
    {
        DynamicGridCellItem item = new DynamicGridCellItem(value, null, image, textColor, checked);
        this.wasChanged = true;
        this.items.add(item);
        return item;
    }
	public DynamicGridCellItem newItem(Object value, String text, Image image, Color textColor, boolean checked)
    {
        DynamicGridCellItem item = new DynamicGridCellItem(value, text, image, textColor, checked);
        this.wasChanged = true;
        this.items.add(item);
        return item;
    }	
	public boolean remove(DynamicGridCellItem item)
	{   		
	    boolean wasRemoved = this.items.remove(item);
	    if(wasRemoved)
	    	this.wasChanged = true;
	    return wasRemoved;
	}
	public void clear()
	{
		if(this.items.size() > 0)
			this.wasChanged = true;
		
		this.cell.setValue(null);
	    this.items.clear();
	}
	public void checkAll() 
	{
		int noItems = this.items.size();
		for(int x = 0; x < noItems; x++)
		{
			this.items.get(x).setChecked(true);
		}
	}
	public void uncheckAll() 
	{
		int noItems = this.items.size();
		for(int x = 0; x < noItems; x++)
		{
			this.items.get(x).setChecked(false);
		}
	}
	
	public boolean wasChanged()
	{
		if(this.wasChanged)
			return true;
		
		int noItems = this.items.size();
		for(int x = 0; x < noItems; x++)
	    {
	    	DynamicGridCellItem item = this.items.get(x);
	    	if(item.wasChanged())
	    		return true;	    	
	    }
		
		return false;
	}
	public void markUnchanged()
	{
		this.wasChanged = false;
		
		int noItems = this.items.size();
		for(int x = 0; x < noItems; x++)
	    {
	    	DynamicGridCellItem item = this.items.get(x);
	    	item.markUnchanged();   	
	    }
	}   
	
	private DynamicGridCell cell;
	private boolean wasChanged = false;
    private ArrayList<DynamicGridCellItem> items = new ArrayList<DynamicGridCellItem>();	
}
