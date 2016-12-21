package ims.framework.interfaces;

public interface ISpellCheckError
{
	int[] getPositions();
	String getErrorWord();
	String[] getSuggestedWords();
}
