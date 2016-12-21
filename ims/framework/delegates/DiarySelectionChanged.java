package ims.framework.delegates;

import ims.framework.controls.DiaryEvent;

import java.io.Serializable;

public interface DiarySelectionChanged extends Serializable
{
	public void handle(DiaryEvent event) throws ims.framework.exceptions.PresentationLogicException;
}
