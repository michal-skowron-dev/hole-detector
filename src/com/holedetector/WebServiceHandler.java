package com.holedetector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;

public class WebServiceHandler extends AsyncTask<String, Void, String>
{
	@Override
	protected String doInBackground(String... urls)
	{
		try
		{
			URL url = new URL(urls[0]);
			URLConnection connection = url.openConnection();
			
			InputStream in = new BufferedInputStream(connection.getInputStream());
			
			return streamToString(in);
		}
		catch (Exception e)
		{
			Log.d(MainActivity.class.getSimpleName(), e.toString());
			return null;
		}
	}
	
	public String streamToString(InputStream is)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		
		try
		{
			while((line = reader.readLine()) != null)
			{
				stringBuilder.append(line + "\n");
			}
			
			reader.close();
		}
		catch (IOException e)
		{
			Log.d(MainActivity.class.getSimpleName(), e.toString());
		}
		
		return stringBuilder.toString();
	}
	
	/*
	@Override
    protected void onPostExecute(String result)
	{
        try
        {
            JSONObject json = new JSONObject(result);

        }
        catch (Exception e)
        {
            Log.d(MainActivity.class.getSimpleName(), e.toString());
        }
    }
    */
}