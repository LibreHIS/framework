package ims.framework;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.delegates.DefaultControl;
import ims.framework.delegates.ControlFocused;

/**
 * This is a base class for all controls in the framework.
 * <p>
 * Every control has an ID that grater or equal to 1000 (the first version of the framework was implemented in C++, so I got this limitation from there).
 * <p>
 * It is assumed that every control has a rectangular form thus every control has the coordinate of the left-top corner and also width and height.
 * <p>
 * When form is changing its state all controls are changing their states as well. Control state is defined through Form Editor and can not be changed on runtime.
 * @author Mikhail Chashchin
 * @version 1.0
 */
public abstract class Control implements IControlComparable, IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	public int getID()
	{
		return this.id;
	}
	public void setFocus()
	{
		if(hasAnyParentHidden() || !isVisible())
			return;
		if(this.controlFocusedDelegate != null)
			this.controlFocusedDelegate.handle(this);
	}
	public void setControlFocusedEvent(ControlFocused delegate)
	{
	    this.controlFocusedDelegate = delegate;
	}
	public void setDefaultControlEvent(DefaultControl delegate)
	{
	    this.defaultControlDelegate = delegate;
	}
	/**
	 * Returns true if control is enabled.
	 */
	public boolean isEnabled()
	{
		return this.enabled;
	}
	/**
	 * Ebable or disable control.
	 * An exception will be thrown if this method is called from any "Logic" class and control state is not set to "Unknown" in Form Editor
	 * <p>
	 * Some controls may override this method.
	 * @param value if true - enable control, otherwise - disable it
	 */
	public void setEnabled(boolean value)
	{
		if (this.form.getFormIsChangingMode() || (this.form.getMode().equals(FormMode.VIEW) && this.viewMode.equals(ControlState.UNKNOWN) || (this.form.getMode().equals(FormMode.EDIT) && this.editMode.equals(ControlState.UNKNOWN))))
			this.enabled = value;
		else
			flagIlegalControlModeChange("Enabled", value);
	}
	public final boolean hasAnyParentDisabled()
	{
		return false; // QUICK FIX FOR FWUI-1655 - jsCN should render disabled all the containers that ARE disabled!
		/*
		if(parentControl == null)
			return false;
		if(!parentControl.isEnabled())
			return true;
		return parentControl.hasAnyParentDisabled();*/
	}
	public final boolean hasAnyParentHidden()
	{
		if(parentControl == null)
			return false;
		if(!parentControl.isVisible())
			return true;
		return parentControl.hasAnyParentHidden();
	}
	/**
	 * Returns true if control is visible.
	 */
	public final boolean isVisible()
	{
		return this.visible;
	}
	/**
	 * Hide or show control.
	 * An exception will be thrown if this method is called from any "Logic" class and control state is not set to "Unknown" in Form Editor
	 * <p>
	 * All controls must override this method.
	 * @param value if true - show control, otherwise - hide it
	 */
	public void setVisible(boolean value)
	{
		if (this.form.getFormIsChangingMode() || (this.form.getMode().equals(FormMode.VIEW) && this.viewMode.equals(ControlState.UNKNOWN) || (this.form.getMode().equals(FormMode.EDIT) && this.editMode.equals(ControlState.UNKNOWN))))
			this.visible = value;
		else
			flagIlegalControlModeChange("Visible", value);
	}

	/**
	 * When application starts a pool of controls is created. 
	 * Controls are initially detached from any form and do not have any state, not event ID.
	 * During a form initialization controls are taken from the pool and attached to the form. 
	 * After that controls are populated with the form specific information.
	 * <p>
	 * This method is used by UIFactory.
	 * <p>
	 * All controls may override this method and set additional state information.
	 * @param id ID of the control
	 * @param x	X coordinate
	 * @param y Y coordinate
	 * @param width Width of the control
	 * @param height Height of the control
	 * @param viewMode Control state in "View" mode
	 * @param editMode Control state in "Edit" mode
	 */
	protected void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		this.parentControl = parentControl;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.viewMode = viewMode;
		this.editMode = editMode;
		this.anchor = anchor; 
	}
	protected void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu)
	{
		setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.menu = menu;
	}

	/**
	 * Destroy link between form and control and set control to its initial state.
	 * <p>
	 * This method is used by FormFlyweightFactory when form is about to be put back to the pool.
	 * <p>
	 * All controls must override this method and free resources that were in use.
	 */
	protected void free() // free resources
	{
		this.form = null;
		this.enabled = true;
		this.visible = true;
		this.controlFocusedDelegate = null;
		this.defaultControlDelegate = null;
		this.menu = null;
	}

	ControlState getViewMode() // used by FormDef.FormModeChanged
	{
		return this.viewMode;
	}

	ControlState getEditMode() // used by FormDef.FormModeChanged
	{
		return this.editMode;
	}
	ControlAnchoring getAnchor()
	{
		return this.anchor;
	}

	protected void flagIlegalControlModeChange(String type, boolean value)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Control.set");
		sb.append(type);
		sb.append('(');
		sb.append(value);
		sb.append("): can't change control state. FormMode = ");
		sb.append(this.form.getMode().toString());
		sb.append(", Control state = ");
		sb.append(this.form.getMode().equals(FormMode.VIEW) ? this.viewMode.toString() : this.editMode.toString());		
		
		if (Boolean.FALSE.equals(ims.configuration.ConfigFlag.GEN.RELEASE_MODE.getValue()))
			throw new RuntimeException(sb.toString());
	}
	
	public boolean equals(Object compare)
	{
		if(compare == null)
			return false;
		if(compare instanceof IControlComparable)
			return ((IControlComparable)compare).getID() == this.getID();
		return false;
	}
	
	public int hashCode()
	{
		return this.getID();
	}
	
	public boolean controlCanFire(boolean ignoreParent)
	{
		if(ignoreParent)
			return this.enabled && this.visible;
		return this.enabled && this.visible && !this.hasAnyParentDisabled() && !this.hasAnyParentHidden();
	}
	public void checkIfControlCanFire()
	{
		checkIfControlCanFire(true);
	}
	public void checkIfControlCanFire(boolean ignoreParent)
	{		
		//if(!controlCanFire(ignoreParent))
		//	throw new RuntimeException("The " + this.toString() + "  control (ID: " + this.id + ") in not accessible, event will not be fired");
	}
	public Control getParentControl()
	{
		return parentControl;
	}
	public int getDepthLevel()
	{
		if(parentControl == null)
			return 1;
		return parentControl.getDepthLevel() + 1;
	}

	protected boolean enabled = true, visible = true;
	protected int id, x, y, width, height;
	protected ControlState viewMode, editMode;
	protected Form form;
	protected ControlAnchoring anchor = ControlAnchoring.TOPLEFT;
	protected ControlFocused controlFocusedDelegate;
	protected DefaultControl defaultControlDelegate;
	
	protected Menu menu;
	protected Control parentControl;
}
