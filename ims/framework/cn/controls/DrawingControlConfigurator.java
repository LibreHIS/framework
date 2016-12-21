package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.DrawingControlConfiguratorData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.DrawingControlConfiguratorChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.DrawingControlArea;
import ims.framework.controls.DrawingControlGroup;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class DrawingControlConfigurator extends ims.framework.controls.DrawingControlConfigurator implements IVisualControl
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
	public void setReadOnly(boolean value)
	{
		super.setReadOnly(value);
		this.data.setReadOnly(value);
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
	public void setImage(Image image)
	{
		this.data.setImage(image);
	}
	public void setAreas(DrawingControlGroup root)
	{
		this.data.setRoot(root);
	}
	public DrawingControlGroup getAreas()
	{
		return this.data.getRoot();
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (DrawingControlConfiguratorData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		super.readOnly = this.data.isReadOnly();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		if(event instanceof DrawingControlConfiguratorChanged)
		{
			boolean wasChanged = this.data.wasChanged();
			
			this.data.setRoot(((DrawingControlConfiguratorChanged)event).getRoot());
			
			if(!wasChanged)
				this.data.markUnchanged();
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<drawingconfigcontrol id=\"a");
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
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		//if (this.data.getImage() == null)
		//	throw new ConfigurationException("Image is not supplied for the drawing configurator");
		//if (this.data.getRoot() == null)
		//	throw new ConfigurationException("The root group is not specified");
		
	    sb.append("<drawingconfigcontrol id=\"a");
		sb.append(super.id);		
		
		sb.append("\" visible=\"");
		sb.append(this.data.isVisible() ? "true" : "false");
		
		if(this.data.isVisible())
		{		
			if(!hasAnyParentDisabled())
			{
				sb.append("\" enabled=\"");
				sb.append(super.enabled ? "true" : "false");
			}
			
			sb.append("\" readOnly=\"");
			sb.append((this.data.isReadOnly() || this.data.getImage() == null) ? "true" : "false");
			if(this.data.getImage() != null)
			{
				sb.append("\" img=\"");
				sb.append(this.data.getImage().getImagePath());
			}
			sb.append("\">");
			if(this.data.getRoot() != null)
			{
				this.renderGroup(sb, this.data.getRoot());
			}
			sb.append("</drawingconfigcontrol>");
		}
		else
		{
			sb.append("\" />");
		}
	}	
	private void renderGroup(StringBuffer sb, DrawingControlGroup root)
	{
		sb.append("<group name=\"");
		sb.append(StringUtils.encodeXML(root.getName()));
		sb.append("\">");
		for (int i = 0; i < root.size(); ++i)
		{
			if (root.isGroup(i))
				this.renderGroup(sb, root.getGroup(i));
			else
			{
				DrawingControlArea area = root.getArea(i);
				sb.append("<area name=\"");
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
		return true;
	}
	public void markUnchanged() 
	{		
	}
	
	private DrawingControlConfiguratorData data;
}
