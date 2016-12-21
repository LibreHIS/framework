package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.ImageButtonData;
import ims.framework.cn.events.ButtonClicked;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class ImageButton extends ims.framework.controls.ImageButton implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Image enabledImage, Image disabledImage, String toolTip, boolean canEditData, boolean isDefaultButton, boolean causesValidation, boolean upload, Menu menu)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, enabledImage, disabledImage, toolTip, isDefaultButton, causesValidation, menu);		
		this.tabIndex = tabIndex;
		this.canEditData = canEditData;
		this.upload = upload;
	}
	public void setTooltip(String value)
	{
		super.tooltip = value;
		this.data.setTooltip(value);
	}
	public String getTooltip()
	{
		return super.tooltip;
	}
	protected void free() // free resources
	{
		super.free();
		
		this.tooltip = null;
		this.data = null;
	}
	public void setUploadButton(boolean value)
	{
		this.upload = value;
		this.data.setUploadButton(value);
	}
    public boolean isUploadButton()
    {
    	return upload;
    }
	public void setEnabledImage(Image image)
	{
	    super.enabledImage = image;
	    this.data.setEnabledImage(image);
	}
	public void setDisabledImage(Image image)
	{
	    super.disabledImage = image;
	    this.data.setDisabledImage(image);
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
	public void setPostbackRequirePdsAuthentication(boolean value)
	{
		 super.postbackRequirePdsAuthentication = value;
		 this.data.setPostbackRequirePdsAuthentication(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (ImageButtonData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		//FWUI-1711 - default button doesn't work on a form with layer		
		//************************************************************
		//** Layer with 2 tabs, default button is on second tab.
		//** DefaultButton property was sent only first time (isNew)		
		//** Now will be send every time.
		//************************************************************
		if(isDefaultButton)
			setAsDefaultButton();
		
		if(isNew)
		{
			this.data.setTooltip(super.tooltip);
			this.data.setEnabledImage(super.enabledImage);
			this.data.setDisabledImage(super.disabledImage);
			this.data.setUploadButton(this.upload);
		}
		else
		{
			super.tooltip = this.data.getTooltip();
			super.enabledImage = this.data.getEnabledImage(); 
			super.disabledImage = this.data.getDisabledImage();
			this.upload = this.data.isUploadButton();			
		}
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof ButtonClicked)
		{
			checkIfControlCanFire(false);
			
			if(super.clickDelegate != null)
			{
				super.clickDelegate.handle();
			}
			
			return true;		
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
	    sb.append("<imagebutton id=\"a");
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
		
		if(!super.causesValidation)
		{
			sb.append("\" validator=\"false");
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		if(super.menu != null)
		{
			sb.append("\" dropDownMenuID=\"");
			sb.append(super.menu.getID());
		}
				
		sb.append("\" srcE=\"");
		sb.append(super.enabledImage.getImagePath());
		sb.append("\" srcD=\"");
		sb.append(super.disabledImage.getImagePath());
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
	    sb.append("<imagebutton id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{				
				if (this.form.isReadOnly() && this.canEditData)
				{
					sb.append("\" enabled=\"");
					sb.append("false");
					
					sb.append("\" tooltip=\"");
					sb.append(StringUtils.encodeXML("<b>Disabled</b><br>The Form is Read Only"));
				}
				else
				{
					if(data.isEnabledChanged())
					{
						sb.append("\" enabled=\"");
						sb.append(this.data.isEnabled() ? "true" : "false");
						data.setEnabledChanged(false);
					}
					
					if(data.isTooltipChanged())
					{
						sb.append("\" tooltip=\"");
						sb.append(super.tooltip == null ? "" : StringUtils.encodeXML(super.tooltip));
						data.setTooltipChanged(false);
					}
				}	
			}
		
			if(data.isEnabledImageChanged())
			{
				sb.append("\" srcE=\"");
				sb.append(super.enabledImage == null ? "" : super.enabledImage.getImagePath());
				data.setEnabledImageChanged(false);
			}
			
			if(data.isDisabledImageChanged())
			{
				sb.append("\" srcD=\"");
				sb.append(super.disabledImage == null ? "" : super.disabledImage.getImagePath());
				data.setDisabledImageChanged(false);
			}
			
			if(data.isUploadChanged())
			{
				sb.append("\" uploads=\"");
				sb.append(upload ? "true" : "false");
				data.setUploadChanged(false);
			}
			
			if(data.isPostbackRequirePdsAuthenticationChanged())
			{
				sb.append("\" postbackRequirePdsAuthentication=\"");
				sb.append(this.data.isPostbackRequirePdsAuthenticationChanged() ? "true" : "false");
				data.setPostbackRequirePdsAuthenticationChanged(false);
			}
		}
		
		sb.append("\" />");
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
				if(data.isTooltipChanged())
					return true;
			}
			
			if(data.isEnabledImageChanged())
				return true;
			if(data.isDisabledImageChanged())
				return true;
			if(data.isUploadChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private ImageButtonData data;
	private int tabIndex;
	private boolean canEditData;
	private boolean upload;
}
