package ims.framework;

import ims.base.interfaces.IModifiable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author mmihalec
 */
public class ListItemCollection implements IItemCollection, IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	
    public ListItemCollection()
    {
        this(true);
    }
    public ListItemCollection(boolean allowDuplicates)
    {
        this.allowDuplicates = allowDuplicates;
    }
    public void add(ListItem item)
    {
        if(this.allowDuplicates || this.items.indexOf(item) < 0)
            this.items.add(item);        
    }
    public void remove(ListItem item)
    {
        boolean removed = this.items.remove(item);
        if(removed)
        	this.dataWasChanged = true;
    }
    public void clear()
    {
    	if(this.items.size() > 0)
    		this.dataWasChanged = true;
    	
        this.items.clear();
    }
    public int size()
    {
        return this.items.size();
    }
    public ListItem get(int index)
    {
        return this.items.get(index);
    }
    public IItem[] getItems() 
    {
        return (IItem[])this.items.toArray();        
    }
    
    public void setCheckedItems(ListItemCollection values)
    {
    	uncheckAll();    	
    	if(values == null)
    		return;
    	
    	ListItemCollection notFoundItems = new ListItemCollection();
    	
    	for(int x = 0; x < values.size(); x++)
    	{
    		ListItem item = values.get(x);
    		if(item != null && item.getValue() != null)
    		{
    			boolean found = false; 
    			for(int y = 0; y < this.items.size(); y++)
    			{
    				if(item.getValue().equals(this.items.get(y).getValue()))
    				{
    					this.items.get(y).setChecked(true);
    					found = true;
    					break;
    				}
    			}    
    			
    			if(!found)
    				notFoundItems.add(item);
    		}
    	}    
    	
    	addUnfoundItems(notFoundItems);
    }
    private void addUnfoundItems(ListItemCollection notFoundItems) 
    {
		if(notFoundItems == null)
			return;
		
		for(int x = 0; x < notFoundItems.size(); x++)
		{
			ListItem item = notFoundItems.get(x);
			
			if(item.getValue() != null)
			{
				item.setEnabled(true);
				item.setChecked(true);
				item.setText(item.getValue().toString());
				
				if(item.getValue() instanceof IEnhancedItem)
				{
					IEnhancedItem iitem = (IEnhancedItem)item.getValue();
					item.setImage(iitem.getIItemImage());
					item.setTextColor(iitem.getIItemTextColor());
				}
				
				add(item);
			}
		}
	}
	public ListItemCollection getCheckedItems()
    {
        ListItemCollection selectedItems = new ListItemCollection(this.allowDuplicates);
        int noItems = this.items.size();
        for(int x = 0; x < noItems; x++)
        {
            ListItem item = this.items.get(x);
            if(item.isChecked())
                selectedItems.add(item);
        }
        return selectedItems;
    }
    public ListItemCollection getUncheckedItems()
    {
        ListItemCollection selectedItems = new ListItemCollection(this.allowDuplicates);
        int noItems = this.items.size();
        for(int x = 0; x < noItems; x++)
        {
            ListItem item = this.items.get(x);
            if(!item.isChecked())
                selectedItems.add(item);
        }
        return selectedItems;
    }
    public void checkAll()
    {
    	checkUncheckAll(true);
    }
    public void uncheckAll()
    {
    	checkUncheckAll(false);
    }
    private void checkUncheckAll(boolean check)
    {
    	for(int x = 0; x < this.items.size(); x++)
        {
            this.items.get(x).setChecked(check);            
        }
    }
    public boolean wasChanged() 
    {
		if(this.dataWasChanged)
			return true;
		
		for(int x = 0; x < this.items.size(); x++)
		{
			if(this.items.get(x).wasChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{
		this.dataWasChanged = false;
		
		for(int x = 0; x < this.items.size(); x++)
		{
			this.items.get(x).markUnchanged();				
		}
	}
    
	private boolean dataWasChanged = false;
    private ArrayList<ListItem> items = new ArrayList<ListItem>();
    private boolean allowDuplicates;	   
}
