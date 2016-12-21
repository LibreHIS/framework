package ims.framework.cn.events;

import ims.framework.Location;
import ims.framework.interfaces.ILocation;

import java.io.Serializable;
import java.util.ArrayList;

public class StoredLocationsEvent implements Serializable
{
	int selectionIndex = -1;
	ArrayList<Location> locations = new ArrayList<Location>();
	private static final long serialVersionUID = 1L;
	
	public StoredLocationsEvent(int selectionIndex)
	{		
		this.selectionIndex = selectionIndex;
	}	
	
	public void add(int id, String name)
	{
		locations.add(new Location(id, name));
	}
	public ILocation getSelectedLocation()
	{
		if(selectionIndex == -1)
			return null;
		else if(selectionIndex == -2)
			return locations.size() == 0 ? null : (ILocation)locations.get(0);
		else
			return locations.get(selectionIndex);
	}
	public ILocation[] getLocations()
	{
		ILocation[] result = new ILocation[locations.size()];
		for(int x = 0; x < locations.size(); x++)
		{
			result[x] = locations.get(x);
		}
		return result;
	}
}
