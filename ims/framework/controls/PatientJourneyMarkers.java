package ims.framework.controls;

import ims.base.interfaces.IModifiable;
import java.io.Serializable;

public interface PatientJourneyMarkers extends Serializable, IModifiable
{
	int size();
	PatientJourneyMarker get(int index);
	PatientJourneyMarker getByIdentifier(Object value);    
    int indexOf(PatientJourneyMarker entry);    
    PatientJourneyMarker newMarker(int weekNumber);
    PatientJourneyMarker newMarker(int weekNumber, String text);
    boolean remove(PatientJourneyMarker entry);
	void clear();	
}
