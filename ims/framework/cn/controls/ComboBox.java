package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.ComboBoxData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.ComboBoxChanged;
import ims.framework.cn.events.ComboBoxSearch;
import ims.framework.cn.events.ComboBoxTextChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.SortOrder;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class ComboBox extends ims.framework.controls.ComboBox implements IVisualControl
{    
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, boolean autoPostBack, SortOrder sortOrder, boolean seachable, int minNumberOfChars, String tooltip, boolean required, int maxVisibleItems, boolean livesearch)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, canBeEmpty, sortOrder, seachable, minNumberOfChars, required, maxVisibleItems, livesearch);
        super.tooltip = tooltip;
        this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
	public void setTooltip(String value)
    {
        super.tooltip = value;
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
	public void clear()
	{		
		this.data.clear();
	}
	public String getEditedText()
	{
	    return super.searchable ? this.data.getEditedText() : null;
	}
	public void setEditedText(String text)
	{
	    this.data.setEditedText(text);
	}
	public String getText()
	{
		if (this.data.isEmpty())
			return null;
		
		if (super.canBeEmpty)
		{
			if (this.data.getSelection() == -1)
				return null;
		}
		else if (this.data.getSelection() == -1)
		{
			if (super.isEnabled())
				return this.data.getText(0);
			return null;
		}
		return this.data.getText(this.data.getSelection());
	}
	public Object getValue()
	{
		if (this.data.isEmpty())
			return null;
		if (super.canBeEmpty)
		{
			if (this.data.getSelection() == -1)
				return null;
		}
		else if (this.data.getSelection() == -1)
		{
			if (super.isEnabled())
				return this.data.getValue(0);
			return null;
		}
		return this.data.getValue(this.data.getSelection());
	}
	public void setValue(Object value) //throws ims.framework.exceptions.PresentationLogicException
	{
		if(super.valueSetDelegate != null)
			super.valueSetDelegate.handle(value);
		
		if (super.canBeEmpty && value == null)
			this.data.setSelection(-1);
		else
			this.data.setSelection(value);
	}
	public ArrayList getValues()
	{
		return this.data.getValues();
	}
	public void showOpened()
	{
		this.data.setShowOpened(true);
	}
	public void newRow(Object value, String text)
	{
        newRow(value, text, null, null);
	}
    public void newRow(Object value, String text, Color textColor)
    {   
        newRow(value, text, null, textColor);
    }
    public void newRow(Object value, String text, Image image, Color textColor)
    {
        this.data.add(value, text, image, textColor);
        
        if (!super.canBeEmpty && this.data.getSelection() == -1)
            this.data.setSelection(0);
        
        if (!super.sortOrder.equals(SortOrder.NONE))
            this.data.sort(super.sortOrder);
    }
    public void newRow(Object value, String text, Image image)
    {   
        newRow(value, text, image, null);
    }
    public boolean removeRow(Object value)
    {
    	return this.data.remove(value);
    }
	public int size()
	{
		return this.data.size();
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
	public void enableLiveSearch(boolean value) 
	{
		this.data.enableLiveSearch(value);	
	}
	public boolean isLiveSearchEnabled() 
	{
		return this.data.isLiveSearchEnabled();		
	}	
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (ComboBoxData) data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			this.data.setTooltip(super.tooltip);
		}
		else
		{
			super.tooltip = this.data.getTooltip();
		}
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof ComboBoxChanged)
		{
			int newSelection = ((ComboBoxChanged) event).getSelection();
			if(this.data.getSelection() != newSelection) // is it possible for them to be equal?????????????????
			{
				boolean selectionChanged = data.isSelectionChanged();
				this.data.setSelection(newSelection);
				data.setSelectionChanged(selectionChanged);
				
				if(super.valueChangedDelegate != null)
				{
				    if(this.autoPostBack)
				        super.valueChangedDelegate.handle();
				}
			}
			else
			{
				if(newSelection < 0 && this.getEditedText() != null)
					this.setEditedText(null);
			}
			
			return true;
		}
		else if(event instanceof ComboBoxTextChanged)
		{
			this.data.setSelection(-1);
			this.data.setEditedText(((ComboBoxTextChanged) event).getText());
			
			if(this.autoPostBack)
			    super.valueChangedDelegate.handle();
			
			return true;
		}
		else if(event instanceof ComboBoxSearch)
		{
			if (super.searchDelegate != null)
			{	
				String text = ((ComboBoxSearch) event).getText();
			    Integer selection = ((ComboBoxSearch) event).getSelection();
			    
			    if(text != null)
			    {
			    	this.data.setSelection(-1);
			    	this.data.setEditedText(text);
			    }
			    else if(selection != null)
			    {
			    	this.data.setSelection(selection.intValue());
			    	text = this.data.getEditedText();
			    }
			    			    
				super.searchDelegate.handle(text);//text == null ? "" : text);
				
				if (this.livesearch)
					this.data.setSearchText(text);
			}
			
			return true;
		}		
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<combobox id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"19");
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		
		if(super.required)
		{
			sb.append("\" required=\"true");
		}
		if(super.searchable)
		{
			sb.append("\" searchable=\"true");
			
			if (super.livesearch || this.enableLiveSearch)
			{
				sb.append("\" livesearch=\"true");
			}
			
			if(super.minNumberOfChars > 0)
			{
				sb.append("\" minNumberOfChars=\"");
				sb.append(super.minNumberOfChars);
			}
		}
		if(super.canBeEmpty)
		{
			sb.append("\" canBeEmpty=\"true");
		}		
		if(super.maxVisibleItems > 0)
		{
			sb.append("\" maxPopupItems=\"");
			sb.append(super.maxVisibleItems);
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
		
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<combobox id=\"a");
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
				
				if (!super.enableLiveSearch)
				{
					if(data.isEnableLiveSearchChanged())
					{
						sb.append("\" livesearch=\"");
						sb.append(this.data.isLiveSearchEnabled() ? "true" : "false");							
						data.enableLiveSearchChanged(false);
					}
				}
			}
			
			if(data.isShowOpenedChanged())
			{
				sb.append("\" opened=\"");
				sb.append(this.data.getShowOpened() ? "true" : "false");
				this.data.setShowOpened(false);
				data.setShowOpenedChanged(false);
			}
			
			if(data.isTooltipChanged())
			{
				sb.append("\" tooltip=\"");
		        sb.append(super.tooltip == null ? "" : StringUtils.encodeXML(super.tooltip));
		        data.setTooltipUnchanged();
			}
	        
			if(data.isSelectionChanged())
			{
				sb.append("\" selection=\"");
				sb.append(this.data.getSelection());
				data.setSelectionChanged(false);
			}
			
			if(this.searchable && this.data.getSelection() == -1)
			{			
				if(data.isEditedTextChanged())
				{
				    sb.append("\" value=\"");
				    
				    if (this.data.getSearchText() != null)
				    	sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getSearchText()));				    	
				    else if(this.data.getEditedText() != null)
				    	sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getEditedText()));
				    
				    data.setEditedTextChanged(false);
				}
			}
			sb.append("\"");
			
			if(data.isValueChanged())
			{
				if(this.data.getValues().size() == 0)
				{
					sb.append(">");
					sb.append("<nooptions/>");
					sb.append("</combobox>");
				}
				else
				{
				    sb.append(">");
					for (int i = 0; i < this.data.size(); ++i)
					{
						sb.append("<option text=\"");
						sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getText(i)));
	                    sb.append("\" ");
	                    if(this.data.getTextColor(i) != null)
	                        sb.append(" textColor=\"" + this.data.getTextColor(i) + "\"");
	                    
	                    Image img = this.data.getImage(i);
	                    if(img != null)
	                    {
	                        sb.append(" img=\"");
	                        sb.append(img.getImagePath());
	                        
	                        // Size attributes not send until ImageInfo class returns the values correctly.                        
	                        int width = img.getImageWidth();
	                        int height = img.getImageHeight();
	                        
	                        if(width > 0 && height > 0)
	                        {
	                        	sb.append("\" imgW=\"" + width);
	                        	sb.append("\" imgH=\"" + height);
	                        }
	                        
	                        sb.append("\" ");
	                    }
	                    sb.append(" />");
					}
					sb.append("</combobox>");				
				}
			
				data.setValueChanged(false);
			}
			else
			{
				 sb.append(" />");			
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
			
			if(data.isSelectionChanged())
				return true;
			if(data.isValueChanged())
				return true;
			if(searchable && data.isEditedTextChanged())
				return true;
			if(data.isShowOpenedChanged())
				return true;
			if(data.isTooltipChanged())
				return true;
		}
		
		return false;
	}	
	public void markUnchanged() 
	{		
	}
    
    private ComboBoxData data;
	private int tabIndex;
	private boolean autoPostBack;
	private boolean enableLiveSearch;
}
