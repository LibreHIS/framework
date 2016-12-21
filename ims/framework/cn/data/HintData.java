package ims.framework.cn.data;

public class HintData implements IControlData
{	
	private static final long serialVersionUID = -4327529720284373905L;
	public boolean isVisible()
	{
		if(tooltip == null)
			return false;
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
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
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		if(tooltip == null && visible)
		{
			visible = false;
			visibleChanged = true;
		}
		
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

	private boolean visible = true;
	private String tooltip = null;
	
	private boolean visibleChanged = false;
	private boolean tooltipChanged = false;
}
