package ims.framework.utils;

import ims.configuration.InitConfig;
import ims.framework.interfaces.IDictionaryProvider;
import ims.framework.interfaces.ISpellChecker;

public class SpellCheckerFactory
{
	private static ISpellChecker spellChecker;
	private static boolean initialized = false;
	
	public static synchronized ISpellChecker getInstance(IDictionaryProvider dictionaryProvider)
	{
		if(!initialized)
		{
			initialized = true;
			spellChecker = initialize(dictionaryProvider);
		}
		
		return spellChecker;
	}

	private static ISpellChecker initialize(IDictionaryProvider dictionaryProvider)
	{
		if(InitConfig.getSpellCheckerId() == null)
			return null;
		
		RegisteredSpellCheckers regSpellChecker = RegisteredSpellCheckers.parse(InitConfig.getSpellCheckerId());
		if(regSpellChecker == null)
			return null;
		
		if(regSpellChecker.equals(RegisteredSpellCheckers.JAZZY))
			return new JazzySpellChecker(dictionaryProvider);
		
		return null;
	}
}
