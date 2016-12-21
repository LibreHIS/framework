package ims.framework.delegates;

import ims.framework.enumerations.DialogResult;

import java.io.Serializable;

public interface MessageBoxClosed extends Serializable
{
	public void handle(int id, DialogResult result) throws ims.framework.exceptions.PresentationLogicException;
}
