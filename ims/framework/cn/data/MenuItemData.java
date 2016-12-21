package ims.framework.cn.data;

import ims.framework.utils.Image;

/**
 * @author mmihalec
 * 
 */
public class MenuItemData extends ChangeableData implements IControlData 
{
	private static final long serialVersionUID = -5133906877524501400L;
	public String getText()
	{
		return this.text;
	}
	public void setText(String value)
	{
		if(!this.dataWasChanged)
		{
			if(this.text == null)
				this.dataWasChanged = value != null;
			else
				this.dataWasChanged = !this.text.equals(value);
		}
		
		this.text = value;
	}
	public boolean getEnabled()
	{
		return this.enabled;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public boolean getVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	public Image getIcon()
	{
		return this.icon;
	}
	public void setIcon(Image value)
	{
		if(!this.dataWasChanged)
		{
			if(this.icon == null)
				this.dataWasChanged = value != null;
			else
				this.dataWasChanged = !this.icon.equals(value);
		}
		
		this.icon = value;
	}
	
	private String text;
	private boolean enabled = true;
	private boolean visible;
	private Image icon;	
}
