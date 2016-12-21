package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.FormName;
import ims.framework.enumerations.DialogResult;

public interface FormDialogClosed extends Serializable
{
	public void handle(FormName formName, DialogResult result) throws ims.framework.exceptions.PresentationLogicException;
}
