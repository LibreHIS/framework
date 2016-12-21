package ims.framework.cn;

import java.util.ArrayList;

import ims.framework.MenuItem;
import ims.framework.cn.data.IControlData;
import ims.framework.exceptions.ConfigurationException;

public final class Menu extends ims.framework.Menu implements IControlData
{
	private static final long serialVersionUID = 8232183632200818563L;
	public Menu(int id)
	{
		this.id = id;
	}
	public int getID()
	{
		return this.id;
	}
	public void add(MenuItem item)
	{		
		this.menuItems.add(item);
	}
	public int count()
	{
		return this.menuItems.size();
	}
	public MenuItem get(int index)
	{
		return this.menuItems.get(index);
	}
	public void clear()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.menuItems.size() != 0;
		
		this.menuItems.clear();
	}
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(obj instanceof Menu)
			return this.id == ((Menu)obj).id;
		return false;
	}
	public int hashCode()
	{
		return super.hashCode();
	}
	public void free()
	{
		for(int x = 0; x < this.menuItems.size(); x++)
			this.menuItems.get(x).free();
		this.menuItems.clear();
	}	
	public void render(StringBuffer sb, boolean formIsReadOnly) throws ConfigurationException 
	{	
		boolean hasVisibleItems = false;
		for(int x = 0; x < this.menuItems.size(); x++)
		{
			MenuItem item = this.menuItems.get(x);			
			if(item.isVisible())
			{
				hasVisibleItems = true;
				break;
			}
		}
		
		sb.append("<menu id=\"");
		sb.append(this.id);
		if(!hasVisibleItems)
		{
			sb.append("\" />");
			return;
		}
		sb.append("\">");
		
		int index = 0;
		boolean shouldBeginAGroup = false;
			
		sb.append("<items>");
		for(int x = 0; x < this.menuItems.size(); x++)
		{
			MenuItem item = this.menuItems.get(x);
			if(item.isVisible())
			{
			    if((item.getBeginAGroup() && index > 0) || (shouldBeginAGroup && index > 0))
			        sb.append("<separator/>");			        
			    shouldBeginAGroup = false;			    
				item.render(sb, formIsReadOnly);
				index++;
			}
			else
			{
			    if(item.getBeginAGroup())
			        shouldBeginAGroup = true;			        
			}
		}		
		sb.append("</items>");		
		sb.append("</menu>");		
	}	
	public boolean wasChanged() 
	{
		if(this.dataWasChanged)
			return true;
		
		for(int x = 0; x < this.menuItems.size(); x++)
		{
			if(this.menuItems.get(x).wasChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{
		this.dataWasChanged = false;
		
		for(int x = 0; x < this.menuItems.size(); x++)
		{
			this.menuItems.get(x).markUnchanged();
		}		
	}
		
	private boolean dataWasChanged = false;
	private int id;
	private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();	
}
