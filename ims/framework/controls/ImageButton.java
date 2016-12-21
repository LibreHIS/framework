package ims.framework.controls;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.delegates.Click;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Image;

public abstract class ImageButton extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Image enabledImage, Image disabledImage, String tooltip, boolean isDefaultButton, boolean causesValidation, Menu menu)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.enabledImage = enabledImage;
		this.disabledImage = disabledImage;
		this.tooltip = tooltip;		
		this.isDefaultButton = isDefaultButton;
		this.causesValidation = causesValidation;
	}
	public abstract void setTooltip(String value);
	public abstract String getTooltip();
	public abstract void setEnabledImage(Image image);
	public abstract void setDisabledImage(Image image);
	public abstract void setUploadButton(boolean value);
    public abstract boolean isUploadButton();
    public abstract void setPostbackRequirePdsAuthentication(boolean value);
	public final void setAsDefaultButton()
    {
		if(this.defaultControlDelegate != null)
			this.defaultControlDelegate.handle(this);
    }
	protected void free() // free resources
	{
		super.free();
		this.enabledImage = null;
		this.disabledImage = null;
		this.clickDelegate = null;
	}
	public void setClickEvent(Click delegate)
	{
		this.clickDelegate = delegate;
	}

	protected boolean postbackRequirePdsAuthentication = false;
	protected Image enabledImage;
	protected Image disabledImage;
	protected Click clickDelegate = null;
	protected String tooltip;	
	protected boolean isDefaultButton = false;
	protected boolean causesValidation = true;

	
}
