package ims.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import ims.framework.controls.Timer;
import ims.framework.delegates.CustomEvent;
import ims.framework.delegates.DefaultControl;
import ims.framework.delegates.ControlFocused;
import ims.framework.delegates.DynamicFormModeChanged;
import ims.framework.delegates.FormClosing;
import ims.framework.delegates.FormDialogClosed;
import ims.framework.delegates.FormModeChanged;
import ims.framework.delegates.FormOpen;
import ims.framework.delegates.MessageBoxClosed;
import ims.framework.delegates.RIEDialogClosed;
import ims.framework.delegates.RIEDialogOpened;
import ims.framework.delegates.TimerElapsed;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.FormMode;
import ims.framework.exceptions.CodingRuntimeException;

public abstract class Form implements IControlContainer, Serializable
{	
	private static final long serialVersionUID = 1L;
	private String uniqueIdentifier;
	
	public String getUniqueIdentifier()
	{
		return uniqueIdentifier;
	}
	public void setUniqueIdentifier(String value)
	{
		uniqueIdentifier = value;
	}
	
	public abstract void setFormInfo(ims.framework.FormInfo formInfo);
	public abstract ims.framework.FormInfo getFormInfo();
	
	public int getWidth()
	{
		return this.width;
	}
	public void setWidth(int value)
	{
		this.width = value;
	}
	public int getHeight()
	{
		return this.height;
	}
	public void setHeight(int value)
	{
		this.height = value;
	}
	public int getDarkHeight() 
	{
		return this.darkHeight;
	}
	public void setDarkHeight(int darkHeight) 
	{
		this.darkHeight = darkHeight;
	}
	public FormMode getMode()
	{
		return this.formMode;
	}
	public boolean canChangeMode(FormMode mode)
    {
    	if(isReadOnly() && mode == FormMode.EDIT)
    		return false;
    	return true;
    }
	public boolean setMode(FormMode value, boolean fireEvent)
	{		
		if(!canChangeMode(value))
			return false;
		
		this.formMode = value;
        if (fireEvent)
        {
        	if(value == null)
    			throw new CodingRuntimeException("Invalid form mode");
        	
        	if(this.formModeChangedDelegate != null)
        	{
        		this.formModeChangedDelegate.handle();
        	}        
        	if(this.formModeChangedToComponentDelegate != null)
        	{
        		this.formModeChangedToComponentDelegate.handle();
        	}
        	if(this.formModeChangedToDynamicFormDelegate != null)
        	{
        		this.formModeChangedToDynamicFormDelegate.handle(value);
        	}
        }
        		
        return true;
	}

	void addControl(Control control)
	{
		this.controls.add(control);
		control.form = this;
	}
	
	public final Iterator<Control> getIterator()
	{
		return this.controls.iterator();
	}

	ArrayList<Control> getControls()
	{
		return this.controls;
	}

	public void setRIEDialogOpenedEvent(RIEDialogOpened delegate)
	{
		this.rieDialogOpenedDelegate = delegate;
	}
	public void setRIEDialogClosedEvent(RIEDialogClosed delegate)
	{
		this.rieDialogClosedDelegate = delegate;
	}	
	public void setFormOpenEvent(FormOpen delegate)
	{
		this.formOpenDelegate = delegate;
	}
	public void setFormModeChangedEvent(FormModeChanged delegate)
	{
		this.formModeChangedDelegate = delegate;
	}
	public void setFormModeChangedToComponentEvent(FormModeChanged delegate)
	{
		this.formModeChangedToComponentDelegate = delegate;
	}
	public void setFormModeChangedToDynamicFormEvent(DynamicFormModeChanged delegate)
	{
		this.formModeChangedToDynamicFormDelegate = delegate;
	}
	public void setFormDialogClosedEvent(FormDialogClosed delegate)
	{
		this.formDialogClosedDelegate = delegate;
	}
	public void setMessageBoxClosedEvent(MessageBoxClosed delegate)
	{
		this.messageBoxClosedDelegate = delegate;
	}
	public void setFormClosingEvent(FormClosing delegate)
	{
		this.formClosingDelegate = delegate;
	}
	public void setFormValueChangedEvent(ValueChanged delegate)
	{
		this.formValueChangedDelegate = delegate;
	}
	public void setControlFocusedEvent(ControlFocused delegate)
	{
	    this.controlFocusedDelegate = delegate;
	}
	public void setDefaultControlEvent(DefaultControl delegate)
	{
	    this.defaultControlDelegate = delegate;
	}
	public void setTimerElapsedEvent(TimerElapsed delegate)
	{
	    this.timerElapsedDelegate = delegate;
	}
	public void setCustomEventEvent(CustomEvent delegate)
	{
		this.customEventDelegate = delegate;
	}
	protected int getControlID(Control c)
	{
		return c.id;
	}
	protected void free(Control c)
	{		
		c.free();
	}
	protected void free()
	{
		rieDialogClosedDelegate = null;
		rieDialogOpenedDelegate = null;
		formOpenDelegate = null;
		formModeChangedDelegate = null;
		formModeChangedToComponentDelegate = null;
		formModeChangedToDynamicFormDelegate = null;
		formDialogClosedDelegate = null;
		messageBoxClosedDelegate = null;
		formClosingDelegate = null;
		formValueChangedDelegate = null;
		controlFocusedDelegate = null;
		defaultControlDelegate = null;
		timerElapsedDelegate = null;
		customEventDelegate = null;
		
		darkHeight = 0;		
		controls.clear();
		setMode(null, false);
		setFormIsChangingMode(false);
		focusedControl = null;
		defaultControl = null;
		setReadOnly(false);
		
		for(int x = 0; x < registredMenus.size(); x++)
			registredMenus.get(x).free();
		
		registredMenus.clear();
		timers.clear();
	}
	
	public boolean getFormIsChangingMode()
	{
		return this.formIsChangingMode;
	}
	protected void setFormIsChangingMode(boolean value)
	{
		this.formIsChangingMode = value;
	}

	public boolean isReadOnly() 
	{
		return this.readOnly;
	}

	public void setReadOnly(boolean b) 
	{
		this.readOnly = b;
		setReadOnly(controls.iterator(), b);
	}
	
	private void setReadOnly(Iterator iterator, boolean value)
	{		
		while (iterator.hasNext())
        {
			Object control = iterator.next();
			if(control instanceof IControlContainer)
			{
				setReadOnly(((IControlContainer)control).getIterator(), value);
			}
			else if(control instanceof CustomComponent)
			{
				((ims.framework.cn.controls.CustomComponent)control).setReadOnly(value);
			}
        }
	}
	
	public String getHelpLink()
	{
		return this.helpLink;
	}
	public void setHelpLink(String value)
	{
		this.helpLink = value == null ? "" : value;
	}
	public void registerMenu(Menu menu)
	{
		this.registredMenus.add(menu);
	}
	public void unregisterMenu(Menu menu)
	{
		this.registredMenus.remove(menu);
	}
	public Menu[] getRegisteredMenus()
	{
		Menu[] result = new Menu[registredMenus.size()];
		for(int x = 0; x < registredMenus.size(); x++)
		{
			result[x] = registredMenus.get(x);
		}
		return result;
	}
	public void addTimer(Timer value)
	{
		timers.add(value);
	}
	public Timer[] getTimers()
	{
		Timer[] result = new Timer[timers.size()];		
		for(int x = 0; x < timers.size(); x++)
		{
			result[x] = timers.get(x);
		}
		return result;
	}
	public Control getDefaultControl()
	{
		return defaultControl;
	}
	
	public String toString()
	{
		FormInfo info = getFormInfo();		
		return info == null ? super.toString() : info.toString();
	}

	protected RIEDialogOpened rieDialogOpenedDelegate = null;
	protected RIEDialogClosed rieDialogClosedDelegate = null;
	protected FormOpen formOpenDelegate = null;
	protected FormModeChanged formModeChangedDelegate = null;
	protected FormModeChanged formModeChangedToComponentDelegate = null;
	protected DynamicFormModeChanged formModeChangedToDynamicFormDelegate = null;
	protected FormDialogClosed formDialogClosedDelegate = null;
	protected MessageBoxClosed messageBoxClosedDelegate = null;
	protected FormClosing formClosingDelegate = null;
	protected ValueChanged formValueChangedDelegate = null;		
	protected ControlFocused controlFocusedDelegate;
	protected DefaultControl defaultControlDelegate;
	protected TimerElapsed timerElapsedDelegate;
	protected CustomEvent customEventDelegate = null;
	
	private int width, height;	
	protected int darkHeight = 0;

	private FormMode formMode;
	protected ArrayList<Control> controls = new ArrayList<Control>();
    private boolean formIsChangingMode = false;
    protected Control focusedControl = null;
    protected Control defaultControl = null;
    private boolean readOnly = false;
    private String helpLink = "";
    protected GenericChangeableCollection<Menu> registredMenus = new GenericChangeableCollection<Menu>();
    protected GenericChangeableCollection<Timer> timers = new GenericChangeableCollection<Timer>();
}
