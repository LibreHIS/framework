package ims.framework.delegates;

import ims.framework.utils.Date;

import java.io.Serializable;

public interface DiaryCurrentDateChanged extends Serializable
{
	public void handle(Date date) throws ims.framework.exceptions.PresentationLogicException;
}
