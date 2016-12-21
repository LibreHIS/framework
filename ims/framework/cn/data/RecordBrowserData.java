package ims.framework.cn.data;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

import java.util.ArrayList;

public class RecordBrowserData implements IControlData
{
	private static final long	serialVersionUID	= 846406440206413690L;
	
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
	public ArrayList getValues()
	{
		ArrayList<Object> result = new ArrayList<Object>();
		for (int i = 0; i < this.values.size(); ++i)
			result.add(this.values.get(i));
		return result;
	}
	public Color getTextColor(int index)
    {
        return this.textColors.get(index);
    }
	public Image getImage(int index)
    {
        return this.images.get(index);
    }
	public String getText(int index)
	{
		return this.texts.get(index);
	}
	public void clear()
	{
		if(!this.valuesChanged)
			this.valuesChanged = this.values.size() != 0;
		
		if(!this.selectionChanged)
			this.selectionChanged = this.selection != -1;
		
		this.selection = -1;
		this.values.clear();
		this.texts.clear();
		this.textColors.clear();
		this.images.clear();		
	}
	public boolean isEmpty()
	{
		return this.values.size() == 0;
	}
	public Object getValue(int index)
	{
		return this.values.get(index);
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
	}
	public void setSelection(Object value)
	{
		setSelection(this.values.indexOf(value));
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
		
        this.hasTooltip = true;
        this.tooltip = value;
    }
    public String getTooltip()
    {
        return this.tooltip;
    }
    public boolean hasTooltip()
    {
        return this.hasTooltip;
    }
    public void add(Object value, String text, Image image, Color textColor)
    {
    	add(-1, value, text, image, textColor);
    }
    public void add(int index, Object value, String text, Image image, Color textColor)
	{
		if (this.values.indexOf(value) != -1) 
			return;
		
		this.valuesChanged = true;
		
		if(index < 0)
		{
			this.values.add(value);
			this.texts.add(text);
	        this.images.add(image);
	        this.textColors.add(textColor);
		}
		else
		{
			this.values.add(index, value);
			this.texts.add(index, text);
	        this.images.add(index, image);
	        this.textColors.add(index, textColor);
		}	
        
        if(this.selection == -1)
        {
        	if(index >= 0)
        		this.selection = index;
        	else
        		this.selection = this.values.size() - 1;
        }
	}
    public int size()
	{
		return this.values.size();
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
	public void setSelectionChanged(boolean selectionChanged)
	{
		this.selectionChanged = selectionChanged;
	}
	public boolean isSelectionChanged()
	{
		return selectionChanged;
	}
	public void setValuesChanged(boolean valuesChanged)
	{
		this.valuesChanged = valuesChanged;
	}
	public boolean isValuesChanged()
	{
		return valuesChanged;
	}
	public void setTooltipChanged(boolean tooltioChanged)
	{
		this.tooltipChanged = tooltioChanged;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}

	private boolean enabled = true;
	private boolean visible = true;
	private int selection = -1;	
	private ArrayList<Object> values = new ArrayList<Object>();
	private ArrayList<String> texts = new ArrayList<String>();
    private ArrayList<Image> images = new ArrayList<Image>();
    private ArrayList<Color> textColors = new ArrayList<Color>();
    private String tooltip = null;
    private boolean hasTooltip = false; 
    
    private boolean enabledChanged = false;
    private boolean visibleChanged = false;
    private boolean selectionChanged = false;
    private boolean valuesChanged = false;
    private boolean tooltipChanged = true;
}
