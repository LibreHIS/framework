package ims.framework.delegates;

import ims.framework.controls.Timer;
import ims.framework.exceptions.PresentationLogicException;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public interface TimerElapsed extends Serializable
{
	public void handle(Timer timer) throws PresentationLogicException;
}
