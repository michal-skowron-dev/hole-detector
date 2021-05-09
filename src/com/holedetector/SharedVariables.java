package com.holedetector;

public class SharedVariables
{	
	private static int language = 0;
    public static int language()
    {
    	return language;
    }
    public static void language(int val)
    {
    	language = val;
    }
    
	private static boolean languageChanged = false;
    public static boolean languageChanged()
    {
    	return languageChanged;
    }
    public static void languageChanged(boolean val)
    {
    	languageChanged = val;
    	SharedMethods.ReplaceTextInFile(String.format("%s,%s,%s,%s,%s,%s", language, sending, sensitive, driver_nr, id, holeCounter), "Config.txt");
    }
	
	private static boolean sending = true;
    public static boolean sending()
    {
    	return sending;
    }
    public static void sending(boolean val)
    {
    	sending = val;
    	SharedMethods.ReplaceTextInFile(String.format("%s,%s,%s,%s,%s,%s", language, sending, sensitive, driver_nr, id, holeCounter), "Config.txt");
    }
    
	private static int sensitive = 5;
    public static int sensitive()
    {
    	return sensitive;
    }
    public static void sensitive(int val)
    {
    	sensitive = val;
    	SharedMethods.ReplaceTextInFile(String.format("%s,%s,%s,%s,%s,%s", language, sending, sensitive, driver_nr, id, holeCounter), "Config.txt");
    }
    
	private static int driver_nr = 0;
    public static int driver_nr()
    {
    	return driver_nr;
    }
    public static void driver_nr(int val)
    {
    	driver_nr = val;
    	SharedMethods.ReplaceTextInFile(String.format("%s,%s,%s,%s,%s,%s", language, sending, sensitive, driver_nr, id, holeCounter), "Config.txt");
    }
    
	private static int id = 0;
    public static int id()
    {
    	return id;
    }
    public static void id(int val)
    {
    	id = val;
    	SharedMethods.ReplaceTextInFile(String.format("%s,%s,%s,%s,%s,%s", language, sending, sensitive, driver_nr, id, holeCounter), "Config.txt");
    }
    
	private static int holeCounter = 0;
    public static int holeCounter()
    {
    	return holeCounter;
    }
    public static void holeCounter(int val)
    {
    	holeCounter = val;
    	SharedMethods.ReplaceTextInFile(String.format("%s,%s,%s,%s,%s,%s", language, sending, sensitive, driver_nr, id, holeCounter), "Config.txt");
    }
}
