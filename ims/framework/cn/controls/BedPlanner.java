package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.BedPlannerData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.BedPlannerBedAttachedImageClicked;
import ims.framework.cn.events.BedPlannerNewBed;
import ims.framework.cn.events.BedPlannerBedClicked;
import ims.framework.cn.events.BedPlannerRemoveBed;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.Bed;
import ims.framework.controls.FloorPlannerArea;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class BedPlanner extends ims.framework.controls.BedPlanner implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);		
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
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
	public void setReadOnly(boolean value)
	{
		super.setReadOnly(value);
		this.data.setReadOnly(value);
	}
	public void clear()
	{
		setPlan("");
		this.data.clearBeds();
	}
	public void setPlan(String plan)
	{
		this.data.setPlan(plan);
	}
	public boolean canAddBeds()
	{
		return this.data.canAddBeds();
	}
	public void addArea(FloorPlannerArea area)
	{
		this.data.addArea(area);
	}
	public void addBedImage(Image image)
	{
		this.data.addBedImage(image);
	}
	public void addBed(Bed bed)
	{
		this.data.addBed(bed);
	}
	public ArrayList<Bed> getBeds()
	{
		return this.data.getBeds();		
	}
	public void clearBeds()
	{
		this.data.clearBeds();
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (BedPlannerData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		super.readOnly = this.data.isReadOnly();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{			
		if(event instanceof BedPlannerNewBed)
		{
			boolean wasChanged = this.data.wasChanged();	
			
			BedPlannerNewBed e = (BedPlannerNewBed)event;
			boolean bedAdded = false;
			Bed	bed = this.data.getBedById(e.getBedID());
			
			if(bed == null)
			{
				bedAdded = true;
				bed = new Bed(e.getBedID(), (Image)this.data.getBedImages().get(e.getBedType()), e.getPath(), e.getTextPosition(), e.getImagePosition());				
				this.data.addBed(bed);
			}
			else
			{				
				bed.setVML(e.getPath());
				bed.setImagePosition(e.getImagePosition());
				bed.setTextPosition(e.getTextPosition());
			}	
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			if(bedAdded)
			{
				if(super.newBedDelegate != null)
					super.newBedDelegate.handle(bed);
			}
			else
			{
				if(super.editBedDelegate != null)
					super.editBedDelegate.handle(bed);
			}
			
			return true;
		}
		else if(event instanceof BedPlannerRemoveBed)
		{
			boolean wasChanged = this.data.wasChanged();
			Bed bed = this.data.removeBed(((BedPlannerRemoveBed)event).getBedID());
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			if(super.removeBedDelegate != null)
				super.removeBedDelegate.handle(bed);
			
			return true;
		}
		else if(event instanceof BedPlannerBedClicked)
		{
			if(this.readOnly)
			{
				if(super.bedClickedDelegate != null)
					super.bedClickedDelegate.handle(this.data.getBedById(new Integer(((BedPlannerBedClicked)event).getBedID())));
			}
			else
			{
				if(super.bedInfoDelegate != null)
				{
					BedPlannerBedClicked castEvent = (BedPlannerBedClicked)event;
					super.bedInfoDelegate.handle(this.data.getBedById(new Integer(castEvent.getBedID())), castEvent.isReadOnly());
				}
			}
			
			return true;
		}				
		else if(event instanceof BedPlannerBedAttachedImageClicked)
		{
			if(super.bedAttachedImageClickedDelegate != null)
			{
				Bed bed = this.data.getBedById(new Integer(((BedPlannerBedAttachedImageClicked)event).getBedID()));
				bed.setClickedImageId(((BedPlannerBedAttachedImageClicked)event).getImageID());
				super.bedAttachedImageClickedDelegate.handle(this.data.getBedById(new Integer(((BedPlannerBedAttachedImageClicked)event).getBedID())));
			}
			
			return true;
		}		

		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
	    sb.append("<bedplanner id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"-1");
				
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\" autoPostBack=\"");
		sb.append("true");
		
		sb.append("\">");
		for (int i = 0; i < this.data.getAreas().size(); ++i)
		{
			FloorPlannerArea area = (FloorPlannerArea)this.data.getAreas().get(i);
			sb.append("<area id=\"");
			sb.append(area.getIndex());
			sb.append("\" name=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(area.getAreaName()));
			sb.append("\" path=\"");
			sb.append(area.getPath());
			sb.append("\"/>");
		}
		if(this.data.getBedImages().size() > 0)
		{
			for (int i = 0; i < this.data.getBedImages().size(); ++i)
			{			
				sb.append("<bed type=\"");
				sb.append(i);
			
				Image image = (Image)this.data.getBedImages().get(i);			
				if(image != null)
				{
					sb.append("\" img=\"");
					sb.append(image.getImagePath());
				}
				
				sb.append("\"/>");
			}
		}
		
		sb.append("<plan><![CDATA[");
		sb.append(this.data.getPlan());
		sb.append("]]></plan></bedplanner>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<bedplanner id=\"a");
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
			
			sb.append("\" canAddBed=\"");
			sb.append(this.data.canAddBeds() ? "true" : "false");
		
			sb.append("\" scrollPosition=\"0,0");
			sb.append("\" readOnly=\"");
			sb.append(this.data.isReadOnly() ? "true" : "false");
			sb.append("\" >");
			
			if(this.data.getBeds().size() > 0)
			{
				for(int x = 0; x < this.data.getBeds().size(); x++)
				{					
					Bed bed = this.data.getBeds().get(x);
					
					sb.append("<bed id=\"");
					sb.append(bed.getId());
					sb.append("\" number=\"");
					sb.append(bed.getNumber());
					sb.append("\" numberColor=\"");
					sb.append(bed.getNumberColor().toString());
					sb.append("\" canDelete=\"");
					sb.append(bed.canBeDeleted() ? "true" : "false");
					sb.append("\" text=\"");
					sb.append(bed.getText() == null ? "" : StringUtils.encodeXML(bed.getText()));					
					sb.append("\" textPos=\"");
					sb.append(bed.getTextPosition().toString());					
					sb.append("\" imgPos=\"");
					sb.append(bed.getImagePosition().toString());					
					sb.append("\" tooltip=\"");
					sb.append(bed.getTooltip() == null ? "" : StringUtils.encodeXML(bed.getTooltip()));
					
					sb.append("\"><vml><![CDATA[");
					
					if (bed.getType().getImagePath().startsWith("g/chair"))
						sb.append(bed.getVML().replace("g/chair-1.emz", bed.getColor().getType().getImagePath()));
					else
						sb.append(bed.getVML().replace("g/bed-1.emz", bed.getColor().getType().getImagePath()));
					
					sb.append("]]></vml>");
					
					if(bed.getAttachedImages() != null && bed.getAttachedImages().size() > 0)
					{
						for(int i = 0; i < bed.getAttachedImages().size(); i++)
						{
							sb.append("<image img=\"");
							Image img =  bed.getAttachedImages().get(i);
							sb.append(img.getImagePath());
							sb.append("\" tooltip=\"");
							sb.append(bed.getAttachedImagesTooltips().get(i) == null ? "" : StringUtils.encodeXML(bed.getAttachedImagesTooltips().get(i)));
							
							if (bed.getAttachedImagesIds().size() > 0 &&
									!(bed.getAttachedImagesIds().size() <= i) &&
										bed.getAttachedImagesIds().get(i) != -1)
							{								
								sb.append("\" id=\"");
								sb.append(bed.getAttachedImagesIds().get(i));
								
								if (bed.getAttachedImagesPostback().size() > 0 &&
										!(bed.getAttachedImagesPostback().size() <= i))
								{
									sb.append("\" autoPostBack=\"");
									sb.append(bed.getAttachedImagesPostback().get(i) ? "true" : "false");
								}								
							}
							
							sb.append("\" />");
						}
					}
					
					sb.append("</bed>");
				}
			}
			else
			{
				sb.append("<nobeds/>");
			}
			
			if(this.data.planWasChanged())
			{
				sb.append("<plan><![CDATA[");
				sb.append(this.data.getPlan());
				sb.append("]]></plan>");
				
				this.data.markPlanAsUnchanged();
			}
			
			sb.append("</bedplanner>");
		}
		else
		{
			sb.append("\" />");
		}
	}
	public boolean wasChanged() 
	{
		return this.data.wasChanged();
	}
	public void markUnchanged() 
	{
		this.data.markUnchanged();
	}
		
	private BedPlannerData data;
}