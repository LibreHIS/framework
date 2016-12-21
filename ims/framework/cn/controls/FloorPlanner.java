package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.FloorPlannerData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.FloorPlannerNewArea;
import ims.framework.cn.events.FloorPlannerNote;
import ims.framework.cn.events.FloorPlannerPlanChanged;
import ims.framework.cn.events.FloorPlannerRemoveArea;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.FloorPlannerArea;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class FloorPlanner extends ims.framework.controls.FloorPlanner implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean areasEnabled)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.tabIndex = tabIndex;
		this.areasEnabled = areasEnabled;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
		this.noChangesToPlan = true;
		this.noChangesToAreas = true;
	}
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public String getPlan()
	{
		return this.data.getPlan();
	}
	public void setPlan(String value)
	{
		this.noChangesToPlan = false;
		this.data.setPlan(value);
	}
	public ArrayList<FloorPlannerArea> getAreas()
	{
		return this.data.getAreas();		
	}
	public void addArea(FloorPlannerArea area)
	{
		this.noChangesToAreas = false;
		this.data.addArea(area);
	}
	public void clearAreas()
	{
		this.noChangesToAreas = false;
		this.data.clearAreas();
	}
	public void updateArea(FloorPlannerArea area)
	{
		this.noChangesToAreas = false;
		this.data.updateArea(area);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (FloorPlannerData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof FloorPlannerPlanChanged)
		{
			boolean wasChanged = this.data.wasChanged();
			
			FloorPlannerPlanChanged e = (FloorPlannerPlanChanged)event;
			this.data.setPlan(e.getShape());
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			return true;
		}
		else if(event instanceof FloorPlannerNote)
		{
			FloorPlannerNote e = (FloorPlannerNote)event;
			FloorPlannerArea area = this.data.getArea(e.getAreaID());
			
			if(super.delegate != null)
				super.delegate.handle(area);
			
			return true;
		}
		else if(event instanceof FloorPlannerNewArea)
		{
			boolean wasChanged = this.data.wasChanged();
			
			FloorPlannerNewArea e = (FloorPlannerNewArea)event;
			this.data.addArea(new FloorPlannerArea(e.getAreaID(), "", e.getPath()));
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			return true;
		}
		else if(event instanceof FloorPlannerRemoveArea)
		{
			boolean wasChanged = this.data.wasChanged();
			
			FloorPlannerRemoveArea e = (FloorPlannerRemoveArea)event;
			this.data.removeArea(e.getAreaID());
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<floorplanner id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		sb.append("\" areasEnabled=\"");
		sb.append(this.areasEnabled ? "true" : "false");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<floorplanner id=\"a");
		sb.append(super.id);
		sb.append("\" visible=\"");
		sb.append(this.data.isVisible() ? "true" : "false");
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				sb.append("\" enabled=\"");
				sb.append(this.data.isEnabled() ? "true" : "false");
			}
				
			sb.append("\" scrollPosition=\"0,0");
			
			sb.append("\">");
			int len = this.data.getAreas().size();
			if (len == 0)
				sb.append("<noareas/>");
			else if (!this.noChangesToAreas)
			{	
				for (int i = 0; i < len; ++i)
				{
					FloorPlannerArea area = this.data.getAreas().get(i);
					sb.append("<area id=\"");
					sb.append(area.getIndex());
					sb.append("\" name=\"");
					sb.append(ims.framework.utils.StringUtils.encodeXML(area.getAreaName()));
					sb.append("\" path=\"");
					sb.append(area.getPath());
					sb.append("\"/>");
				}
			}
			if (this.data.getPlan() == "")
				sb.append("<noplan/>");
			else if (!this.noChangesToPlan)
			{	
				sb.append("<plan><![CDATA[");
				sb.append(this.data.getPlan());
				sb.append("]]></plan>");
			}
			sb.append("</floorplanner>");
		}
		else
		{
			sb.append("\" />");
		}
	}

	// Not implemented
	public boolean wasChanged() 
	{
		return true;
	}
	// Not implemented
	public void markUnchanged() 
	{
	}
	
	private FloorPlannerData data;
	private boolean noChangesToPlan = true;
	private boolean noChangesToAreas = true;
	private int tabIndex;
	private boolean areasEnabled = false;
}