package com.holedetector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import android.os.Environment;

public class SharedMethods
{
	public static void ReplaceTextInFile(String text, String fileName)
	{
	    try
	    {
	        File root = new File(Environment.getExternalStorageDirectory(), "HoleDetector");
	        if (!root.exists())
	        {
	            root.mkdirs();
	        }
	        
	        File file = new File(root, fileName);
	        FileWriter writer = new FileWriter(file);
	        writer.append(text);
	        writer.flush();
	        writer.close();
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public static void ReadSettings(boolean onlyHoleCounter)
	{
		File dir = Environment.getExternalStoragePublicDirectory("/HoleDetector");
		File file = new File(dir,"Config.txt");

		String text = "";

		try
		{
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    text = br.readLine();
		    br.close();
		}
		catch (IOException e)
		{
			
		}
		
		if (text.length() != 0)
		{
			try
			{
				String[] array = text.split(",");
				
				if (!onlyHoleCounter)
				{
					SharedVariables.language(Integer.parseInt(array[0]));
					SharedVariables.sending(Boolean.parseBoolean(array[1]));
					SharedVariables.sensitive(Integer.parseInt(array[2]));
					SharedVariables.driver_nr(Integer.parseInt(array[3]));
					SharedVariables.id(Integer.parseInt(array[4]));
				}
				SharedVariables.holeCounter(Integer.parseInt(array[5]));
			}
			catch (Exception e)
			{
				// Proszê nie modyfikowaæ pliku: HoleDetector\Config.txt :)
				// ~Micha³
			}
			finally
			{
				if (!onlyHoleCounter)
				{
					SharedVariables.languageChanged(true);
				}
			}
		}
	}
	
	public static void ChangeLogsLanguage()
	{
		File dir = Environment.getExternalStoragePublicDirectory("/HoleDetector");
		File file = new File(dir,"Logs.txt");

		String text = "";

		try
		{
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null)
		    {
		        text += line;
		        text += "\n";
		    }
		    br.close();
		    
		    ReplaceTextInFile("", "Logs.txt");
		    FormatLogs(true);
		    
		    String[] split = text.split("\n");
		    for (int i = 2; i < split.length; i++)
		    {
		    	AppendLogs(split[i]);
		    }
		}
		catch (IOException e)
		{
			return;
		}
	}
	
	public static void AppendLogs(String Text)
	{
		try
		{
	        File root = new File(Environment.getExternalStorageDirectory(), "HoleDetector");
	        if (!root.exists())
	        {
	            root.mkdirs();
	        }
	        
	        File file = new File(root, "Logs.txt");
	        
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		    out.println(Text);
		    out.close();
		}
		catch (IOException e)
		{
			
		}
	}
	
	public static void FormatLogs(boolean onlyChangeLanguage)
	{
		if (!onlyChangeLanguage)
		{
			SharedVariables.id(0);
		}
		
		if (SharedVariables.language() == 0)
		{
			SharedMethods.AppendLogs("[ID]	[LATITUDE]	[LONGITUDE]	[SIZE]	[WHEEL]	[TIME]	[ROUTE]" + System.getProperty("line.separator"));
		}
		else if (SharedVariables.language() == 1)
		{
			SharedMethods.AppendLogs("[ID]	[SZEROKOŒÆ]	[D£UGOŒÆ]	[ROZMIAR]	[KO£O]	[CZAS]	[TRASA]" + System.getProperty("line.separator"));
		}
		else
		{
			SharedMethods.AppendLogs("[ID]	[BREITE]	[LÄNGENGRAD]	[Größe]	[RAD]	[ZEIT]	[ROUTE]" + System.getProperty("line.separator"));
		}
	}
	
	public static boolean EmptyLogs()
	{
        File root = new File(Environment.getExternalStorageDirectory(), "HoleDetector");
        if (!root.exists())
        {
            root.mkdirs();
        }
        
        File file = new File(root, "Logs.txt");
        
		if (file.length() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static String capitalize(String s)
	{
	    if (s == null || s.length() == 0)
	    {
	        return "";
	    }
	    
	    char first = s.charAt(0);
	    if (Character.isUpperCase(first))
	    {
	        return s;
	    }
	    else
	    {
	        return Character.toUpperCase(first) + s.substring(1);
	    }
	}
	
	public static int SetDriverNumber()
	{
		Random r = new Random();
		return r.nextInt(Integer.MAX_VALUE) + 1;
	}
	
	public static float SensitiveFactor()
	{
		if (SharedVariables.sensitive() == 5)
		{
			return 0;
		}
		else
		{
			int sensitive = SharedVariables.sensitive() - 5;
			
			if (sensitive > 0)
			{
				return (float) (1.5 * sensitive);
			}
			else
			{
				return (float) (3 * sensitive);
			}
		}
	}
}