package ims.framework.cn.data;

import ims.framework.controls.DrawingControlGroup;
import ims.framework.utils.Image;

public class DrawingControlConfiguratorData extends ChangeableData implements IControlData
{
	private static final long serialVersionUID = -3668185426821912423L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setReadOnly(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.readOnly != value;
		
		this.readOnly = value;
	}
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
	public DrawingControlGroup getRoot()
	{
		return this.root;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setRoot(DrawingControlGroup value)
	{
		if(!this.dataWasChanged)
		{
			if(this.root == null)
				this.dataWasChanged = value != null;
			else
				this.dataWasChanged = !this.root.equals(value);
		}
		
		this.root = value;
	}
	public Image getImage()
	{
		return this.image;
	}
	public void setImage(Image value)
	{
		if(!this.dataWasChanged)
		{
			if(this.image == null)
				this.dataWasChanged = value != null;
			else
				this.dataWasChanged = !this.image.equals(value);
		}
		
		this.image = value;
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	private DrawingControlGroup root = null;
	private Image image = null;
	private boolean readOnly = false;
}
