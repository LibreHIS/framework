package ims.framework.interfaces;

import java.io.Serializable;

public interface ISearch extends Serializable
{
	ISearchResult[] search(String text);
}
