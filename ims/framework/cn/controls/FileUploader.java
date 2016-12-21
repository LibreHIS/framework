package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.FileUploaderData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.FileSelected;
import ims.framework.cn.events.FileUploaded;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;

public class FileUploader extends ims.framework.controls.FileUploader implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean autoPostBack, boolean uploadOnSelection, boolean showOverwrite)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
		this.uploadOnSelection = uploadOnSelection;
		this.showOverwrite = showOverwrite;		
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
	public void setShowOverwrite(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (FileUploaderData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();		
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		if(event instanceof FileUploaded)
		{
			FileUploaded typedEvent = (FileUploaded)event;
			if(this.fileUploadedDelegate != null)
				this.fileUploadedDelegate.handle(typedEvent.getFileName());
			
			return true;
		}
		else if(event instanceof FileSelected)
		{
			FileSelected typedEvent = (FileSelected)event;
			if(this.fileSelectedDelegate != null)
				this.fileSelectedDelegate.handle(typedEvent.getFileName());
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<uploadbox id=\"a");
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
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
			
			if(this.uploadOnSelection)
			{
				sb.append("\" uploadOnSelection=\"true");
			}
		}
		
		if(this.showOverwrite)
		{
			sb.append("\" showOverwrite=\"true");						
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
		sb.append("<uploadbox id=\"a");
		sb.append(super.id);
		
		if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledUnchanged();
				}
			}
		}
		
		if(this.showOverwrite)
		{
			if(this.data.isOverwriteOnUploadChanged())
			{
				sb.append("\" overwriteOnUpload=\"");
				sb.append(this.data.isOverwriteOnUpload() ? "true" : "false");
				this.data.setOverwriteOnUploadUnchanged();
			}
			
			if(this.data.isEnabledOverwriteOnUploadChanged())
			{
				sb.append("\" enabledOverwriteOnUpload=\"");
				sb.append(this.data.isEnabledOverwriteOnUpload() ? "true" : "false");
				this.data.setEnabledOverwriteOnUploadUnchanged();
			}
		}
			
		sb.append("\" />");
	}
	public boolean wasChanged() 
	{
		if(this.data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					return true;
				}
			}
		}
		
//		if (this.data.isOverwriteOnUploadChanged())
//			return true;
		
//		if (this.data.isEnabledOverwriteOnUploadChanged())
//			return true;
			
		return false;
	}
	public void markUnchanged() 
	{
	}
	
	private FileUploaderData data;
	private int tabIndex;
	private boolean autoPostBack = false;
	private boolean uploadOnSelection = false;
	private boolean showOverwrite = false;	
	
	public void setOverwriteOnUpload(boolean value) 
	{		
		this.data.setOverwriteOnUpload(value);		
	}	
	public void setEnabledOverwriteOnUpload(boolean value) 
	{		
		this.data.setEnabledOverwriteOnUpload(value);		
	}	
}