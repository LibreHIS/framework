package ims.framework.delegates;

import ims.framework.controls.TimeLineEvent;
import ims.framework.utils.Date;

public interface TimeLineClick
{
	public void handle(TimeLineEvent event, Date date) throws ims.framework.exceptions.PresentationLogicException;
}
