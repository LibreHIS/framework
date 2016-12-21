package ims.framework.controls;

import ims.base.interfaces.IModifiable;
import ims.framework.utils.DateTime;

import java.io.Serializable;

public interface DiaryEvents extends Serializable, IModifiable
{
	int size();
	DiaryEvent get(int index);
	DiaryEvent getByIdentifier(Object value);    
    int indexOf(DiaryEvent event);
    DiaryEvent newEvent(DateTime dateTime, String text);
    boolean remove(DiaryEvent event);
	void clear();
}
