package ims.framework.controls;

import ims.base.interfaces.IModifiable;
import java.io.Serializable;

public interface PatientJourneyEntries extends Serializable, IModifiable
{
	int size();
	PatientJourneyEntry get(int index);
	PatientJourneyEntry getByIdentifier(Object value);    
    int indexOf(PatientJourneyEntry entry);
    PatientJourneyEntry newEntry(int weekNumber);
    boolean remove(PatientJourneyEntry entry);
	void clear();	
}
