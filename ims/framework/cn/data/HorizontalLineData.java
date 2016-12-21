package ims.framework.cn.data;

public class HorizontalLineData implements IControlData
{
	private static final long serialVersionUID = 8236541276591546945L;
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!visibleChanged)
			visibleChanged = this.visible != value;
		
		this.visible = value;
	}	
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}

	private boolean visible = true;	
	private boolean visibleChanged = false;
}
