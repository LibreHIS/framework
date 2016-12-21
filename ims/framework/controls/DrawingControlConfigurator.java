package ims.framework.controls;

import java.io.Serializable;

import ims.framework.Control;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.utils.Image;

abstract public class DrawingControlConfigurator extends Control implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
	public void setReadOnly(boolean value)
	{
		if (this.form.getFormIsChangingMode() || (this.form.getMode().equals(FormMode.VIEW) && this.viewMode.equals(ControlState.UNKNOWN) || (this.form.getMode().equals(FormMode.EDIT) && this.editMode.equals(ControlState.UNKNOWN))))
			this.readOnly = value;
		else
			super.flagIlegalControlModeChange("ReadOnly", value);
	}
	
	abstract public void setImage(Image image);
	abstract public void setAreas(DrawingControlGroup root);
	abstract public DrawingControlGroup getAreas();
	
	protected boolean readOnly = true;
}
