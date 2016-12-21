package ims.framework;

import java.io.Serializable;
import java.util.Iterator;

import ims.framework.controls.BedPlanner;
import ims.framework.controls.DrawingControl;
import ims.framework.controls.DrawingControlConfigurator;
import ims.framework.controls.DynamicGrid;
import ims.framework.controls.Grid;
import ims.framework.controls.Timer;
import ims.framework.delegates.CancelArgs;
import ims.framework.delegates.CustomEvent;
import ims.framework.delegates.FormClosing;
import ims.framework.delegates.FormDialogClosed;
import ims.framework.delegates.FormModeChanged;
import ims.framework.delegates.FormOpen;
import ims.framework.delegates.MessageBoxClosed;
import ims.framework.delegates.RIEDialogClosed;
import ims.framework.delegates.RIEDialogOpened;
import ims.framework.delegates.TimerElapsed;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.enumerations.DialogResult;
import ims.framework.exceptions.PresentationLogicException;

public abstract class FormBridge extends AbstractBridge implements ims.framework.IReportDataProvider, ims.framework.interfaces.IRecordedInError, Serializable
{ 
	private static final long serialVersionUID = 1L;
	
	public abstract String getUniqueIdentifier();	
	
	public final boolean isReadOnly()
	{
		return this.form.isReadOnly();
	}
	protected void setContext(Form value)
	{
		value.setUniqueIdentifier(getUniqueIdentifier());
		
		this.form = value;				
		this.form.setFormOpenEvent(new FormOpen()
		{
			private static final long serialVersionUID = 1L;

			public void handle(Object[] args)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.formOpenDelegate != null)
					FormBridge.this.formOpenDelegate.handle(args);
			}
		});
		this.form.setRIEDialogOpenedEvent(new RIEDialogOpened()
		{
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.rieDialogOpenedDelegate != null)
					FormBridge.this.rieDialogOpenedDelegate.handle();
			}
		});	
		this.form.setRIEDialogClosedEvent(new RIEDialogClosed()
		{
			private static final long serialVersionUID = 1L;

			public void handle(DialogResult result)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.rieDialogClosedDelegate != null)
					FormBridge.this.rieDialogClosedDelegate.handle(result);
			}
		});		
		this.form.setFormModeChangedEvent(new FormModeChanged()
		{
			private static final long serialVersionUID = 1L;
			public void handle()
			{
				FormBridge.this.form.setFormIsChangingMode(true);
				handle(FormBridge.this.form.getControls().iterator());
				FormBridge.this.form.setFormIsChangingMode(false);
				if (FormBridge.this.formModeChangedDelegate != null)
					FormBridge.this.formModeChangedDelegate.handle();
			}
			private void handle(Iterator<Control> iterator)
			{
				while (iterator.hasNext())
				{
					Control c = iterator.next();
					if (c instanceof Container)
						handle(((Container)c).getIterator());
					else
					{
						if ((FormBridge.this.form.getMode().equals(FormMode.EDIT) && c.getEditMode().equals(ControlState.HIDDEN)) || (FormBridge.this.form.getMode().equals(FormMode.VIEW) && c.getViewMode().equals(ControlState.HIDDEN)))
							c.setVisible(false);
						else
						{
							if ((FormBridge.this.form.getMode().equals(FormMode.EDIT) && !c.getEditMode().equals(ControlState.UNKNOWN)) || (FormBridge.this.form.getMode().equals(FormMode.VIEW) && !c.getViewMode().equals(ControlState.UNKNOWN)))
								c.setVisible(true);
							if (c instanceof Grid)
							{
								Grid grid = (Grid) c;
								if (FormBridge.this.form.getMode().equals(FormMode.EDIT))
								{
									if (c.getEditMode().equals(ControlState.DISABLED))
										grid.setEnabled(false);
									else
									{
										grid.setEnabled(true);
										if (c.getEditMode().equals(ControlState.EDITABLE))
											grid.setReadOnly(false);
										else if (c.getEditMode().equals(ControlState.READONLY))
											grid.setReadOnly(true);
									}
								}
								else
								{
									if (c.getViewMode().equals(ControlState.DISABLED))
										grid.setEnabled(false);
									else
									{
										grid.setEnabled(true);
										if (c.getViewMode().equals(ControlState.EDITABLE))
											grid.setReadOnly(false);
										else if (c.getViewMode().equals(ControlState.READONLY))
											grid.setReadOnly(true);
									}
								}
							}
							else if (c instanceof DrawingControl)
							{
								DrawingControl drawing = (DrawingControl) c;
								if (FormBridge.this.form.getMode().equals(FormMode.EDIT))
								{
									if (c.getEditMode().equals(ControlState.DISABLED))
										drawing.setEnabled(false);
									else
									{
										drawing.setEnabled(true);
										if (c.getEditMode().equals(ControlState.EDITABLE))
											drawing.setReadOnly(false);
										else if (c.getEditMode().equals(ControlState.READONLY))
											drawing.setReadOnly(true);
									}
								}
								else
								{
									if (c.getViewMode().equals(ControlState.DISABLED))
										drawing.setEnabled(false);
									else
									{
										drawing.setEnabled(true);
										if (c.getViewMode().equals(ControlState.EDITABLE))
											drawing.setReadOnly(false);
										else if (c.getViewMode().equals(ControlState.READONLY))
											drawing.setReadOnly(true);
									}
								}								
							}
							else if (c instanceof DrawingControlConfigurator)
							{
								DrawingControlConfigurator drawingConfig = (DrawingControlConfigurator) c;
								if (FormBridge.this.form.getMode().equals(FormMode.EDIT))
								{
									if (c.getEditMode().equals(ControlState.DISABLED))
										drawingConfig.setEnabled(false);
									else
									{
										drawingConfig.setEnabled(true);
										if (c.getEditMode().equals(ControlState.EDITABLE))
											drawingConfig.setReadOnly(false);
										else if (c.getEditMode().equals(ControlState.READONLY))
											drawingConfig.setReadOnly(true);
									}
								}
								else
								{
									if (c.getViewMode().equals(ControlState.DISABLED))
										drawingConfig.setEnabled(false);
									else
									{
										drawingConfig.setEnabled(true);
										if (c.getViewMode().equals(ControlState.EDITABLE))
											drawingConfig.setReadOnly(false);
										else if (c.getViewMode().equals(ControlState.READONLY))
											drawingConfig.setReadOnly(true);
									}
								}								
							}							
                            else if (c instanceof DynamicGrid)
                            {
                                DynamicGrid dynamicgrid = (DynamicGrid) c;
                                if (FormBridge.this.form.getMode().equals(FormMode.EDIT))
                                {
                                    if (c.getEditMode().equals(ControlState.DISABLED))
                                        dynamicgrid.setEnabled(false);
                                    else
                                    {
                                        dynamicgrid.setEnabled(true);
                                        if (c.getEditMode().equals(ControlState.EDITABLE))
                                            dynamicgrid.setReadOnly(false);
                                        else if (c.getEditMode().equals(ControlState.READONLY))
                                            dynamicgrid.setReadOnly(true);
                                    }
                                }
                                else
                                {
                                    if (c.getViewMode().equals(ControlState.DISABLED))
                                        dynamicgrid.setEnabled(false);
                                    else
                                    {
                                        dynamicgrid.setEnabled(true);
                                        if (c.getViewMode().equals(ControlState.EDITABLE))
                                            dynamicgrid.setReadOnly(false);
                                        else if (c.getViewMode().equals(ControlState.READONLY))
                                            dynamicgrid.setReadOnly(true);
                                    }
                                }                               
                            }
                            else if (c instanceof BedPlanner)
                            {
                            	BedPlanner bedPlanner = (BedPlanner) c;
                                if (FormBridge.this.form.getMode().equals(FormMode.EDIT))
                                {
                                    if (c.getEditMode().equals(ControlState.DISABLED))
                                    	bedPlanner.setEnabled(false);
                                    else
                                    {
                                    	bedPlanner.setEnabled(true);
                                        if (c.getEditMode().equals(ControlState.EDITABLE))
                                        	bedPlanner.setReadOnly(false);
                                        else if (c.getEditMode().equals(ControlState.READONLY))
                                        	bedPlanner.setReadOnly(true);
                                    }
                                }
                                else
                                {
                                    if (c.getViewMode().equals(ControlState.DISABLED))
                                    	bedPlanner.setEnabled(false);
                                    else
                                    {
                                    	bedPlanner.setEnabled(true);
                                        if (c.getViewMode().equals(ControlState.EDITABLE))
                                        	bedPlanner.setReadOnly(false);
                                        else if (c.getViewMode().equals(ControlState.READONLY))
                                        	bedPlanner.setReadOnly(true);
                                    }
                                }                               
                            }
							else
							{
								if (FormBridge.this.form.getMode().equals(FormMode.EDIT))
								{
									if (c.getEditMode().equals(ControlState.ENABLED))
										c.setEnabled(true);
									else if (c.getEditMode().equals(ControlState.DISABLED))
										c.setEnabled(false);
								}
								else
								{
									if (c.getViewMode().equals(ControlState.ENABLED))
										c.setEnabled(true);
									else if (c.getViewMode().equals(ControlState.DISABLED))
										c.setEnabled(false);
								}
							}
						}
					}
				}
			}
		});
		this.form.setFormDialogClosedEvent(new FormDialogClosed()
		{
			private static final long serialVersionUID = 1L;

			public void handle(FormName formName, DialogResult result)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.formDialogClosedDelegate != null)
					FormBridge.this.formDialogClosedDelegate.handle(formName, result);
			}
		});
		this.form.setMessageBoxClosedEvent(new MessageBoxClosed()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int messageBoxId, DialogResult result)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.messageBoxClosed != null)
					FormBridge.this.messageBoxClosed.handle(messageBoxId, result);
			}
		});
		this.form.setFormClosingEvent(new FormClosing()
		{
			private static final long serialVersionUID = 1L;

			public void handle(CancelArgs args)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.formClosingDelegate != null)
					FormBridge.this.formClosingDelegate.handle(args);
			}
		});		
		this.form.setTimerElapsedEvent(new TimerElapsed()
		{
			private static final long serialVersionUID = 1L;

			public void handle(Timer timer)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.timerElapsedDelegate != null)
					FormBridge.this.timerElapsedDelegate.handle(timer);
			}
		});
		this.form.setCustomEventEvent(new CustomEvent()
		{
			private static final long serialVersionUID = 1L;

			public void handle(ims.framework.CustomEvent event)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (FormBridge.this.customEventDelegate != null)
					FormBridge.this.customEventDelegate.handle(event);
			}
		});
	}

	protected void free() // called by FormBridgeFlyweightFactory
	{
		this.form = null;
		super.freeCollections();
	}
	
	protected void fireValueChanged()
	{
		if(this.form.formValueChangedDelegate != null)
		try
		{
			this.form.formValueChangedDelegate.handle();
		}
		catch (PresentationLogicException e)
		{
			throw new RuntimeException(e);
		}
	}

	public FormMode getMode()
	{
		return this.form.getMode();
	}

	public void setMode(FormMode value)  
	{
		this.form.setMode(value, true);
	}
	public void setRIEDialogClosedEvent(RIEDialogClosed delegate)
	{
		this.rieDialogClosedDelegate = delegate;
	}
	public void setRIEDialogOpenedEvent(RIEDialogOpened delegate)
	{
		this.rieDialogOpenedDelegate = delegate;
	}
	public void setFormOpenEvent(FormOpen delegate)
	{
		this.formOpenDelegate = delegate;
	}	
	public void setFormModeChangedEvent(FormModeChanged delegate)
	{
		this.formModeChangedDelegate = delegate;
	}
	public void setFormDialogClosedEvent(FormDialogClosed delegate)
	{
		this.formDialogClosedDelegate = delegate;
	}
	public void setMessageBoxClosedEvent(MessageBoxClosed delegate)
	{
		this.messageBoxClosed = delegate;
	}
	public void setFormClosingEvent(FormClosing delegate)
	{
		this.formClosingDelegate = delegate;
	}	
	protected void addControl(Control control)
	{
		this.form.addControl(control);
	}
	protected Object getControl(int index)
	{
		return this.form.controls.get(index);
	}
	public void setTimerElapsedEvent(TimerElapsed delegate)
	{
		this.timerElapsedDelegate = delegate;
	}
	public void setCustomEventEvent(CustomEvent delegate)
	{
		this.customEventDelegate = delegate;
	}

	protected RIEDialogOpened rieDialogOpenedDelegate = null;
	protected RIEDialogClosed rieDialogClosedDelegate = null;
	protected FormOpen formOpenDelegate = null;
	protected FormModeChanged formModeChangedDelegate = null;
	protected FormDialogClosed formDialogClosedDelegate = null;
	protected MessageBoxClosed messageBoxClosed = null;
	protected FormClosing formClosingDelegate = null;
	protected TimerElapsed timerElapsedDelegate = null;
	protected CustomEvent customEventDelegate = null;
	
	private Form form; 
}
