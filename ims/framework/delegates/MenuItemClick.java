package ims.framework.delegates;

import ims.framework.Control;

/**
 * @author mmihalec
 */
public interface MenuItemClick 
{
	public void handle(Control sender) throws ims.framework.exceptions.PresentationLogicException;
}
