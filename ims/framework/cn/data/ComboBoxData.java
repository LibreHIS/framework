package ims.framework.cn.data;

import ims.framework.enumerations.SortOrder;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

import java.util.ArrayList;

public class ComboBoxData implements IControlData
{
	private static final long serialVersionUID = -6842256206353990466L;
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
	public void clear()
	{
		if(!this.valueChanged)
			this.valueChanged = this.values.size() != 0;		
		if(!this.selectionChanged)
			this.selectionChanged = this.selection != -1;
		if(!this.editedTextChanged)
			this.editedTextChanged = this.editedText != null;
		if(!this.searchTextChanged)
			this.searchTextChanged = this.searchText != null;
		
		this.selection = -1;
		this.values.clear();
		this.texts.clear();
		this.textColors.clear();
		this.images.clear();
		this.editedText = null;
		this.searchText = null;
	}
	public void setEditedText(String value)
	{
		if(!this.editedTextChanged)
		{
			if(this.editedText == null)
				this.editedTextChanged = value != null;
			else 
				this.editedTextChanged = this.editedText.equals(value);
		}
		
	    this.editedText = value;
	    
	    if(value != null)
	    {
		    for(int x = 0; x < this.texts.size(); x++)
		    {
		    	if(value.equalsIgnoreCase(this.texts.get(x)))
		    	{
		    		if(!this.selectionChanged)
		    			this.selectionChanged = this.selection != x;
		    		
		    		this.editedText = this.texts.get(x);
		    		this.selection = x;
		    		break;
		    	}
		    }
	    }	    
	    else
	    {
	    	if(!this.selectionChanged)
    			this.selectionChanged = this.selection != -1;
	    	
	    	this.selection = -1;
	    }
	}
	public String getEditedText()
	{
	    return this.editedText; 
	}
	public void setSearchText(String value)
	{
		if(!this.searchTextChanged)
		{
			if(this.searchText == null)
				this.searchTextChanged = value != null;
			else 
				this.searchTextChanged = this.searchText.equals(value);
		}
		
	    this.searchText = value;	    
	    this.selection = -1;	    
	}
	public String getSearchText()
	{
	    return this.searchText; 
	}
	public boolean isEmpty()
	{
		return this.values.size() == 0;
	}
	public int getSelection()
	{
		return this.selection;
	}
	public void setSelection(int index)
	{
		if(index < -1)
			index = -1;
		
		if(!this.selectionChanged)
			this.selectionChanged = this.selection != index; 
			
		this.selection = index;
		String newEditedText = (index >= 0 && this.texts.size() >= index + 1) ? (String)this.texts.get(index) : null;
				
		if(!this.editedTextChanged)
		{
			if(this.editedText == null)
				this.editedTextChanged = newEditedText != null;
			else
				this.editedTextChanged = !this.editedText.equals(newEditedText);
		}
		
		this.editedText = newEditedText;
	}
	public void setSelection(Object value)
	{
		setSelection(this.values.indexOf(value));
	}
	public String getText(int index)
	{
		return this.texts.get(index);
	}
	public Object getValue(int index)
	{
		return this.values.get(index);
	}
	public Image getImage(int index)
    {
        return this.images.get(index);
    }
	public Color getTextColor(int index)
    {
        return this.textColors.get(index);
    }
	public ArrayList getValues()
	{
		ArrayList<Object> result = new ArrayList<Object>();
		for (int i = 0; i < this.values.size(); ++i)
			result.add(this.values.get(i));
		return result;
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
        return this.tooltip;
    }
    public boolean getShowOpened()
	{
		return this.showOpened;
	}
	public void setShowOpened(boolean value)
	{
		if(!this.showOpenedChanged)
			this.showOpenedChanged = this.showOpened != value;
		
		this.showOpened = value;
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
	public void enableLiveSearch(boolean value)
	{
		if(!this.enableLiveSearchChanged)
			this.enableLiveSearchChanged = this.enableLiveSearch != value;
		
		this.valueChanged = true;
		
		this.enableLiveSearch = value;		
	}
	public boolean isLiveSearchEnabled()
	{
		return this.enableLiveSearch;
	}
	public void add(Object value, String text, Image image, Color textColor)
	{
		//Don't add it if it's already there.
		if (this.values.indexOf(value) != -1) 
			return;
		
		this.valueChanged = true;		
		this.values.add(value);
		this.texts.add(text);
        this.images.add(image);
        this.textColors.add(textColor);
	}
	public boolean remove(Object value)
	{
		int index = this.values.indexOf(value);
		if(index >= 0)
		{
			this.valueChanged = true;
			if(this.selection == index)
			{
				this.selection = -1;
				this.selectionChanged = true;
			}
			
			this.values.remove(index);
			this.texts.remove(index);
			this.images.remove(index);
			this.textColors.remove(index);
			
			return true;
		}
		
		return false;
	}
	public int size()
	{
		return this.values.size();
	}
	public void sort(SortOrder order)
	{
		//TODO: This method should be implemented using the Collections.sort(...) method
		Object currentSelection = (this.selection == -1 ? null : this.values.get(this.selection));
		int k = 0;
		do
		{
			k = 0;
			for (int i = 0; i < this.texts.size() - 1; ++i)
			{
				String t1 = this.texts.get(i);
				String t2 = this.texts.get(i + 1);
				if ((order.equals(SortOrder.ASCENDING) && t1.compareToIgnoreCase(t2) > 0) || (order.equals(SortOrder.DESCENDING) && t1.compareToIgnoreCase(t2) < 0))
				{
					this.valueChanged = true;
					
					this.texts.set(i, t2);
					this.texts.set(i + 1, t1);

					Object o = this.values.get(i);
					this.values.set(i, this.values.get(i + 1));
					this.values.set(i + 1, o);

					++k;
				}
			}
		}
		while (k > 0);
		
		if (currentSelection != null)
		{
			setSelection(this.values.indexOf(currentSelection));			
		}
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
	public void enableLiveSearchChanged(boolean enableLiveSearchChanged)
	{
		this.enableLiveSearchChanged = enableLiveSearchChanged;
	}
	public boolean isEnableLiveSearchChanged()
	{
		return enableLiveSearchChanged;
	}
	public void setSelectionChanged(boolean selectionChanged)
	{
		this.selectionChanged = selectionChanged;
	}
	public boolean isSelectionChanged()
	{
		return selectionChanged;
	}
	public void setValueChanged(boolean valueChanged)
	{
		this.valueChanged = valueChanged;
	}
	public boolean isValueChanged()
	{
		return valueChanged;
	}
	public void setTooltipUnchanged()
	{
		this.tooltipChanged = false;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}
	public void setEditedTextChanged(boolean editedTextChanged)
	{
		this.editedTextChanged = editedTextChanged;
	}
	public boolean isEditedTextChanged()
	{
		return editedTextChanged;
	}
	public void setShowOpenedChanged(boolean showOpenedChanged)
	{
		this.showOpenedChanged = showOpenedChanged;
	}
	public boolean isShowOpenedChanged()
	{
		return showOpenedChanged;
	}	
	
	private boolean enabled = true;
	private boolean visible = true;
	private boolean required = false;
	private boolean enableLiveSearch = false;
	private int selection = -1;
	private ArrayList<Object> values = new ArrayList<Object>();
	private ArrayList<String> texts = new ArrayList<String>();
    private ArrayList<Image> images = new ArrayList<Image>();
    private ArrayList<Color> textColors = new ArrayList<Color>();
	private String editedText; 
	private String searchText; 
	private boolean showOpened = false;
	private String tooltip = null;
    
    private boolean enabledChanged = false;
    private boolean visibleChanged = false;
    private boolean requiredChanged = false;
    private boolean enableLiveSearchChanged = false;
    private boolean selectionChanged = false;
    private boolean valueChanged = false;
    private boolean editedTextChanged = false;
    private boolean searchTextChanged = false;
    private boolean showOpenedChanged = false;
    private boolean tooltipChanged = false;
}
