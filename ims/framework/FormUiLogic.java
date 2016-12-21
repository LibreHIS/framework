package ims.framework;

public class FormUiLogic
{
	private Form form;
	private UILogic uiLogic;

	public FormUiLogic(Form form, UILogic uiLogic)
	{
		this.form = form;
		this.uiLogic = uiLogic;		
	}

	public Form getForm()
	{
		return form;
	}
	public UILogic getUiLogic()
	{
		return uiLogic;
	}
}
