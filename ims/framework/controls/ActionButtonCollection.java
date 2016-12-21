package ims.framework.controls;

import ims.base.interfaces.IModifiable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public final class ActionButtonCollection implements Serializable, IModifiable
{
	private static final long serialVersionUID = 1L;
	private boolean wasChanged = true;
	private static ActionButtonCollection wizardActionButtons;
	
	private String name;	
	private ArrayList<ActionButton> list = new ArrayList<ActionButton>();
	
	public synchronized static ActionButtonCollection getWizard()
	{
		if(wizardActionButtons == null)
			wizardActionButtons = new ActionButtonCollection("actionBar");
		
		wizardActionButtons.add(new ActionButton(1, "Previous"));
		wizardActionButtons.add(new ActionButton(2, "Next"));
		wizardActionButtons.add(new ActionButton(3, "Finish"));
		wizardActionButtons.add(new ActionButton(4, "Cancel"));
		
		return wizardActionButtons;
	}
	
	public ActionButtonCollection(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	public boolean add(ActionButton actionButton)
	{
		if(list.contains(actionButton))
			return false;
		
		wasChanged = true;
		
		return list.add(actionButton);
	}
	public ActionButton get(int index)
	{
		return list.get(index);
	}
	public int size()
	{
		return list.size();
	}
	public ActionButton remove(int index)
	{
		ActionButton result = list.remove(index);
		
		if(result != null)
			wasChanged = true;
		
		return result;
	}
	public boolean remove(ActionButton actionButton)
	{
		boolean result = list.remove(actionButton);
		
		if(result)
			wasChanged = true;
		
		return result;
	}
	public boolean contains(ActionButton actionButton)
	{
		return list.contains(actionButton);
	}
	public void clear()
	{
		if(list.size() > 0)
			wasChanged = true;
		
		list.clear();
		list.trimToSize();
	}
	public void markUnchanged()
	{
		wasChanged = false;
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			((ActionButton)iterator.next()).markUnchanged();				
		}		
	}
	public boolean wasChanged()
	{
		if(wasChanged)
			return true;
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			if(((ActionButton)iterator.next()).wasChanged())
				return true;
		}
		
		return false;
	}
}
