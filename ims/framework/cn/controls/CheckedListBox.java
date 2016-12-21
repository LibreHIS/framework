package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.ListItem;
import ims.framework.ListItemCollection;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.Menu;
import ims.framework.cn.data.CheckedListBoxData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.CheckedListBoxChanged;
import ims.framework.cn.events.CheckedListBoxItemChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.StringUtils;

/**
 * @author mmihalec
 */
public class CheckedListBox extends ims.framework.controls.CheckedListBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu, boolean autoPostBack, String tooltip, int tabIndex, boolean required, int maxCheckedItems)
    {
        super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu, tooltip, required, maxCheckedItems);
        this.tooltip = tooltip;
        this.required = required;
        this.maxCheckedItems = maxCheckedItems; 
        this.tabIndex = tabIndex;
        this.autoPostBack = autoPostBack;
    }
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
    public void newItem(ListItem item) 
    {
        this.data.newItem(item);
    }
    public int size() 
    {
        return this.data.size();
    }
    public void clear() 
    {
        this.data.clear();
    }
    public ListItemCollection getCheckedItems() 
    {
        return this.data.getCheckedItems();
    }
    public void setCheckedItems(ListItemCollection values)
    {
    	this.data.setCheckedItems(values);
    }
    public void setTooltip(String value) 
    {
        this.tooltip = value;
        this.data.setTooltip(value);
    }    
    public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public void setMaxCheckedItems(int value)
    {
    	super.maxCheckedItems = value < -1 ? -1 : value;
    	this.data.setMaxCheckedItems(value < -1 ? -1 : value);
    }
	public void setRequired(boolean value)
	{
		if(super.required == true)
			throw new CodingRuntimeException("The control does not allow setting the required property at runtime as it was already marked as required at design time");
		this.data.setRequired(value);
	}
	public boolean isRequired()
	{
		if(super.required == true)
			return true;
		return this.data.isRequired();
	}
    public void restore(IControlData data, boolean isNew) 
    {
        this.data = (CheckedListBoxData)data;
        super.enabled = this.data.isEnabled();
        super.visible = this.data.isVisible();
        
        if(isNew)
        {
        	this.data.setTooltip(super.tooltip);
        	this.data.setMaxCheckedItems(this.maxCheckedItems);
        }
        else
        {
        	super.tooltip = this.data.getTooltip();
        	super.maxCheckedItems = this.data.getMaxCheckedItems();
        }        
    }
    public boolean fireEvent(IControlEvent event) throws PresentationLogicException 
    {
    	if(event instanceof CheckedListBoxChanged)
    	{
    		CheckedListBoxChanged castEvent = (CheckedListBoxChanged)event;    		    		
    		    		
    		if(castEvent.getSelectionIndex() != -2) // -2 means no change in selection index
    		{
    			boolean selectionIndexChanged = this.data.isSelectedIndexChanged();
    			this.data.setSelectedIndex(castEvent.getSelectionIndex());    		
	    		data.setSelectedIndexChanged(selectionIndexChanged);
    		}    		
    		if(castEvent.getItems() != null)
    		{
    			boolean itemsChanged = data.isItemsChanged();
    			for(int x = 0; x < castEvent.getItems().length; x++)
    			{
    				CheckedListBoxItemChanged item = castEvent.getItems()[x];
    				data.setSelected(item.getIndex(), item.isChecked());
    			}    			
    			if(!itemsChanged)
    				data.markItemsUnchanged();
    			
    			if(super.valueChangedDelegate != null)
    				super.valueChangedDelegate.handle();
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    public void renderControl(StringBuffer sb) throws ConfigurationException 
    {
    	sb.append("<checkedlistbox id=\"a");
    	sb.append(super.id);    	
    	sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		
		if(super.required)
		{
			sb.append("\" required=\"true");
		}
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if(super.menu != null)
		{
			sb.append("\" menuID=\"");
			sb.append(super.menu.getID());
		}
		if(super.tooltip != null) 
        {
            sb.append("\" tooltip=\"");
            sb.append(StringUtils.encodeXML(super.tooltip));
        }   
		
    	sb.append("\" />");
    }
    public void renderData(StringBuffer sb) throws ConfigurationException 
    {
    	sb.append("<checkedlistbox id=\"a");
    	sb.append(super.id);
    	
    	if(data.isVisibleChanged())
    	{
    		sb.append("\" visible=\"");
    		sb.append(this.data.isVisible() ? "true" : "false");
    		data.setVisibleChanged(false);
    	}
    	
    	if(this.data.isVisible())
    	{
    		if(!hasAnyParentDisabled())
    		{
    			if(data.isEnabledChanged())
    			{
    				sb.append("\" enabled=\"");    	
    				sb.append(this.data.isEnabled() ? "true" : "false");
    				data.setEnabledChanged(false);
    			}
    		
	    		if(!super.required && enabled)
				{
	    			if(data.isRequiredChanged())
	    			{
	    				sb.append("\" required=\"");
	    				sb.append(this.data.isRequired() ? "true" : "false");
	    				data.setRequiredChanged(false);
	    			}
				}
    		}
    		
    		if(data.isSelectedIndexChanged())
    		{
    			sb.append("\" selectedIndex=\"");
    			sb.append(data.getSelectedIndex());
    			data.setSelectedIndexChanged(false);
    		}
	    	
	    	if(data.isTooltipChanged())
	    	{
	    		sb.append("\" tooltip=\"");
	    		sb.append(this.tooltip == null ? "" : StringUtils.encodeXML(this.tooltip));
	    		data.setTooltipChanged(false);
	    	}
	    	
	    	if(data.isMaxCheckedItemsChanged())
	    	{
	    		sb.append("\" maxCheckedItems=\"");
	    		sb.append(super.maxCheckedItems);
	    		data.setMaxCheckedItemsChanged(false);
	    	}
	        	    	
	        if(data.isItemsChanged())
	        {
	        	sb.append("\" >");
	        	
		        if(data.getItems().size() == 0)
		        	sb.append("<items/>");
		        else
		        {
		        	sb.append("<items>");
		        	
		        	for(int x = 0; x < data.getItems().size(); x++)
		        	{
		        		ListItem item = data.getItems().get(x);
		        		
		        		sb.append("<item text=\"");
		        		sb.append(StringUtils.encodeXML(item.getText()));
		        		sb.append("\" enabled=\"");
		        		sb.append(item.isEnabled() ? "true" : "false");
		        		sb.append("\" checked=\"");
		        		sb.append(item.isChecked() ? "true" : "false");
		        		if(item.getTextColor() != null)
		        		{
		        			sb.append("\" textColor=\"");
		            		sb.append(item.getTextColor());
		        		}        	
		        		if(item.getImage() != null)
		        		{
		        			sb.append("\" img=\"");
		        			sb.append(item.getImage().getImagePath());                   
		        		}
		        		if(item.getTooltip() != null)
		        		{
		        			sb.append("\" tooltip=\"");
		        			sb.append(StringUtils.encodeXML(item.getTooltip()));                   
		        		}
		        		
		        		sb.append("\" />");
		        	}
		        	sb.append("</items>");        	
		        }
		        
		        data.markItemsUnchanged();
		        
		        sb.append("</checkedlistbox>");
	        }
	        else
	        {
	        	sb.append("\" />");
	        }	            	
    	}
    	else
    	{
    		sb.append("\" />");
    	}
    }    
    public boolean wasChanged() 
    {
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
				
				if(!super.required && enabled)
				{
					if(data.isRequiredChanged())
						return true;
				}
			}
			
			if(data.isTooltipChanged())
				return true;
			if(data.isSelectedIndexChanged())
				return true;
			if(data.isMaxCheckedItemsChanged())
				return true;
			if(data.isItemsChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
    private CheckedListBoxData data;
    protected boolean autoPostBack;
    protected int tabIndex;
}
