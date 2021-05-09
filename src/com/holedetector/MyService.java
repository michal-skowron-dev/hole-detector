package com.holedetector;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.FloatMath;
import android.widget.Toast;

@SuppressLint("FloatMath")
public class MyService extends Service implements LocationListener, SensorEventListener
{	
	private float[] mGravity;
	
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;
	
	private float mAccelX;
	private float mAccelXLast;
	private float mAccelXCurrent;
	
	private Sensor sensor;
	private SensorManager sensorManager;
	
	private LocationManager locationManager;
	
	private String lat = "";
	private String lon = "";
	
	private String device = null;
	private String device_name = null;
	private String os = null;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	private String time = df.format(Calendar.getInstance().getTime());
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
    @Override
    public void onCreate()
    {
		device = SharedMethods.capitalize(Build.MANUFACTURER);
		device_name = SharedMethods.capitalize(Build.MODEL);
		os = Build.VERSION.RELEASE;
		
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
    	SharedMethods.ReadSettings(false);
    	
		if (SharedVariables.language() == 0)
		{
	    	Toast.makeText(this, getResources().getString(R.string.ENG_service_start), Toast.LENGTH_SHORT).show();
		}
		else if (SharedVariables.language() == 1)
		{
	    	Toast.makeText(this, getResources().getString(R.string.POL_service_start), Toast.LENGTH_SHORT).show();
		}
		else
		{
	    	Toast.makeText(this, getResources().getString(R.string.GER_service_start), Toast.LENGTH_SHORT).show();
		}
		
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		
    	return START_STICKY;
    }
    
    @Override
    public void onDestroy()
    {
		if (SharedVariables.language() == 0)
		{
	    	Toast.makeText(this, getResources().getString(R.string.ENG_service_stop), Toast.LENGTH_SHORT).show();
		}
		else if (SharedVariables.language() == 1)
		{
	    	Toast.makeText(this, getResources().getString(R.string.POL_service_stop), Toast.LENGTH_SHORT).show();
		}
		else
		{
	    	Toast.makeText(this, getResources().getString(R.string.GER_service_stop), Toast.LENGTH_SHORT).show();
		}
		
		sensorManager.unregisterListener(this);
		
    	super.onDestroy();
    }
    
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0)
	{
		 sensorManager.unregisterListener(this);
		 
		 if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		 {
			mGravity = arg0.values.clone();

			float x = mGravity[0];
			float y = mGravity[1];
			float z = mGravity[2];
			
			mAccelXLast = mAccelXCurrent;
			mAccelXCurrent = x;
			float deltaX = x - mAccelXLast;
			mAccelX = mAccelX * 0.9f + deltaX;
			
			short holeSize = 0;
			int wheel = 0;
			
			if (mAccelX >= (9 - SharedMethods.SensitiveFactor()) && mAccelX <= Float.MAX_VALUE)
			{
				wheel = -1;
			}
			else if (mAccelX >= -Float.MAX_VALUE && mAccelX <= (-9 + SharedMethods.SensitiveFactor()))
			{
				wheel = 1;
			}
			else
			{
				wheel = 0;
			}
			
			mAccelLast = mAccelCurrent;
	        mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
	        float delta = mAccelCurrent - mAccelLast;
	        mAccel = mAccel * 0.9f + delta;
			
			if (mAccel >= (10 - SharedMethods.SensitiveFactor()) && mAccel <= (15 - SharedMethods.SensitiveFactor()))
			{
				holeSize = 1;
			}
			else if (mAccel > (15 - SharedMethods.SensitiveFactor()) && mAccel <= (20 - SharedMethods.SensitiveFactor()))
			{
				holeSize = 2;
			}
			else if (mAccel > (20 - SharedMethods.SensitiveFactor()))
			{
				holeSize = 3;
			}
			
			if (holeSize != 0 && lat.length() != 0 && lon.length() != 0)
			{
				time = df.format(Calendar.getInstance().getTime());
				
				String sholeSize = String.valueOf(holeSize);
				String sdriver_nr = String.valueOf(SharedVariables.driver_nr());
				
				SharedVariables.id(SharedVariables.id() + 1);
				SharedMethods.AppendLogs(String.format("[%s]	[%s]	[%s]	[%s]	[%s]	[%s]	[%s]", SharedVariables.id(), lat, lon, sholeSize, wheel, time, sdriver_nr));
				
				if (SharedVariables.sending())
				{
					new WebServiceHandler().execute("http://dziury.ml/insert.php?lat='"+lat+"'&lon='"+lon+"'&hole='"+sholeSize+"'&wheel='"+wheel+"'&time='"+time+"'&drive_nr='"+sdriver_nr+"'&device='"+device+"'&device_name='"+device_name+"'&os_ver='"+os+"'");
					SharedVariables.holeCounter(SharedVariables.holeCounter() + 1);
				}
				
				if (SharedVariables.language() == 0)
				{
			    	Toast.makeText(this, getResources().getString(R.string.ENG_adding_entry), Toast.LENGTH_SHORT).show();
				}
				else if (SharedVariables.language() == 1)
				{
			    	Toast.makeText(this, getResources().getString(R.string.POL_adding_entry), Toast.LENGTH_SHORT).show();
				}
				else
				{
			    	Toast.makeText(this, getResources().getString(R.string.GER_adding_entry), Toast.LENGTH_SHORT).show();
				}
			}
			
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		 }
	}

	@Override
	public void onLocationChanged(Location arg0)
	{	
		lat = String.valueOf(arg0.getLatitude());
		lon = String.valueOf(arg0.getLongitude());
	}

	@Override
	public void onProviderDisabled(String arg0)
	{
		
	}

	@Override
	public void onProviderEnabled(String arg0)
	{
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2)
	{
		
	}
}
