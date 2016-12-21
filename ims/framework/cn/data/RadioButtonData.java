package ims.framework.cn.data;

import java.util.ArrayList;

public class RadioButtonData extends ChangeableData implements IControlData
{
	private static final long serialVersionUID = 121352734564611853L;
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
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	public int getValue()
	{
		return this.value;
	}
	public void setValue(int value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.value != value;
		
		this.value = value;
	}
	public ArrayList<Boolean> getEnabledOptions()
	{
		return this.enabledOptions;
	}
	public ArrayList<Boolean> getVisibleOptions()
	{
		return this.visibleOptions;
	}
	public void markChanged()
	{
		this.dataWasChanged = true;
	}
	public void renameOption(int index, String name)
	{
		if(name == null)
			name = "";
		
		for(int x = 0; x < this.renamedOptions.size(); x++)
		{
			ItemRenamed item = this.renamedOptions.get(x);
			if(item.index == index)
			{
				if(!this.dataWasChanged)
				{
					if(item.name == null)
						this.dataWasChanged = true;
					else
						this.dataWasChanged = item.name.equals(name);
				}
				
				item.name = name;
				return;
			}
		}
		
		this.dataWasChanged = true;
		this.renamedOptions.add(new ItemRenamed(index, name));
	}
	public boolean hasNewName(int index)
	{
		for(int x = 0; x < this.renamedOptions.size(); x++)
		{
			ItemRenamed item = this.renamedOptions.get(x);
			if(item.index == index)
				return true;
		}
		
		return false;
	}
	public String getNewName(int index)
	{
		for(int x = 0; x < this.renamedOptions.size(); x++)
		{
			ItemRenamed item = this.renamedOptions.get(x);
			if(item.index == index)
				return item.name;
		}
		
		return null;
	}
	private boolean enabled = true;
	private boolean visible = true;
	private int value = -1;
	private ArrayList<Boolean> enabledOptions = new ArrayList<Boolean>();
	private ArrayList<Boolean> visibleOptions = new ArrayList<Boolean>();
	private ArrayList<ItemRenamed> renamedOptions = new ArrayList<ItemRenamed>();
	
	private class ItemRenamed
	{
		public ItemRenamed(int index, String name)
		{
			this.index = index;
			this.name = name;
		}
		
		public int index;
		public String name; 
	}
}