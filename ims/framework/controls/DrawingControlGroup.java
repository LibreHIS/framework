package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;

public class DrawingControlGroup implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlGroup(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	public int getID()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void add(DrawingControlGroup group)
	{
		this.col.add(group);
	}
	public void add(DrawingControlArea area)
	{
		this.col.add(area);
	}
	public int size()
	{
		return this.col.size();
	}
	public boolean isGroup(int index)
	{
		return this.col.get(index) instanceof DrawingControlGroup;
	}
	public DrawingControlGroup getGroup(int index)
	{
		return (DrawingControlGroup)this.col.get(index);
	}
	public DrawingControlArea getArea(int index)
	{
		return (DrawingControlArea)this.col.get(index);
	}
	
	public DrawingControlGroupCollection getGroups()
	{
		ArrayList<DrawingControlGroup> groups = new ArrayList<DrawingControlGroup>();
		for (int i = 0; i < this.col.size(); i++)
		{
			if (isGroup(i))
			{
				groups.add((DrawingControlGroup)this.col.get(i));
			}
		}
		return new DrawingControlGroupCollection(groups);
	}
	
	public DrawingControlAreaCollection getAreas()
	{
		ArrayList<DrawingControlArea> areas = new ArrayList<DrawingControlArea>();
		for (int i = 0; i < this.col.size(); i++)
		{
			if (!isGroup(i))
			{
				areas.add((DrawingControlArea)this.col.get(i));
			}
		}
		return new DrawingControlAreaCollection(areas);		
	}

	public DrawingControlAreaCollection getAllAreas()
	{
		DrawingControlAreaCollection areas = getAreas();
		DrawingControlGroupCollection groups = getGroups();
		getAllAreas(groups, areas);
		return areas;
	}
	
	private DrawingControlAreaCollection getAllAreas(DrawingControlGroupCollection groups, DrawingControlAreaCollection areas)
	{
		for (int i = 0; i < groups.size(); i++)
		{
			DrawingControlGroup group = groups.get(i);
			areas.addAll(group.getAreas());
			DrawingControlGroupCollection childGroups = group.getGroups();
			getAllAreas(childGroups, areas);
		}		
		return areas;
	}
	
	private int id;
	private String name;
	private ArrayList<Object> col = new ArrayList<Object>();
}
