package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.HTMLViewerData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.HTMLViewerClick;
import ims.framework.cn.events.HTMLViewerResized;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.SizeInfo;
import ims.framework.utils.TextileString;

public class HTMLViewer extends ims.framework.controls.HTMLViewer implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean keepSizeInfo, boolean hasBorder, boolean shadow)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);		
		this.tabIndex = tabIndex;
		this.keepSizeInfo = keepSizeInfo;
		this.hasBorder = hasBorder;
		this.shadow = shadow;
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
	public void setIFrameValue(String url)
	{
		this.data.setHTML("<iframe src=\"" + url + "\" style=\"width: 100%; height: 100%; border: 0; \"></iframe>");		
	}
	public void setHTML(String value)
	{
	    this.data.setHTML(value);        
	}
	public void setHTML(String value, boolean isIFrame)
	{
		this.data.setEmbeddedObject(isIFrame);
	    this.data.setHTML(value);        
	}
	public void setHTML(TextileString value)
	{
	    this.data.setHTML(value);        
	}
	public SizeInfo getCurrentSize() 
	{
		if(keepSizeInfo)
			return data.getCurrentSize() == null ? new SizeInfo(width, height) : data.getCurrentSize();
		return null;
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (HTMLViewerData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof HTMLViewerClick)
		{
			if(super.delegate != null)
				super.delegate.handle(((HTMLViewerClick)event).getValue());
			
			return true;
		}
		else if(event instanceof HTMLViewerResized)
		{
			HTMLViewerResized typedEvent = (HTMLViewerResized)event;			
			data.setCurrentSize(new SizeInfo(typedEvent.getWidth(), typedEvent.getHeight()));
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<htmlviewer id=\"a");
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
		
		if(shadow)
		{
			sb.append("\" shadow=\"true");
		}
		if(keepSizeInfo)
		{
			sb.append("\" sendSizeInfo=\"true");
		}
		if(!hasBorder)
		{
			sb.append("\" border=\"false");
		}

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<htmlviewer id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleUnchanged();
		}
		
		if(data.isEmbeddedObjectChanged())
		{
			sb.append("\" embeddedObject=\"");
			sb.append(this.data.isEmbeddedObject() ? "true" : "false");
			data.setEmbeddedObjectUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					data.setEnabledUnchanged();
				}
			}
			
			if(data.isValueChanged())
			{
				sb.append("\">");
				
				if(this.data.getHTML() == null || this.data.getHTML().length() == 0)
			    {
			        sb.append("<value/>");
			    }
				else 
				{
				    sb.append("<value><![CDATA[");
					sb.append(this.data.getHTML());
					sb.append("]]></value>");
				}
				
				sb.append("</htmlviewer>");
				
				data.setValueUnchanged();
			}
			else
			{
				sb.append("\" />");
			}			
		}
		else
		{
			sb.append("\" />");
		}
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
			}
			
			if(data.isValueChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private HTMLViewerData data;
	private int tabIndex;
	private boolean keepSizeInfo;
	private boolean hasBorder = true;
	private boolean shadow = true;
}
