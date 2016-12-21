package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.ButtonData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.ButtonClicked;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class Button extends ims.framework.controls.Button implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, boolean canEditData, String tooltip, boolean isDefaultButton, boolean causesValidation, boolean upload, Menu menu, Color backgroundColor, Color textColor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, tooltip, isDefaultButton, causesValidation, menu, backgroundColor, textColor);
		
		this.tabIndex = tabIndex;
		this.text = text;		 
		this.canEditData = canEditData;
		this.upload = upload;
	}
	protected void free()
	{
		super.free();
		
		this.text = null;
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
	public void setTooltip(String value)
	{
		super.tooltip = value;
		this.data.setTooltip(value);		
	}	
	public void setBackgroundColor(Color value)
	{
		super.backgroundColor = value;
		this.data.setBackgroundColor(value);		
	}
	public void clearBackgroundColor()
	{
		super.backgroundColor = null;
		this.data.setBackgroundColor(null);		
	}
	public void setTextColor(Color value)
	{
		super.textColor = value;
		this.data.setTextColor(value);		
	}
	public void clearTextColor()
	{
		super.textColor = null;
		this.data.setTextColor(null);		
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
	public void setText(String value) 
	{
		this.data.setText(value);
		this.text = value;
	}
	public String getText()
	{
		return text;
	}	
	public void setImage(Image value)
	{
		this.data.setImage(value);
	}
	public void setPostbackRequirePdsAuthentication(boolean value)
	{
		 super.postbackRequirePdsAuthentication = value;
		 this.data.setPostbackRequirePdsAuthentication(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (ButtonData)data;
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
			this.data.setText(this.text);
			this.data.setTooltip(this.tooltip);
			this.data.setBackgroundColor(this.backgroundColor);
			this.data.setTextColor(this.textColor);
			this.data.setUploadButton(this.upload);
		}
		else
		{
			this.text = this.data.getText();
			this.tooltip = this.data.getTooltip();
			this.backgroundColor = this.data.getBackgroundColor();
			this.textColor = this.data.getTextColor();
			this.upload = this.data.isUploadButton();
		}		
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		checkIfControlCanFire(false);	
		
		if(event instanceof ButtonClicked)
		{
			if(super.clickDelegate != null)
			{
				super.clickDelegate.handle();
			}
						  
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<button id=\"a");
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
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{		
		sb.append("<button id=\"a");
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
					
					if(data.isPostbackRequirePdsAuthenticationChanged())
					{
						sb.append("\" postbackRequirePdsAuthentication=\"");
						sb.append(this.data.isPostbackRequirePdsAuthenticationChanged() ? "true" : "false");
						data.setPostbackRequirePdsAuthenticationChanged(false);
					}
				}
			}
			
			if(data.isImageChanged())
			{
				if(data.getImage() != null)
				{
					sb.append("\" img=\"");
					sb.append(data.getImage().getImagePath());				
					
					if(data.getImage().getImageWidth() != 12)
					{
						sb.append("\" imgWidth=\"");
						sb.append(data.getImage().getImageWidth());
					}
					
					if(data.getImage().getImageHeight() != 12)
					{
						sb.append("\" imgHeight=\"");
						sb.append(data.getImage().getImageHeight());
					}
				}
				else
				{
					sb.append("\" img=\"");
				}
				
				data.setImageChanged(false);
			}
				
			if(data.isTextChanged())
			{
				sb.append("\" value=\"");
				sb.append(this.text == null ? "" : ims.framework.utils.StringUtils.encodeXML(this.text));				
				data.setTextChanged(false);
			}
			
			if(data.isTextColorChanged())
			{
				sb.append("\" textColor=\"");
				sb.append(super.textColor == null ? "" : super.textColor);				
				data.setTextColorChanged(false);
			}
			
			if(data.isBackgroundColorChanged())
			{
				sb.append("\" backgroundColor=\"");
				sb.append(super.backgroundColor == null ? "" : super.backgroundColor);				
				data.setBackgroundColorChanged(false);
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
			
			if(data.isTextChanged())
				return true;	
			if(data.isBackgroundColorChanged())
				return true;
			if(data.isTextColorChanged())
				return true;
			if(data.isImageChanged())
				return true;
			if(data.isUploadChanged())
				return true;
			if(data.isPostbackRequirePdsAuthenticationChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private ButtonData data;
	private int tabIndex;
	private String text;
	private boolean canEditData;
	private boolean upload;
}