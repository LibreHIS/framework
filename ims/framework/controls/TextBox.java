package ims.framework.controls;

import ims.framework.Control;
import ims.framework.cn.Menu;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.utils.Color;

public abstract class TextBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu, boolean required, CharacterCasing casing, String mask)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.required = required;
		this.casing = casing;
		this.mask = mask;
	}
	protected void free()
	{
		super.free();
		
		this.tooltip = null;
		this.valueChangedDelegate = null;
	}
	public void setValueChangedEvent(ValueChanged delegate)
    {
        this.valueChangedDelegate = delegate; 
    }
	
	abstract public String getValue();
	abstract public String getSelectedText();	
    abstract public void setValue(String value);
    abstract public void setTextColor(Color color);
    abstract public void setTooltip(String value);
    abstract public void setRequired(boolean value);
    abstract public boolean isRequired();
    
    protected ValueChanged valueChangedDelegate = null;
    protected String tooltip;
    protected boolean required;
    protected CharacterCasing casing = CharacterCasing.NORMAL;
    protected String mask = "";
}
