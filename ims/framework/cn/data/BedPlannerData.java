package ims.framework.cn.data;

import ims.framework.controls.Bed;
import ims.framework.controls.FloorPlannerArea;
import ims.framework.utils.Image;

import java.util.ArrayList;

public class BedPlannerData extends ChangeableData implements IControlData
{
	private static final long serialVersionUID = -4438724745072189042L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
	public void setReadOnly(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.readOnly != value;
		
		this.readOnly = value;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	public String getPlan()
	{
		return this.plan;
	}
	public void setPlan(String value)
	{
		if(!this.planWasChanged)
		{
			if(this.plan == null)
				this.planWasChanged = value != null;
			else 
				this.planWasChanged = !this.plan.equals(value);
		}
		
		if(this.planWasChanged)
			this.dataWasChanged = true;
		
		this.plan = value;
	}
	public void addArea(FloorPlannerArea value)
	{
		this.dataWasChanged = true;
		this.areas.add(value);
	}
	public ArrayList getAreas()
	{
		return this.areas;
	}
	public void addBedImage(Image value)
	{
		this.dataWasChanged = true;
		this.bedImages.add(value);
	}
	public ArrayList getBedImages()
	{
		return this.bedImages;
	}
	public void addBed(Bed value)
	{
		this.dataWasChanged = true;
		this.beds.add(value);
	}
	public Bed removeBed(int id)
	{
		this.dataWasChanged = true;
		Bed bed = getBedById(id);		
		this.beds.remove(bed);
		return bed;
	}	
	public Bed getBedById(int id)
	{
		for(int x = 0; x < beds.size(); x++)
		{
			Bed bed = beds.get(x);
			if(bed.getId() == id)
				return bed;
		}
		
		return null;
	}
	public ArrayList<Bed> getBeds()
	{
		return this.beds;
	}
	public void clearBeds()
	{
		if(!this.dataWasChanged)
		{
			this.dataWasChanged = this.beds.size() > 0;
		}
		
		this.beds.clear();
	}
	public boolean planWasChanged()
	{
		return this.planWasChanged;
	}
	public void markPlanAsUnchanged()
	{
		this.planWasChanged = false;	
	}
	public boolean canAddBeds()
	{
		return canAddBeds;
	}
	public void setCanAddBeds(boolean value)
	{
		if(!this.dataWasChanged)
		{
			this.dataWasChanged = canAddBeds != value;
		}
		
		canAddBeds = value;
	}
	
	private boolean canAddBeds = true;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean readOnly = false;
	private String plan = "";
	private boolean planWasChanged = true;	
	private ArrayList<FloorPlannerArea> areas = new ArrayList<FloorPlannerArea>();
	private ArrayList<Image> bedImages = new ArrayList<Image>();
	private ArrayList<Bed> beds = new ArrayList<Bed>();
}