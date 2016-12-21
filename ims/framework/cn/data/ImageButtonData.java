package ims.framework.cn.data;

import ims.framework.utils.Image;

public class ImageButtonData implements IControlData
{	
	private static final long serialVersionUID = -44679309569172545L;
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
	public void setEnabledImage(Image value)
	{
		if(!this.enabledImageChanged)
		{
			if(this.enabledImage == null)
				this.enabledImageChanged = value != null;
			else
				this.enabledImageChanged = !this.enabledImage.equals(value);
		}
		
	    this.enabledImage = value;
	}
	public Image getEnabledImage()
	{
	    return this.enabledImage;
	}
	public void setDisabledImage(Image value)
	{
		if(!this.disabledImageChanged)
		{
			if(this.disabledImage == null)
				this.disabledImageChanged = value != null;
			else
				this.disabledImageChanged = !this.disabledImage.equals(value);
		}
		
	    this.disabledImage = value;
	}
	public Image getDisabledImage()
	{
	    return this.disabledImage;
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
	public void setTooltipChanged(boolean tooltipChanged)
	{
		this.tooltipChanged = tooltipChanged;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}
	public void setEnabledImageChanged(boolean enabledImageChanged)
	{
		this.enabledImageChanged = enabledImageChanged;
	}
	public boolean isEnabledImageChanged()
	{
		return enabledImageChanged;
	}
	public void setDisabledImageChanged(boolean disabledImageChanged)
	{
		this.disabledImageChanged = disabledImageChanged;
	}
	public boolean isDisabledImageChanged()
	{
		return disabledImageChanged;
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
	private String tooltip = null; 
	private Image enabledImage = null;
	private Image disabledImage = null;
	private boolean upload;
	
	private boolean enabledChanged = false;
	private boolean postbackRequirePdsAuthenticationChanged = false;
	private boolean visibleChanged = false;
	private boolean tooltipChanged = false; 
	private boolean enabledImageChanged = false;
	private boolean disabledImageChanged = false;
	private boolean uploadChanged = false;	
}
