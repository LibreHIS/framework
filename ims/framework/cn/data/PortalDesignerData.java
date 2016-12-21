package ims.framework.cn.data;


public class PortalDesignerData implements IControlData
{
	private static final long serialVersionUID = 7444790293462985686L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
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
	public String getCaption() 
	{
		return this.caption;
	}
	public void setCaption(String value) 
	{		
		if(!this.captionChanged)
		{
			if(this.caption == null)
				this.captionChanged = value != null;
			else 
				this.captionChanged = !this.caption.equals(value);
		}
		
		this.caption = value;		
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
	public void setCaptionChanged(boolean captionChanged)
	{
		this.captionChanged = captionChanged;
	}
	public boolean isCaptionChanged()
	{
		return captionChanged;
	}
	public void setTooltipChanged(boolean tooltipChanged)
	{
		this.tooltipChanged = tooltipChanged;
	}
	public boolean isTooltipChanged()
	{
		return tooltipChanged;
	}
		
	private boolean enabled = true;
	private boolean visible = true;
	private String caption = null;
	private String tooltip = null;	
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean captionChanged = false;
	private boolean tooltipChanged = false;
}
