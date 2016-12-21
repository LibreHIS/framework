package ims.framework.controls;

import ims.base.interfaces.IModifiable;
import java.io.Serializable;

public interface PatientJourneyClocks extends Serializable, IModifiable
{
	int size();
	PatientJourneyClock get(int index);
	PatientJourneyClock getByIdentifier(Object value);    
    int indexOf(PatientJourneyClock entry);
    PatientJourneyClock newClock(int startWeekNumber, int endWeekNumber);
    PatientJourneyClock newClock(int startWeekNumber, int endWeekNumber, boolean isEnded);
    boolean remove(PatientJourneyClock clock);
	void clear();	
}
