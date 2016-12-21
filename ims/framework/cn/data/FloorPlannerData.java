package ims.framework.cn.data;

import ims.framework.controls.FloorPlannerArea;

import java.util.ArrayList;

public class FloorPlannerData extends ChangeableData implements IControlData
{ 
	private static final long serialVersionUID = 2863170284095032759L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public void setEnabled(boolean value)
	{
		this.enabled = value;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		this.visible = value;
	}
	public String getPlan()
	{
		return this.plan;
	}
	public void setPlan(String plan)
	{
		this.plan = plan;
	}
	public ArrayList<FloorPlannerArea> getAreas()
	{
		return this.areas;
	}
	public FloorPlannerArea getArea(int areaID)
	{
		for (int i = 0; i < this.areas.size(); ++i)
		{	
			FloorPlannerArea f = this.areas.get(i);
			if (f.getIndex() == areaID)
				return f;
		}
		return null;
	}
	public void addArea(FloorPlannerArea area)
	{
		for (int i = 0; i < this.areas.size(); ++i)
		{	
			FloorPlannerArea f = this.areas.get(i);
			if (f.getIndex() == area.getIndex())
			{
				this.areas.set(i, area);
				return;
			}
		}
		this.areas.add(area);
	}
	public void updateArea(FloorPlannerArea area)
	{
		for (int i = 0; i < this.areas.size(); ++i)
		{	
			FloorPlannerArea f = this.areas.get(i);
			if (f.getIndex() == area.getIndex())
			{
				this.areas.set(i, area);
				break;
			}
		}
	}
	public void removeArea(int areaID)
	{
		for (int i = 0; i < this.areas.size(); ++i)
		{	
			FloorPlannerArea f = this.areas.get(i);
			if (f.getIndex() == areaID)
			{
				this.areas.remove(i);
				break;
			}
		}
	}
	public void clearAreas()
	{
		this.areas.clear();
	}	
	
	private boolean enabled = true;
	private boolean visible = true;	
	private String plan = "";
	private ArrayList<FloorPlannerArea> areas = new ArrayList<FloorPlannerArea>();	
}
