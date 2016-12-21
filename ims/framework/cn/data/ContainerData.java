package ims.framework.cn.data;

public class ContainerData extends FormData implements IControlData
{	
	private static final long serialVersionUID = -1245962676414816476L;
	
	public Boolean isEnabled()
	{
		return this.enabled;
	}
	public Boolean isVisible()
	{
		return this.visible;
	}
	public Boolean isHeaderEnabled()
	{
		if(this.headerEnabled == null)
			return Boolean.TRUE;
		return this.headerEnabled;
	}
	public Boolean isHeaderVisible()
	{
		if(this.headerVisible == null)
			return Boolean.TRUE;
		return this.headerVisible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
		{
			if(this.enabled == null)
				this.enabledChanged = true;
			else
				this.enabledChanged = this.enabled.booleanValue() != value;
		}
		
		this.enabled = new Boolean(value);
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
		{
			if(this.visible == null)
				this.visibleChanged = true;
			else
				this.visibleChanged = this.visible.booleanValue() != value;
		}
		
		this.visible = new Boolean(value);
	}
	public void setHeaderEnabled(boolean value)
	{
		if(!this.headerEnabledChanged)
		{
			if(this.headerEnabled == null)
				this.headerEnabledChanged = true;
			else
				this.headerEnabledChanged = this.headerEnabled.booleanValue() != value;
		}
		
		this.headerEnabled = new Boolean(value);	
	}
	public void setHeaderVisible(boolean value)
	{
		if(!this.headerVisibleChanged)
		{
			if(this.headerVisible == null)
				this.headerVisibleChanged = true;
			else
				this.headerVisibleChanged = this.headerVisible.booleanValue() != value;
		}
		
		this.headerVisible = new Boolean(value);	
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
	public String getCaption()
	{
		return this.caption;
	}
	public void setCollapsed(boolean value)
	{
		if(!this.collapsedChanged)
		{
			if(this.collapsed == null)
				this.collapsedChanged = true;
			else
				this.collapsedChanged = this.collapsed.booleanValue() != value;
		}
		
		this.collapsed = new Boolean(value);
	}
	public Boolean getCollapsed()
	{
		return this.collapsed;
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
	public void setHeaderEnabledChanged(boolean headerEnabledChanged)
	{
		this.headerEnabledChanged = headerEnabledChanged;
	}
	public boolean isHeaderEnabledChanged()
	{
		return headerEnabledChanged;
	}
	public void setHeaderVisibleChanged(boolean headerVisibleChanged)
	{
		this.headerVisibleChanged = headerVisibleChanged;
	}
	public boolean isHeaderVisibleChanged()
	{
		return headerVisibleChanged;
	}
	public void setCaptionChanged(boolean captionChanged)
	{
		this.captionChanged = captionChanged;
	}
	public boolean isCaptionChanged()
	{
		return captionChanged;
	}
	public void setCollapsedChanged(boolean collapsedChanged)
	{
		this.collapsedChanged = collapsedChanged;
	}
	public boolean isCollapsedChanged()
	{
		return collapsedChanged;
	}
	public void setInitialized()
	{
		initialized = true;		
	}	
	public boolean isInitialized()
	{
		return initialized;
	}
		
	private Boolean enabled;
	private Boolean visible;
	private Boolean headerEnabled;
	private Boolean headerVisible;
	private String caption;
	private Boolean collapsed;
	
	private boolean enabledChanged = true;
	private boolean visibleChanged = true;
	private boolean headerEnabledChanged = true;
	private boolean headerVisibleChanged = true;
	private boolean captionChanged = true;
	private boolean collapsedChanged = true;
	private boolean initialized = false;	
}
