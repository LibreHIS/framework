package ims.framework.delegates;

import ims.framework.enumerations.DialogResult;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public interface RIEDialogClosed extends Serializable
{
	public void handle(DialogResult result) throws ims.framework.exceptions.PresentationLogicException;
}
