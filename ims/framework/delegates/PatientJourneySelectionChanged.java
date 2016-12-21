package ims.framework.delegates;

import java.io.Serializable;

public interface PatientJourneySelectionChanged extends Serializable
{
	public void handle(Object entry) throws ims.framework.exceptions.PresentationLogicException;;
}
