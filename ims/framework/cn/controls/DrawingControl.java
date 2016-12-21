package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.DrawingControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.DrawingControlNote;
import ims.framework.cn.events.DrawingControlShapeRemoved;
import ims.framework.cn.events.DrawingControlShapeEdited;
import ims.framework.cn.events.DrawingControlVMLChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.DrawingControlArea;
import ims.framework.controls.DrawingControlGroup;
import ims.framework.controls.DrawingControlGroupCollection;
import ims.framework.controls.DrawingControlAreaCollection;
import ims.framework.controls.DrawingControlShape;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class DrawingControl extends ims.framework.controls.DrawingControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean allowMultiAreaDrawing)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.allowMultiAreaDrawing = allowMultiAreaDrawing;
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
	public void setImage(Image image)
	{
		this.data.setImage(image);
	}
	public void addBrush(int id, String caption, Image image)
	{
		addBrush(id, caption, image, true, null);
	}
	public void addBrush(int id, String caption, Image image, boolean multipleMarkings)
	{
		addBrush(id, caption, image, multipleMarkings, null);
	}
	public void addBrush(int id, String caption, Image image, String tooltip)
	{
		addBrush(id, caption, image, true, tooltip);
	}
	public void addBrush(int id, String caption, Image image, boolean multipleMarkings, String tooltip)
	{
		this.data.addBrush(id, caption, image, multipleMarkings, tooltip);
	}	
	public void addBrush(int id, String caption, Color color)
	{
		addBrush(id, caption, color, true, null);
	}
	public void addBrush(int id, String caption, Color color, boolean multipleMarkings)
	{
		addBrush(id, caption, color, multipleMarkings, null);
	}
	public void addBrush(int id, String caption, Color color, String tooltip)
	{
		addBrush(id, caption, color, true, tooltip);
	}
	public void addBrush(int id, String caption, Color color, boolean multipleMarkings, String tooltip)
	{
		this.data.addBrush(id, caption, color, multipleMarkings, tooltip);
	}
	public void clearBrushes()
	{
		this.data.clearBrushes();
	}
	public void setAreas(DrawingControlGroup root)
	{
		this.data.setRoot(root);
	}
	public DrawingControlGroup getAreas()
	{
		return this.data.getRoot();
	}
	public void addShape(DrawingControlShape shape)
	{
		this.data.addShape(shape);
	}
	public ArrayList getShapes()
	{
		return this.data.getShapes();
	}
	public void clearShapes()
	{
		this.data.clearShapes();
	}
	public String getGroupOrAreaName(int targetID)
	{
		DrawingControlGroup root = this.data.getRoot();
		if(root.getID() == targetID)
			return root.getName();
		
		DrawingControlGroupCollection groups = root.getGroups(); 
		for(int x = 0; x < groups.size(); x++)
		{
			DrawingControlGroup group = groups.get(x);
			if(group.getID() == targetID)
				return group.getName();
		}
		
		DrawingControlAreaCollection areas = root.getAllAreas(); 
		for(int x = 0; x < areas.size(); x++)
		{
			DrawingControlArea area = areas.get(x);
			if(area.getID() == targetID)
				return area.getName();
		}
		
		return null;
	}
	public boolean isGroup(int targetID)
	{
		DrawingControlGroup root = this.data.getRoot();
		if(root.getID() == targetID)
			return true;
		
		DrawingControlGroupCollection groups = root.getGroups(); 
		for(int x = 0; x < groups.size(); x++)
		{
			DrawingControlGroup group = groups.get(x);
			if(group.getID() == targetID)
				return true;
		}	
		
		return false;
	}
	public void setPrintTitle(String printTitle)
	{
		this.data.setPrintTitle(printTitle);
	}
	public void setPrintSubTitle(String printSubTitle)
	{
		this.data.setPrintSubTitle(printSubTitle);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (DrawingControlData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		super.readOnly = this.data.isReadOnly();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		boolean wasChanged = this.data.wasChanged();
		if(event instanceof DrawingControlVMLChanged)
		{			
			this.data.addShape(((DrawingControlVMLChanged)event).getShape());
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			return true;
		}
		else if(event instanceof DrawingControlShapeRemoved)
		{
			int index = ((DrawingControlShapeRemoved)event).getIndex();
			this.data.removeShape(index);
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			if(super.drawingControlShapeRemovedDelegate != null)
				super.drawingControlShapeRemovedDelegate.handle(index);
			
			return true;
		}
		else if(event instanceof DrawingControlShapeEdited)
		{
			if(super.drawingControlShapeEditedDelegate != null)
				super.drawingControlShapeEditedDelegate.handle(((DrawingControlShapeEdited)event).getIndex());
			
			return true;
		}
		else if(event instanceof DrawingControlNote)
		{
			if(super.drawingControlNoteDelegate != null)
				super.drawingControlNoteDelegate.handle(this.data.getShape(((DrawingControlNote)event).getIndex()));
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<drawingcontrol id=\"a");
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
		if(this.allowMultiAreaDrawing)
		{
			sb.append("\" allowMultiAreaDrawing=\"true");
		}


		sb.append("\"/>");		
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		if (this.data.getImage() == null)
			throw new ConfigurationException("Image is not supplied for the drawing control");
		if ((this.data.getImageBrushes().length + this.data.getColorBrushes().length) == 0)
			throw new ConfigurationException("Brushes are not supplied for the drawing control");
		if (this.data.getRoot() == null)
			throw new ConfigurationException("The root group is not specified");
		
	    sb.append("<drawingcontrol id=\"a");
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
				
			if(this.data.getImage() != null)
			{
				sb.append("\" img=\"");
				sb.append(this.data.getImage().getImagePath());
			}
			
			sb.append("\" readOnly=\"");
			sb.append(this.data.isReadOnly() ? "true" : "false");
			sb.append("\" printTitle=\"");
			sb.append(StringUtils.encodeXML(this.data.getPrintTitle()));
			sb.append("\" printSubTitle=\"");
			sb.append(StringUtils.encodeXML(this.data.getPrintSubTitle()));
			sb.append("\">");
			
			if(this.data.getImageBrushes().length == 0)
			{
				sb.append("<brush/>");
			}
			else
			{
				for (int i = 0; i < this.data.getImageBrushes().length; ++i)
				{					
					sb.append("<brush id=\"" + this.data.getImageBrushes()[i].getID() + "\" caption=\""  + StringUtils.encodeXML(this.data.getImageBrushes()[i].getCaption()) + "\" src=\"");			
					sb.append(this.data.getImageBrushes()[i].getImage().getImagePath());
					if(this.data.getImageBrushes()[i].getTooltip() != null)
					{
						sb.append("\" tooltip=\"");
						sb.append(StringUtils.encodeXML(this.data.getImageBrushes()[i].getTooltip()));						
					}
					if(!this.data.getImageBrushes()[i].allowsMultipleMarkings())
					{
						sb.append("\" multiDraw=\"false");
					}
					sb.append("\"/>");
				}
			}
	
			if(this.data.getColorBrushes().length == 0)
			{
				sb.append("<color/>");
			}
			else
			{
				for (int i = 0; i < this.data.getColorBrushes().length; ++i)
				{
					sb.append("<color id=\"" + this.data.getColorBrushes()[i].getID() + "\" caption=\""  + StringUtils.encodeXML(this.data.getColorBrushes()[i].getCaption()) + "\" value=\"");
					sb.append(this.data.getColorBrushes()[i].getColor().toString());
					if(this.data.getColorBrushes()[i].getTooltip() != null)
					{
						sb.append("\" tooltip=\"");
						sb.append(StringUtils.encodeXML(this.data.getColorBrushes()[i].getTooltip()));						
					}
					if(!this.data.getColorBrushes()[i].allowsMultipleMarkings())
					{
						sb.append("\" multiDraw=\"false");
					}
					sb.append("\"/>");
				}
			}
			
			this.renderGroup(sb, this.data.getRoot());
			
			ArrayList shapes = this.data.getShapes();
			if (shapes.size() == 0)
				sb.append("<noshapes/>");
			else
			{
				for (int i = 0; i < shapes.size(); ++i)
				{
					DrawingControlShape d = (DrawingControlShape)shapes.get(i);
					sb.append("<shape targetID=\"");
					sb.append(d.getTargetID());
					sb.append("\" brushID=\"");
					sb.append(d.getBrushID());
					sb.append("\" readOnly=\"");
					sb.append(d.getReadOnly() ? "true" : "false");
					sb.append("\" id=\"");
					sb.append(d.getIndex());	
					if(this.data.isReadOnly() && this.data.isVisible())
					{
						if(d.getTooltip() != null && d.getTooltip().length() > 0)
						{
							sb.append("\" tooltip=\"");
							sb.append(StringUtils.encodeXML(d.getTooltip()));
						}
					}
					sb.append("\"><![CDATA[");
					sb.append(d.getVML());
					sb.append("]]></shape>");
				}
			}
					
			sb.append("</drawingcontrol>");
		}
		else
		{
			sb.append("\" />");
		}
	}
	private void renderGroup(StringBuffer sb, DrawingControlGroup root)
	{
		sb.append("<group id=\"" + root.getID() + "\" name=\"");
		sb.append(StringUtils.encodeXML(root.getName()));
		sb.append("\">");
		for (int i = 0; i < root.size(); ++i)
		{
			if (root.isGroup(i))
				this.renderGroup(sb, root.getGroup(i));
			else
			{
				DrawingControlArea area = root.getArea(i);
				sb.append("<area id=\"" + area.getID() + "\" name=\"");
				sb.append(StringUtils.encodeXML(area.getName()));
				sb.append("\" path=\"");
				sb.append(area.getPath());
				sb.append("\"/>");
			}
		}
		sb.append("</group>");
	}
	public boolean wasChanged() 
	{
		return this.data.wasChanged();
	}
	public void markUnchanged() 
	{
		this.data.markUnchanged();
	}
	
	private DrawingControlData data;	
	private boolean allowMultiAreaDrawing = true;
}