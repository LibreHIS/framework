package ims.framework.cn.data;

import java.util.ArrayList;

public class SearchableComboBoxData implements IControlData
{
	private static final long serialVersionUID = -6487659225982270827L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public String getCriteria()
	{
		return this.criteria;
	}
	public void setCriteria(String value)
	{
		if(!this.criteriaChanged)
		{
			if(this.criteria == null)
				this.criteriaChanged = value != null;
			else 
				this.criteriaChanged = !this.criteria.equals(value);
		}
		
		this.criteria = value;
	}
	public int getSelection()
	{
		return this.selection;
	}
	public void setSelection(int index)
	{
		if(!this.selectionChanged)
			this.selectionChanged = this.selection == index;
		
		this.selection = index;
	}
	public void clearOptions()
	{
		if(!this.selectionChanged)
			this.selectionChanged = this.selection != -1;		
		this.selection = -1;
		
		if(!this.optionsChanged)
			this.optionsChanged = this.values.size() > 0;
			
		this.values.clear();
		this.texts.clear();
	}
	public void add(Object value, String text)
	{		
		this.optionsChanged = true;
		this.values.add(value);
		this.texts.add(text);
	}
	public int size()
	{
		return this.values.size();
	}
	public Object getValue(int index)
	{
		return this.values.get(index);
	}
	public String getText(int index)
	{
		return this.texts.get(index);
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
	public void setOptionsChanged(boolean optionsChanged)
	{
		this.optionsChanged = optionsChanged;
	}
	public boolean isOptionsChanged()
	{
		return optionsChanged;
	}
	public void setCriteriaChanged(boolean criteriaChanged)
	{
		this.criteriaChanged = criteriaChanged;
	}
	public boolean isCriteriaChanged()
	{
		return criteriaChanged;
	}
	
	

	private boolean enabled = true;
	private boolean visible = true;
	private int selection = -1;
	private ArrayList<Object> values = new ArrayList<Object>();
	private ArrayList<String> texts = new ArrayList<String>();
	private String criteria = "";
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean selectionChanged = false;
	private boolean optionsChanged = false;
	private boolean criteriaChanged = false;
}
