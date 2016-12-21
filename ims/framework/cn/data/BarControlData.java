package ims.framework.cn.data;

import ims.framework.controls.BarControlResource;

import java.util.ArrayList;

public class BarControlData implements IControlData
{
	private static final long serialVersionUID = 376608968040585607L;
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
	public void addResource(BarControlResource resource)
	{		
		this.resourcesChanged = true;
		this.resources.add(resource);
	}
	public void clear()
	{
		if(!this.resourcesChanged)
			this.resourcesChanged = this.resources.size() != 0;
		
		this.resources.clear();
	}
	public ArrayList getResources()
	{
		return this.resources;
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
	public void setResourcesChanged(boolean resourcesChanged)
	{
		this.resourcesChanged = resourcesChanged;
	}
	public boolean isResourcesChanged()
	{
		return resourcesChanged;
	}

	private boolean enabled = true;
	private boolean visible = true;
	private ArrayList<BarControlResource> resources = new ArrayList<BarControlResource>();	
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean resourcesChanged = false;
}
