package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.Control;

/**
 * @author mmihalec
 */
public interface ControlFocused extends Serializable
{
	public void handle(Control control);
}
