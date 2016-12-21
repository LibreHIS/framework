package ims.framework.cn.data;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

public class ButtonData implements IControlData
{
	private static final long serialVersionUID = 7444790299462985686L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public boolean isPostbackRequirePdsAuthentication()
	{
		return this.postbackRequirePdsAuthentication;
	}
	public void setEnabled(boolean value)
	{	
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;				
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;		
	}
	public void setPostbackRequirePdsAuthentication(boolean value)
	{
		if(!this.postbackRequirePdsAuthenticationChanged)
			this.postbackRequirePdsAuthenticationChanged = this.postbackRequirePdsAuthentication != value;
		
		this.postbackRequirePdsAuthentication = value;		
	}
	public String getText() 
	{
		return this.text;
	}
	public void setText(String value) 
	{		
		if(!this.textChanged)
		{
			if(this.text == null)
				this.textChanged = value != null;
			else 
				this.textChanged = !this.text.equals(value);
		}
		
		this.text = value;		
	}
	public Color getTextColor() 
	{
		return this.textColor;
	}
	public void setTextColor(Color value) 
	{		
		if(!this.textColorChanged)
		{
			if(this.textColor == null)
				this.textColorChanged = value != null;
			else 
				this.textColorChanged = !this.textColor.equals(value);
		}
		
		this.textColor = value;		
	}
	public Color getBackgroundColor() 
	{
		return this.backgroundColor;
	}
	public void setBackgroundColor(Color value) 
	{		
		if(!this.backgroundColorChanged)
		{
			if(this.backgroundColor == null)
				this.backgroundColorChanged = value != null;
			else 
				this.backgroundColorChanged = !this.backgroundColor.equals(value);
		}
		
		this.backgroundColor = value;		
	}
	public void setTooltip(String value)
	{
		if(!this.tooltipChanged)
		{
			if(this.tooltip == null)
				this.tooltipChanged = value != null;
			else
				this.tooltipChanged = !this.tooltip.equals(value);
		}
		
		this.tooltip = value;		
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public void setImage(Image value)
	{
		if(!this.imageChanged)
		{
			if(this.image == null)
				this.imageChanged = value != null;
			else
				this.imageChanged = !this.image.equals(value);
		}
		
		this.image = value;	
	}
	public Image getImage()
	{
		return this.image;		
	}	
	public void setUploadButton(boolean value)
	{
		upload = value;		
	}
    public boolean isUploadButton()
    {
    	return upload;
    }
    public void setPostbackRequirePdsAuthenticationChanged(boolean postbackRequirePdsAuthenticationChanged)
	{
		this.postbackRequirePdsAuthenticationChanged = postbackRequirePdsAuthenticationChanged;
	}
	public boolean isPostbackRequirePdsAuthenticationChanged()
	{
		return postbackRequirePdsAuthenticationChanged;
	}
	public void setEnabledChanged(boolean enabledChanged)
	{
		this.enabledChanged = enabledChanged;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setTextChanged(boolean textChanged)
	{
		this.textChanged = textChanged;
	}
	public boolean isTextChanged()
	{
		return textChanged;
	}
	public void setTextColorChanged(boolean textColorChanged)
	{
		this.textColorChanged = textColorChanged;
	}
	public boolean isTextColorChanged()
	{
		return textColorChanged;
	}
	public void setBackgroundColorChanged(boolean backgroundColorChanged)
	{
		this.backgroundColorChanged = backgroundColorChanged;
	}
	public boolean isBackgroundColorChanged()
	{
		return this.backgroundColorChanged;
	}
	public void setTooltipChanged(boolean tooltipChanged)
	{
		this.tooltipChanged = tooltipChanged;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}
	public void setImageChanged(boolean imageChanged)
	{
		this.imageChanged = imageChanged;
	}
	public boolean isImageChanged()
	{
		return imageChanged;
	}
	public void setUploadChanged(boolean uploadChanged)
	{
		this.uploadChanged = uploadChanged;
	}
	public boolean isUploadChanged()
	{
		return uploadChanged;
	}
		
	private boolean enabled = true;
	private boolean visible = true;
	private boolean postbackRequirePdsAuthentication = false;
	private String text = null;
	private Color textColor = null;
	private Color backgroundColor = null;
	private String tooltip = null;	
	private Image image;
	private boolean upload;
	
	private boolean postbackRequirePdsAuthenticationChanged = false;
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean textChanged = false;
	private boolean textColorChanged = false;
	private boolean backgroundColorChanged = false;
	private boolean tooltipChanged = false;
	private boolean imageChanged = false;
	private boolean uploadChanged = true;	
}
