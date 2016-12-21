package ims.framework.cn;

import java.util.ArrayList;
import java.util.Iterator;

import ims.framework.Control;
import ims.framework.CustomComponent;
import ims.framework.CustomEvent;
import ims.framework.GenericChangeableCollection;
import ims.framework.IControlContainer;
import ims.framework.cn.controls.Timer;
import ims.framework.cn.data.FormData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.IMenuEvent;
import ims.framework.cn.events.MessageBoxEvent;
import ims.framework.cn.events.TimerEvent;
import ims.framework.controls.DynamicForm;
import ims.framework.delegates.DefaultControl;
import ims.framework.delegates.ControlFocused;
import ims.framework.enumerations.DialogResult;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class Form extends ims.framework.Form
{
    private static final long serialVersionUID = 1L;
    private ims.framework.FormInfo formInfo;
        
    public ims.framework.FormInfo getFormInfo()
    {
    	return formInfo;
    }
    public void setFormInfo(ims.framework.FormInfo formInfo)
    {
    	this.formInfo = formInfo;
    }    
	public void restore(FormData data, boolean isNew)
	{
		attachControlEventHandlers();
		
		if(data.isEmpty())
		{
			for(int i = 0; i < super.controls.size(); ++i)
			{
				data.add(super.controls.get(i).getClass());
			}			
			for (int x = 0; x < super.registredMenus.size(); x++)
			{
				Menu menu = (Menu) super.registredMenus.get(x);
				for (int i = 0; i < menu.count(); i++)
				{
					data.addMenuItem(((MenuItem)menu.get(i)).getData());
				}
			}
			for (int x = 0; x < super.timers.size(); x++)
			{
				data.addTimer(((Timer)super.timers.get(x)).getData());				
			}
		}
		
		for(int i = 0; i < super.controls.size(); ++i)
		{
			((IVisualControl)super.controls.get(i)).restore(data.getData(i), isNew);
		}
		
		int count = 0;
		for (int x = 0; x < super.registredMenus.size(); x++)
		{
			Menu menu = (Menu) super.registredMenus.get(x);
			for (int i = 0; i < menu.count(); i++)
			{
				MenuItem item = (MenuItem)menu.get(i);
				item.restore(data.getMenuItem(count++));				
			}				
		}	
		for(int x = 0; x < super.timers.size(); x++)
		{
			((Timer)super.timers.get(x)).restore(data.getTimer(x));
		}
	}
	public void fireFormOpenEvent(Object[] params) throws PresentationLogicException
	{
		attachControlEventHandlers();
		
		// we notify all custom controls inside this form about this event
		fireFormOpenEvent(controls.iterator());
		
		if (super.formOpenDelegate != null)
			super.formOpenDelegate.handle(params == null ? new Object[0] : params);
	}
	public void fireRIEDialogOpenedEvent() throws PresentationLogicException
	{		
		if (super.rieDialogOpenedDelegate != null)
			super.rieDialogOpenedDelegate.handle();
	}
	public void fireRIEDialogClosedEvent(DialogResult result) throws PresentationLogicException
	{		
		if (super.rieDialogClosedDelegate != null)
			super.rieDialogClosedDelegate.handle(result);
	}
	public void fireFormModeChangedEvent()
	{
		if (super.formModeChangedDelegate != null)
			super.formModeChangedDelegate.handle();
	}
	public void fireFormClosingEvent(ims.framework.delegates.CancelArgs args) throws PresentationLogicException
	{
		if (super.formClosingDelegate != null)
			super.formClosingDelegate.handle(args);
	}
	public void fireMessageBoxClosedEvent(MessageBoxEvent event) throws PresentationLogicException
	{
		if (super.messageBoxClosedDelegate != null)
			super.messageBoxClosedDelegate.handle(event.getID(), event.getResult());
		
		// we notify all custom controls inside this form about this event
		fireMessageBoxClosedEvent(controls.iterator(), event);
	}
	public boolean fireFormDialogClosedEvent(int formName, DialogResult result, String callerIdentifier) throws PresentationLogicException
	{
		class FormName extends ims.framework.FormName
		{
            private static final long serialVersionUID = 1L;
			FormName(int value)
			{
				super(value);
			}
		}
		
		if((callerIdentifier == null && this.getUniqueIdentifier() == null) || (this.getUniqueIdentifier() != null && this.getUniqueIdentifier().equals(callerIdentifier)))
		{
			if (super.formDialogClosedDelegate != null)
				super.formDialogClosedDelegate.handle(new FormName(formName), result);	
			return true;
		}		
		else
		{		
			// we notify all custom controls inside this form about this event
			// BUT only one component caused the dialog to be opened so we only need to notify that component
			return fireFormDialogClosedEvent(controls.iterator(), formName, result, callerIdentifier);
		}
	}
	public void fireCustomEventEvent(CustomEvent event) throws PresentationLogicException
	{
        if(super.customEventDelegate != null)
        	super.customEventDelegate.handle(event);
        
        fireCustomEventEvent(controls.iterator(), event);
	}
	private void fireCustomEventEvent(Iterator iterator, CustomEvent event) throws PresentationLogicException 
	{
		while (iterator.hasNext())
        {
			Object control = iterator.next();
			if(control instanceof IControlContainer)
			{
				fireCustomEventEvent(((IControlContainer)control).getIterator(), event);
			}
			else if(control instanceof CustomComponent)
			{
				((ims.framework.cn.controls.CustomComponent)control).fireCustomEventEvent(event);
			}
        }
	}
	private void fireFormOpenEvent(Iterator iterator) throws PresentationLogicException 
	{
		while (iterator.hasNext())
        {
			Object control = iterator.next();
			if(control instanceof IControlContainer)
			{
				fireFormOpenEvent(((IControlContainer)control).getIterator());
			}
			else if(control instanceof CustomComponent)
			{
				((ims.framework.cn.controls.CustomComponent)control).fireFormOpenEvent();
			}
        }
	}
	private boolean fireFormDialogClosedEvent(Iterator iterator, int formName, DialogResult result, String callerIdentifier) throws PresentationLogicException 
	{
		while (iterator.hasNext())
        {
			Object control = iterator.next();
			if(control instanceof IControlContainer)
			{
				if(fireFormDialogClosedEvent(((IControlContainer)control).getIterator(), formName, result, callerIdentifier))
					return true;
			}
			else if(control instanceof CustomComponent)
			{
				if(((ims.framework.cn.controls.CustomComponent)control).fireFormDialogClosedEvent(formName, result, callerIdentifier))
					return true;
			}			
			else if(control instanceof DynamicForm)
			{
				if(((ims.framework.cn.controls.DynamicForm)control).fireFormDialogClosedEvent(formName, result, callerIdentifier))
					return true;
			}
        }
		
		return false;
	}	
	private void fireMessageBoxClosedEvent(Iterator iterator, MessageBoxEvent event) throws PresentationLogicException 
	{
		while (iterator.hasNext())
        {
			Object control = iterator.next();
			if(control instanceof IControlContainer)
			{
				fireMessageBoxClosedEvent(((IControlContainer)control).getIterator(), event);
			}
			else if(control instanceof CustomComponent)
			{
				((ims.framework.cn.controls.CustomComponent)control).fireMessageBoxClosedEvent(event);
			}
        }
	}
	private boolean fireEvent(Iterator iterator, IControlEvent event) throws PresentationLogicException
	{		
       	while (iterator.hasNext())
        {
			Control control = (Control)iterator.next();
            if (event.getControlID() == super.getControlID(control))
			{
				if(((IVisualControl) control).fireEvent(event))
					return true;
			}
            else if(control instanceof CustomComponent)
            {
            	if(((IVisualControl) control).fireEvent(event))
            		return true;
            }
            else if(control instanceof DynamicForm)
            {
            	if(((IVisualControl) control).fireEvent(event))
            		return true;
            }
            else if (control instanceof IControlContainer)
            {
            	if (fireEvent(((ims.framework.IControlContainer)control).getIterator(), event))
            		return true;
            }
        }
       	
        return false;
	}
	public Control findControl(int controlID)
	{
		return findControl(super.controls.iterator(), controlID);
	}
	private Control findControl(Iterator iterator, int controlID)
	{
		while (iterator.hasNext())
        {
			Control control = (Control)iterator.next();
			if(control.getID() == controlID)
				return control;
			else if(control instanceof IControlContainer)
			{
				Control childControl = findControl(((IControlContainer)control).getIterator(), controlID);
				if(childControl != null)
					return childControl;
			}	
			else if(control instanceof CustomComponent)
			{
				Control childControl = findControl(((CustomComponent)control).getIterator(), controlID);
				if(childControl != null)
					return childControl;
			}
			else if(control instanceof DynamicForm)
			{
				Control childControl = ((ims.framework.cn.controls.DynamicForm)control).findControl(controlID);
				if(childControl != null)
					return childControl;
			}
        }
		
		return null;			
	}
	private boolean fireEvent(Iterator iterator, IMenuEvent event) throws PresentationLogicException
	{
		Control sender = findControl(iterator, event.getControlID());
		if(sender == null)
			throw new PresentationLogicException("Sender control not found (MenuID: " + event.getMenuID() + ", MenuItemID: " + event.getMenuItemID() + ")");
		
		if(super.registredMenus != null)
		{
			for(int x = 0; x < super.registredMenus.size(); x++)
			{
				Menu menu = (Menu)super.registredMenus.get(x);
				if(menu.getID() == event.getMenuID())
				{
					for(int y = 0; y < menu.count(); y++)
					{
						if(menu.get(y).getID() == event.getMenuItemID())
						{
							menu.get(y).click(sender);
							return true;
						}
					}
				}
			}
		}
		
		Control parentControl = sender.getParentControl();
		while(parentControl != null)
		{
			if(parentControl instanceof DynamicForm)
			{
				boolean result = ((ims.framework.cn.controls.DynamicForm)parentControl).fireEvent(event);
				if(result)
					return true;
			}
			else if(parentControl instanceof CustomComponent)
			{
				boolean result = ((ims.framework.cn.controls.CustomComponent)parentControl).fireEvent(event);
				if(result)
					return true;
			}
			
			parentControl = parentControl.getParentControl();
		}
		
		return false;
	}
	public void fireEvent(TimerEvent event) throws PresentationLogicException
	{
        for(int x = 0; x < timers.size(); x++)
        {
        	if(timers.get(x).getID() == event.getID())
        	{
        		if (super.timerElapsedDelegate != null)
        			super.timerElapsedDelegate.handle(timers.get(x));
        		return;
        	}
        }
	}	
	public boolean fireEvent(IMenuEvent event) throws PresentationLogicException
	{
        return fireEvent(super.controls.iterator(), event);
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
        return fireEvent(super.controls.iterator(), event);
	}
	public void renderForm(StringBuffer sb, Integer navigationID, String caption) throws ConfigurationException
	{			
		sb.append("<form");
		
		if(navigationID != null)
		{
			sb.append(" navID=\"");		
			sb.append(navigationID);
			sb.append("\"");
		}
		
		sb.append(" width=\"");
		sb.append(this.getWidth());
		sb.append("\" height=\"");
		sb.append(this.getHeight());
				
		sb.append("\" caption=\"");
		sb.append(ims.framework.utils.StringUtils.encodeXML(caption));
		
		sb.append("\" captionImg=\"");
		sb.append(formInfo.getImagePath());
				
		if(super.getHelpLink().length() > 0)
		{
			sb.append("\" helpLink=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(super.getHelpLink()));
		}
		sb.append("\" darkHeight=\"");
		sb.append(super.getDarkHeight());	
		sb.append("\">");
		for (int i = 0; i < super.controls.size(); ++i)
			 ((IVisualControl) super.controls.get(i)).renderControl(sb);
		sb.append("</form>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{		
		for(int i = 0; i < super.controls.size(); ++i)
		{			
			Control control = super.controls.get(i);
		 	if(control.wasChanged())
		 	{		 		
		 		((IVisualControl)control).renderData(sb);
		 		control.markUnchanged();
		 	}			
		}
		
		for(int x = 0; x < this.registredMenus.size(); x++)
		{			
			Menu menu = (Menu)this.registredMenus.get(x); 
			if(menu.wasChanged())
			{								
				menu.render(sb, this.isReadOnly());
				menu.markUnchanged();
			}
		}		
	}
	public GenericChangeableCollection<ims.framework.controls.Timer> renderTimers()
	{
		GenericChangeableCollection<ims.framework.controls.Timer> activeTimers = new GenericChangeableCollection<ims.framework.controls.Timer>();
		if(timers.wasChanged())
		{
			for(int x = 0; x < timers.size(); x++)
			{
				ims.framework.controls.Timer timer = timers.get(x);
				if(timer.wasChanged())
					activeTimers.add(timer);
			}
			
			timers.markUnchanged();
		}
		return activeTimers;
	}	
	public boolean wasChanged()
	{
		for(int i = 0; i < super.controls.size(); ++i)
		{
			Control control = super.controls.get(i);
			if(control.wasChanged())
			 	return true;		 		
		}
		for(int x = 0; x < this.registredMenus.size(); x++)
		{			
			Menu menu = (Menu)this.registredMenus.get(x); 
			if(menu.wasChanged())
				return true;
		}
		
		return false;
	}
	ArrayList getAllControls()
	{
		return super.controls;
	}
	public Control getFocusedControl()
	{
		return this.focusedControl;
	}
	public void free() // free resources. Called by ControlFlyweightFactory.freeForm(...)
	{
		super.free();		
	}
	void freeControl(Control c)
	{
		super.free(c);
	}
	private void attachControlEventHandlers()
	{
		ArrayList controlList = getAllControls();
		for(int x = 0; x < controlList.size(); x++)
		{
			Control control = (Control)controlList.get(x);
			
			if(control instanceof ims.framework.cn.controls.Container)
				attachControlEventHandlers((ims.framework.cn.controls.Container)control);
			
			control.setControlFocusedEvent(new ControlFocused()
			{
				private static final long serialVersionUID = 1L;

				public void handle(Control control) 
				{
					Form.this.focusedControl = control;
					if(Form.this.controlFocusedDelegate != null)
						Form.this.controlFocusedDelegate.handle(control);
				}
			});
			
			control.setDefaultControlEvent(new DefaultControl()
			{
				private static final long serialVersionUID = 1L;

				public void handle(Control control) 
				{
					Form.this.defaultControl = control;
					if(Form.this.defaultControlDelegate != null)
						Form.this.defaultControlDelegate.handle(control);
				}
			});
		}
	}
	private void attachControlEventHandlers(ims.framework.cn.controls.Container container)
	{
		Iterator i = container.getIterator();
		while(i.hasNext())
		{
			Control control = (Control)i.next();
			
			if(control instanceof ims.framework.cn.controls.Container)
				attachControlEventHandlers((ims.framework.cn.controls.Container)control);
			
			control.setControlFocusedEvent(new ControlFocused()
			{
				private static final long serialVersionUID = 1L;

				public void handle(Control control) 
				{
					Form.this.focusedControl = control;
					if(Form.this.controlFocusedDelegate != null)
						Form.this.controlFocusedDelegate.handle(control);
				}
			});
			
			control.setDefaultControlEvent(new DefaultControl()
			{
				private static final long serialVersionUID = 1L;

				public void handle(Control control) 
				{
					Form.this.defaultControl = control;
					if(Form.this.defaultControlDelegate != null)
						Form.this.defaultControlDelegate.handle(control);
				}
			});
		}
	}	
}
