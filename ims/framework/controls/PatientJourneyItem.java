package ims.framework.controls;

import ims.base.interfaces.IModifiable;

import java.io.Serializable;

public interface PatientJourneyItem extends Serializable, IModifiable
{
	int getWeekNumber();
	int getPositionIndex();
}
