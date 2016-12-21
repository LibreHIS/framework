package ims.framework.cn.data;

import ims.framework.ListItem;
import ims.framework.ListItemCollection;

/**
 * @author mmihalec
 */
public class CheckedListBoxData implements IControlData
{    
	private static final long serialVersionUID = 1888599198446753237L;
	public void newItem(ListItem item) 
    {
		for(int x = 0; x < this.items.size(); x++)
		{
			ListItem existingItem = this.items.get(x); 
			if(existingItem.getValue() != null && existingItem.getValue().equals(item.getValue()))
			{				
				return;
			}
		}
		
        this.items.add(item);
    }
    public boolean isEnabled()
    {
        return this.enabled;
    }
    public boolean isVisible()
    {
        return this.visible;
    }
    public void setEnabled(boolean value)
    {
    	if(!this.enabledChanged)
    		this.enabledChanged = this.enabled != value;
    	
        this.enabled = value;
    }
    public void setVisible(boolean value)
    {
    	if(!this.visibleChanged)
    		this.visibleChanged = this.visible != value;
    	
        this.visible = value;
    }
    public void setTooltip(String value)
    {
    	if(!this.tooltipChanged)
    	{
	    	if(this.tooltip == null)
				this.tooltipChanged = value != null;
			else 
				this.tooltipChanged = !this.tooltip.equals(value);
    	}
    	
        this.tooltip = value;
    }
    public String getTooltip()
    {
        return this.tooltip == null ? "" : this.tooltip;
    }
    public void clear()
    {
    	setSelectedIndex(-1);
    	this.items.clear();
    }
    public int size()
    {     
        return this.items.size();
    }
    public ListItem getRow(int index)
    {
        return this.items.get(index);
    }
    public ListItemCollection getCheckedItems()
    {
        return this.items.getCheckedItems();
    }
    public void setCheckedItems(ListItemCollection values)
    {
    	this.items.setCheckedItems(values);
    }
    public ListItemCollection getItems()
    {
        return this.items;
    }
    public void setSelected(int index, boolean selected)
    {    	
    	this.items.get(index).setChecked(selected);
    }
    public int getSelectedIndex()
    {
    	return this.selectedIndex;
    }
    public void setSelectedIndex(int value)
    {
    	if(!this.selectedIndexChanged)
    		this.selectedIndexChanged = this.selectedIndex != value;
    	
    	this.selectedIndex = value;
    }    
	public void setMaxCheckedItems(int value) 
	{
		if(!this.maxCheckedItemsChanged)
    		this.maxCheckedItemsChanged = this.maxCheckedItems != value;
		
		this.maxCheckedItems = value;
		this.hasMaxCheckedItems = true;
	}
	public int getMaxCheckedItems()
	{
		return this.maxCheckedItems;
	}
	public boolean hasMaxCheckedItems()
	{
		return this.hasMaxCheckedItems;
	}
	public void setRequired(boolean value)
	{
		if(!this.requiredChanged)
			this.requiredChanged = this.required != value;
		
		this.required = value;
	}
	public boolean isRequired()
	{
		return this.required;
	}        
    public void setEnabledChanged(boolean enabledChanged)
	{
		this.enabledChanged = enabledChanged;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setRequiredChanged(boolean requiredChanged)
	{
		this.requiredChanged = requiredChanged;
	}
	public boolean isRequiredChanged()
	{
		return requiredChanged;
	}
	public void setTooltipChanged(boolean tooltipChanged)
	{
		this.tooltipChanged = tooltipChanged;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}
	public void setSelectedIndexChanged(boolean selectedIndexChanged)
	{
		this.selectedIndexChanged = selectedIndexChanged;
	}
	public boolean isSelectedIndexChanged()
	{
		return selectedIndexChanged;
	}
	public void markItemsUnchanged()
	{
		this.items.markUnchanged();
	}
	public boolean isItemsChanged()
	{
		return this.items.wasChanged();
	}
	public void setMaxCheckedItemsChanged(boolean maxCheckedItemsChanged)
	{
		this.maxCheckedItemsChanged = maxCheckedItemsChanged;
	}
	public boolean isMaxCheckedItemsChanged()
	{
		return maxCheckedItemsChanged;
	}	

    private boolean enabled = true;
    private boolean visible = true;
    private boolean required = false;
    private String tooltip = null;
    private int selectedIndex = -1;
    private ListItemCollection items = new ListItemCollection();
    private int maxCheckedItems = -1;	
    private boolean hasMaxCheckedItems = false;
    
    private boolean enabledChanged = false;
    private boolean visibleChanged = false;
    private boolean requiredChanged = false;
    private boolean tooltipChanged = false;
    private boolean selectedIndexChanged = false;    
    private boolean maxCheckedItemsChanged = false;
}
