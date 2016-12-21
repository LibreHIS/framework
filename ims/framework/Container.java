package ims.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import ims.framework.delegates.TabActivated;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

abstract public class Container extends Control implements IControlContainer, Serializable
{
	private static final long serialVersionUID = 1L;
	
	final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String caption, int groupID, boolean visible, boolean autoPostBack, boolean isInLayer, boolean layerHasTabs, boolean layerHasAutoPostBack, boolean collapsable)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.caption = caption;
		
		if(layerHasTabs)
		{
			this.groupID = groupID;
		}
		
		this.visible = visible;
		this.enabled = true;		
		this.autoPostBack = autoPostBack;
		this.isInLayer = isInLayer;
		this.layerHasTabs = layerHasTabs;
		this.layerHasAutoPostBack = layerHasAutoPostBack; 
		this.collapsable = collapsable; 
	}
	protected void free()
	{
		super.free();
		
		this.caption = null;
		this.controls.clear();
		this.controls.trimToSize();		
		this.tabActivatedDelegate = null;
	}	
	public void setTabActivatedEvent(ims.framework.delegates.TabActivated delegate)
	{
		this.tabActivatedDelegate = delegate;        
	}
	public final Iterator<Control> getIterator()
	{
		return this.controls.iterator();
	}	
	protected void addControl(Control control)
	{
		this.controls.add(control);
		control.form = this.form;
	}
	
	public abstract void setHeaderEnabled(boolean value);
	public abstract boolean isHeaderEnabled();
	public abstract void setHeaderVisible(boolean value);
	public abstract boolean isHeaderVisible();
	public abstract void setCaption(String value);
	public abstract String getCaption();
    public abstract void setScrollToTop(boolean value);
    public abstract void setCollapsed(boolean value);
    public abstract boolean isCollapsed();
    public abstract boolean isInitialized();		

	protected String caption;
	protected int groupID = -1;
	protected ArrayList<Control> controls = new ArrayList<Control>();
	protected TabActivated tabActivatedDelegate = null;
	protected boolean autoPostBack = false;
	protected boolean isInLayer = false;
	protected boolean layerHasTabs = false;
	protected boolean layerHasAutoPostBack = false;
	protected boolean collapsable = true;
	protected boolean collapsed = false;
	protected boolean initialized = false;
}