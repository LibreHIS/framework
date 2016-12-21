package ims.framework;

import java.io.Serializable;

public abstract class FormInfo implements Serializable
{		
	private static final long serialVersionUID = 1L;
	private int formId;
	
	public FormInfo(Integer formId)
	{
		this.formId = formId;
	}	
	
	public final int getFormId()
	{
		return formId;
	}
	
	public abstract String getNamespaceName();
	public abstract String getFormName();	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract String[] getContextVariables();
	public abstract String getLocalVariablesPrefix();
	public abstract FormInfo[] getComponentsFormInfo();
	public abstract String getImagePath();
	
	public String toString()
	{
		return getNamespaceName() + "." + getFormName() + " (Id: " + getFormId() + ")";
	}
}
