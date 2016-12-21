package ims.framework;

import ims.framework.controls.ActionButton;

public abstract class Wizard
{
	protected FormName[] forms;
	protected ActionButton[] actionButtons;
	protected boolean fullScreen;
	protected boolean patientInformation;
	private int step = 1;
	
	public Wizard()
	{
	}
	protected void setContext(FormName[] forms, ActionButton[] actionButtons, boolean fullScreen, boolean patientInformation)
	{
		this.forms = forms;
		this.actionButtons = actionButtons;
		this.fullScreen = fullScreen;
		this.patientInformation = patientInformation;
	}
	
	public int getStep()
	{
		return step;
	}
	public FormName getCurrentForm()
	{
		return forms[step - 1];
	}
	public int getNumberOfSteps()
	{
		return forms.length;
	}
	public FormName first()
	{
		step = 1;
		return getCurrentForm();
	}
	public FormName next()
	{
		step++;
		return getCurrentForm();
	}
	public FormName previous()
	{
		step--;
		return getCurrentForm();
	}
	public FormName last()
	{
		step = getNumberOfSteps();
		return getCurrentForm();
	}
	public boolean isFullScreen()
	{
		return fullScreen;
	}
	public boolean hasPatientInformation()	
	{
		return patientInformation;
	}
}
