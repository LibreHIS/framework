package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.Click;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

/**
 * @author mmihalec
 * 
 */
public abstract class RichTextControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	public final static String IdentityTag = "<!-- _ims_rich_text_control_1234567890_tag_ -->";
	
	abstract public String getValue();
	abstract public String getText();
    abstract public void setValue(String value);
    abstract public void addAutoCorrect(String fromText, String toText);
    abstract public void clearAutoCorrect();
    abstract public void addAutoGuess(String fromText, String toText);
    abstract public void clearAutoGuess();
    abstract public void setGlossaryVisible(boolean value);
    abstract public void pasteText(String value);
    abstract public void setRequired(boolean value);
    abstract public boolean isRequired();
    abstract public boolean spellCheck();
    
    protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean required, boolean simpleMode)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.required = required;
		this.simpleMode = simpleMode;
	}
	protected void free()
	{
		super.free();	
		
		this.valueChangedDelegate = null;
		this.glossaryClickDelegate = null;
	}	
	
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate; 
	}	
    public void setGlossaryClickEvent(Click delegate)
    {
        this.glossaryClickDelegate = delegate;        
    }	
    
    protected ValueChanged valueChangedDelegate = null;
    protected Click glossaryClickDelegate = null;
    protected boolean required;
    protected boolean simpleMode = false;
}
