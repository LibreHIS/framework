package ims.framework.cn.controls;

import java.util.Iterator;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.ContainerData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.TabActivated;
import ims.framework.cn.events.TabCollapsedOrExpanded;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class Container extends ims.framework.Container implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	protected void free() // free resources
	{
		super.free();
		
		this.controls.clear();
		this.controls.trimToSize();
		this.caption = null;
		this.data = null;
	}
	public void setEnabled(boolean value)
	{
		super.enabled = value;
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.visible = value;
		this.data.setVisible(value);		
	}
	public void setHeaderEnabled(boolean value)
	{
		this.data.setHeaderEnabled(value);
	}
	public boolean isHeaderEnabled()
	{
		return this.data.isHeaderEnabled().booleanValue();
	}
	public void setHeaderVisible(boolean value)
	{
		this.data.setHeaderVisible(value);
	}
	public boolean isHeaderVisible()
	{
		return this.data.isHeaderVisible().booleanValue();
	}
	public void setCaption(String value)
	{
		super.caption = value;
		this.data.setCaption(value);
	}
	public String getCaption()
	{
		return super.caption;
	}
	public void setCollapsed(boolean value)
	{
		super.collapsed = value;
		this.data.setCollapsed(value);
	}
	public boolean isCollapsed()
	{
		return super.collapsed;
	}
    public void setScrollToTop(boolean value)
    {
        this.scrollTop = value;
    }    
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (ContainerData)data;

		if(this.data.isVisible() != null)
			super.visible = this.data.isVisible().booleanValue();
		if(this.data.isEnabled() != null)
			super.enabled = this.data.isEnabled().booleanValue();
		if(this.data.getCaption() != null)
			super.caption = this.data.getCaption();
		if(this.data.getCollapsed() != null)
			super.collapsed = this.data.getCollapsed().booleanValue(); 
		
		if(this.data.isEmpty()) 
		{
			for(int i = 0; i < super.controls.size(); ++i)
			{
				this.data.add(super.controls.get(i).getClass());
			}
		}
		for(int i = 0; i < super.controls.size(); ++i)
		{
			((IVisualControl)super.controls.get(i)).restore(this.data.getData(i), isNew);
		}
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof TabActivated)
		{			
			if(super.tabActivatedDelegate != null)
				super.tabActivatedDelegate.handle();
			
			data.setInitialized();
			
			return true;
		}
		else if(event instanceof TabCollapsedOrExpanded)
		{
			boolean wasChanged = data.isCollapsedChanged();
			data.setCollapsed(((TabCollapsedOrExpanded)event).isCollapsed());
			data.setCollapsedChanged(wasChanged);
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<container id=\"a");
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
				
		if(super.isInLayer)
		{
			sb.append("\" scrollable=\"false");
		}
		if(super.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");		
		}
		if(super.collapsable)
		{
			sb.append("\" collapsable=\"true");
		}
		if (super.caption != null)
		{
			sb.append("\" caption=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(super.caption));
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		if (super.groupID > -1)
		{
			sb.append("\" groupID=\"");
			sb.append(super.groupID);
		}
		
		sb.append("\">");
		Iterator li = super.getIterator();
		while (li.hasNext())
			 ((IVisualControl) li.next()).renderControl(sb);
		sb.append("</container>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<container id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{		
			sb.append("\" visible=\"");		
			sb.append(super.visible ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(super.collapsable)
		{
			if(!super.visible)
			{
				sb.append("\" collapsed=\"true");
			}
			else
			{
				if(data.isCollapsedChanged())
				{
					sb.append("\" collapsed=\"");
					sb.append(super.collapsed ? "true" : "false");
					data.setCollapsedChanged(false);
				}
			}
		}

		if(this.isVisible())
		{
			data.setInitialized();
		}
		
		if(this.visible || (this.isInLayer && this.layerHasTabs))
		{
			// has to be in both places
			if (super.caption != null)
			{
				if(data.isCaptionChanged())
				{
					sb.append("\" caption=\"");
					sb.append(ims.framework.utils.StringUtils.encodeXML(super.caption));
					data.setCaptionChanged(false);
				}
			}
			
			if(this.isInLayer && this.layerHasTabs)
			{
				if(data.isHeaderVisibleChanged())
				{
					sb.append("\" headerVisible=\"" + (this.isHeaderVisible() ? "true":"false"));
					data.setHeaderVisibleChanged(false);
				}
				
				if(this.isHeaderVisible())
				{
					if(data.isHeaderEnabledChanged())
					{
						sb.append("\" headerEnabled=\"" + (this.isHeaderEnabled() ? "true":"false"));
						data.setHeaderEnabledChanged(false);
					}
				}
			}
		}
				
		if(this.visible == true || (this.isInLayer && this.layerHasTabs && !this.autoPostBack))
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(super.enabled ? "true" : "false");
					data.setEnabledChanged(false);
				}
			}
			
			if(this.scrollTop)
	        {
	            sb.append("\" scrollTop=\"true");
	            this.scrollTop = false;
	        }
			
			sb.append("\" >");
			
			Iterator li = super.getIterator();
			while(li.hasNext())
			{
				Control control = (Control)li.next();
				if(control.wasChanged())
				{					
					((IVisualControl)control).renderData(sb);
					control.markUnchanged();
				}								
			}
			
			sb.append("</container>");		
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
			if(super.collapsable)
			{
				if(data.isCollapsedChanged())
					return true;
			}
		}
		
		if(visible || (isInLayer && layerHasTabs))		
		{
			if (super.caption != null)
			{
				if(data.isCaptionChanged())
					return true;
			}
			
			if(this.isInLayer && this.layerHasTabs)
			{
				if(data.isHeaderVisibleChanged())
					return true;
				if(data.isHeaderVisible() != null && data.isHeaderVisible().booleanValue() && data.isHeaderEnabledChanged())
					return true;
			}
		}
		
		if(visible || (this.isInLayer && this.layerHasTabs && !this.autoPostBack))
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
			}
			
			if(this.scrollTop)
				return true;
		
			Iterator li = super.getIterator();
			while(li.hasNext())
			{
				if(((Control)li.next()).wasChanged())
					return true;
			}
		}
		
		return false;
	}	
	public void markUnchanged() 
	{		
	}
	
	private ContainerData data;
    private boolean scrollTop = false;

	@Override
	public boolean isInitialized()
	{
		return data.isInitialized();
	}	
}
