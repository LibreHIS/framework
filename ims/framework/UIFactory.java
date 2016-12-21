package ims.framework;

import java.io.Serializable;

import ims.framework.controls.Timer;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

abstract public class UIFactory  implements Serializable
{
	private static final long serialVersionUID = 1L;
    abstract public UIEngine getUIEngine();
	abstract public Form getForm();
	abstract public Control getControl(Class controlClass, Object[] params);
	abstract public Control getControl(Class controlClass, Form parentForm, Object[] params);
	abstract public Menu createMenu(int id);
	abstract public Timer createTimer(int id, int interval, boolean enabled);
	abstract public MenuItem createMenuItem(int id, String text, boolean enabled, boolean visible, Integer imageID, boolean canEditData, boolean beginAGroup);
	abstract public CustomComponent getEmptyCustomComponent();
	protected final void setContainerContext(Control parentControl, Container container, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String caption, int groupID, boolean visible, boolean autoPostBack, boolean isInLayer, boolean layerHasTabs, boolean layerHasAutoPostBack, boolean collapsable)
	{
		container.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, caption, groupID, visible, autoPostBack, isInLayer, layerHasTabs, layerHasAutoPostBack, collapsable);
	}
}