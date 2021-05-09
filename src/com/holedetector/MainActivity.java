package com.holedetector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("FloatMath")
public class MainActivity extends Activity implements LocationListener, SensorEventListener
{
	private TextView textview1, textview3, textview5, textview6, textview8,
					 textview2, textview4, textview7;
	private Button button1, button2;
	private Switch switch1;
	
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
	
	private boolean onLocationChangePermission = false;
	private boolean sensorRegistered = false;
	private boolean programmaticallyChangedSwitchState = false;
	
	private String lat = "";
	private String lon = "";
	
	private String device = null;
	private String device_name = null;
	private String os = null;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	private SimpleDateFormat eng = new SimpleDateFormat("dd MMMM yyyy (HH:mm:ss)", Locale.ENGLISH);
	private SimpleDateFormat ger = new SimpleDateFormat("dd MMMM yyyy (HH:mm:ss)", Locale.GERMAN);
	private SimpleDateFormat pol = new SimpleDateFormat("dd MMMM yyyy (HH:mm:ss)", new Locale("pl", "PL"));
	private String time = df.format(Calendar.getInstance().getTime());
	
	private Handler _handler;
	
	@Override
	public void onPause()
	{
	    _handler = null;
	    super.onPause();
	}
	
	@Override
	public void onResume()
	{
	    super.onResume();
	    
	    _handler = new Handler();
	    Runnable r = new Runnable()
	    {
	        public void run()
	        {
	            if (_handler == _h0)
	            {
	                tick();
	                _handler.postDelayed(this, 1000);
	            }
	        }

	        private final Handler _h0 = _handler;
	    };
	    r.run();
	}

	private void tick()
	{
		if (SharedVariables.languageChanged())	
		{
			SharedVariables.languageChanged(false);
			
			if (SharedVariables.language() == 0)
			{
				setTitle(getResources().getString(R.string.app_name));
				switch1.setText(getResources().getString(R.string.ENG_switch_1));
				button1.setText(getResources().getString(R.string.ENG_button_1));
				
				if (!button2.isEnabled())
				{
					button2.setText(getResources().getString(R.string.ENG_button_2));
				}
				else
				{
					String str = (String) button2.getText();
					str = str.replaceAll("\\D+","");
					
					
					if (str.equals(""))
					{
						button2.setText(getResources().getString(R.string.ENG_button_2));
					}
					else if (str.equals("1"))
					{
						button2.setText(getResources().getString(R.string.ENG_button_2_1));
					}
					else
					{
						button2.setText(getResources().getString(R.string.ENG_button_2) + String.format(" (+%s new holes)", str));
					}
				}
				
				textview1.setText(getResources().getString(R.string.ENG_textview_1));
				textview2.setText(getResources().getString(R.string.ENG_textview_2));
				textview3.setText(getResources().getString(R.string.ENG_textview_3));
				textview4.setText(getResources().getString(R.string.ENG_textview_4));
				textview5.setText(getResources().getString(R.string.ENG_textview_5));
				textview6.setText(getResources().getString(R.string.ENG_textview_6));
				textview7.setText(getResources().getString(R.string.ENG_textview_7));
				textview8.setText(getResources().getString(R.string.ENG_textview_8));
			}
			else if (SharedVariables.language() == 1)
			{
				setTitle(getResources().getString(R.string.POL_app_name));
				switch1.setText(getResources().getString(R.string.POL_switch_1));
				button1.setText(getResources().getString(R.string.POL_button_1));
				
				if (!button2.isEnabled())
				{
					button2.setText(getResources().getString(R.string.POL_button_2));
				}
				else
				{
					String str = (String) button2.getText();
					str = str.replaceAll("\\D+","");
					
					
					if (str.equals(""))
					{
						button2.setText(getResources().getString(R.string.POL_button_2));
					}
					else if (str.equals("1"))
					{
						button2.setText(getResources().getString(R.string.POL_button_2_1));
					}
					else
					{
						button2.setText(getResources().getString(R.string.POL_button_2) + String.format(" (+%s nowych dziur)", str));
					}
				}
				
				textview1.setText(getResources().getString(R.string.POL_textview_1));
				textview2.setText(getResources().getString(R.string.POL_textview_2));
				textview3.setText(getResources().getString(R.string.POL_textview_3));
				textview4.setText(getResources().getString(R.string.POL_textview_4));
				textview5.setText(getResources().getString(R.string.POL_textview_5));
				textview6.setText(getResources().getString(R.string.POL_textview_6));
				textview7.setText(getResources().getString(R.string.POL_textview_7));
				textview8.setText(getResources().getString(R.string.POL_textview_8));
			}
			else
			{
				setTitle(getResources().getString(R.string.GER_app_name));
				switch1.setText(getResources().getString(R.string.GER_switch_1));
				button1.setText(getResources().getString(R.string.GER_button_1));
				
				if (!button2.isEnabled())
				{
					button2.setText(getResources().getString(R.string.GER_button_2));
				}
				else
				{
					String str = (String) button2.getText();
					str = str.replaceAll("\\D+","");
					
					
					if (str.equals(""))
					{
						button2.setText(getResources().getString(R.string.GER_button_2));
					}
					else if (str.equals("1"))
					{
						button2.setText(getResources().getString(R.string.GER_button_2_1));
					}
					else
					{
						button2.setText(getResources().getString(R.string.GER_button_2) + String.format(" (+%s neue löcher)", str));
					}
				}

				textview1.setText(getResources().getString(R.string.GER_textview_1));
				textview2.setText(getResources().getString(R.string.GER_textview_2));
				textview3.setText(getResources().getString(R.string.GER_textview_3));
				textview4.setText(getResources().getString(R.string.GER_textview_4));
				textview5.setText(getResources().getString(R.string.GER_textview_5));
				textview6.setText(getResources().getString(R.string.GER_textview_6));
				textview7.setText(getResources().getString(R.string.GER_textview_7));
				textview8.setText(getResources().getString(R.string.GER_textview_8));
			}
		}
		
		if (SharedVariables.language() == 0)
		{
			textview1.setText(getResources().getString(R.string.ENG_textview_1) + " " + eng.format(Calendar.getInstance().getTime()));
		}
		else if (SharedVariables.language() == 1)
		{
			textview1.setText(getResources().getString(R.string.POL_textview_1) + " " + pol.format(Calendar.getInstance().getTime()));
		}
		else
		{
			textview1.setText(getResources().getString(R.string.GER_textview_1) + " " + ger.format(Calendar.getInstance().getTime()));
		}
	}
	
	private boolean isMyServiceRunning(Class<?> serviceClass)
	{
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
	    {
	        if (serviceClass.getName().equals(service.service.getClassName()))
	        {
	            return true;
	        }
	    }
	    return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		device = SharedMethods.capitalize(Build.MANUFACTURER);
		device_name = SharedMethods.capitalize(Build.MODEL);
		os = Build.VERSION.RELEASE;
		
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		textview1 = (TextView) findViewById(R.id.textView1);
		textview3 = (TextView) findViewById(R.id.textView3);
		textview5 = (TextView) findViewById(R.id.textView5);
		textview6 = (TextView) findViewById(R.id.textView6);
		textview8 = (TextView) findViewById(R.id.textView8);
		
		textview2 = (TextView) findViewById(R.id.textView2);
		textview4 = (TextView) findViewById(R.id.textView4);
		textview7 = (TextView) findViewById(R.id.textView7);
		
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		
		switch1 = (Switch) findViewById(R.id.switch1);
		
		SharedMethods.ReadSettings(false);
		
		button1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dziury.ml/export.php")));
			}
		});
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
				if (arg1)
				{
					if (SharedVariables.language() == 0)
					{
				    	Toast.makeText(MainActivity.this, getResources().getString(R.string.ENG_wait), Toast.LENGTH_SHORT).show();
					}
					else if (SharedVariables.language() == 1)
					{
				    	Toast.makeText(MainActivity.this, getResources().getString(R.string.POL_wait), Toast.LENGTH_SHORT).show();
					}
					else
					{
				    	Toast.makeText(MainActivity.this, getResources().getString(R.string.GER_wait), Toast.LENGTH_SHORT).show();
					}
					
					onLocationChangePermission = true;
					
					if (programmaticallyChangedSwitchState)
					{
						programmaticallyChangedSwitchState = false;
					}
					else
					{
						SharedVariables.driver_nr(SharedMethods.SetDriverNumber());
					}
					
					if (SharedMethods.EmptyLogs())
					{
						SharedMethods.FormatLogs(false);
					}
					
					if (SharedVariables.language() == 0)
					{
						button2.setText(getResources().getString(R.string.ENG_button_2));
					}
					else if (SharedVariables.language() == 1)
					{
						button2.setText(getResources().getString(R.string.POL_button_2));
					}
					else
					{
						button2.setText(getResources().getString(R.string.GER_button_2));
					}
					
					button2.setEnabled(false);
				}
				else
				{
					onLocationChangePermission = false;
					sensorManager.unregisterListener(MainActivity.this);
					sensorRegistered = false;
					
					SharedMethods.ReadSettings(true);
					if (SharedVariables.holeCounter() != 0)
					{
						if (SharedVariables.holeCounter() == 1)
						{
							if (SharedVariables.language() == 0)
							{
								button2.setText(getResources().getString(R.string.ENG_button_2_1));
							}
							else if (SharedVariables.language() == 1)
							{
								button2.setText(getResources().getString(R.string.POL_button_2_1));
							}
							else
							{
								button2.setText(getResources().getString(R.string.GER_button_2_1));
							}
						}
						else
						{
							if (SharedVariables.language() == 0)
							{
								button2.setText(getResources().getString(R.string.ENG_button_2) + String.format(" (+%s new holes)", SharedVariables.holeCounter()));
							}
							else if (SharedVariables.language() == 1)
							{
								button2.setText(getResources().getString(R.string.POL_button_2) + String.format(" (+%s nowych dziur)", SharedVariables.holeCounter()));
							}
							else
							{
								button2.setText(getResources().getString(R.string.GER_button_2) + String.format(" (+%s neue löcher)", SharedVariables.holeCounter()));
							}
						}
						
						SharedVariables.holeCounter(0);
					}
					
					if (SharedVariables.language() == 0)
					{
						textview3.setText(getResources().getString(R.string.ENG_textview_3));
						textview5.setText(getResources().getString(R.string.ENG_textview_5));
						textview6.setText(getResources().getString(R.string.ENG_textview_6));
						textview8.setText(getResources().getString(R.string.ENG_textview_8));
					}
					else if (SharedVariables.language() == 1)
					{
						textview3.setText(getResources().getString(R.string.POL_textview_3));
						textview5.setText(getResources().getString(R.string.POL_textview_5));
						textview6.setText(getResources().getString(R.string.POL_textview_6));
						textview8.setText(getResources().getString(R.string.POL_textview_8));
					}
					else
					{
						textview3.setText(getResources().getString(R.string.GER_textview_3));
						textview5.setText(getResources().getString(R.string.GER_textview_5));
						textview6.setText(getResources().getString(R.string.GER_textview_6));
						textview8.setText(getResources().getString(R.string.GER_textview_8));
					}
					
					SharedVariables.driver_nr(0);
					button2.setEnabled(true);
				}
			}
		});
		
		if (isMyServiceRunning(MyService.class))
		{
			programmaticallyChangedSwitchState = true;
			switch1.setChecked(true);
			stopService(new Intent(getBaseContext(), MyService.class));
		}
	}
	
	@Override
	protected void onDestroy()
	{
    	if (switch1.isChecked())
    	{
    		startService(new Intent(getBaseContext(), MyService.class));
    	}
    	
	    super.onDestroy();
	 }
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0)
	{
		 sensorManager.unregisterListener(MainActivity.this);
		 
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
				if (SharedVariables.language() == 0)
				{
					textview3.setText(getResources().getString(R.string.ENG_textview_3_1));
				}
				else if (SharedVariables.language() == 1)
				{
					textview3.setText(getResources().getString(R.string.POL_textview_3_1));
				}
				else
				{
					textview3.setText(getResources().getString(R.string.GER_textview_3_1));
				}
			}
			else if (mAccelX >= -Float.MAX_VALUE && mAccelX <= (-9 + SharedMethods.SensitiveFactor()))
			{
				wheel = 1;
				if (SharedVariables.language() == 0)
				{
					textview3.setText(getResources().getString(R.string.ENG_textview_3_2));
				}
				else if (SharedVariables.language() == 1)
				{
					textview3.setText(getResources().getString(R.string.POL_textview_3_2));
				}
				else
				{
					textview3.setText(getResources().getString(R.string.GER_textview_3_2));
				}
			}
			else
			{
				wheel = 0;
				if (SharedVariables.language() == 0)
				{
					textview3.setText(getResources().getString(R.string.ENG_textview_3_3));
				}
				else if (SharedVariables.language() == 1)
				{
					textview3.setText(getResources().getString(R.string.POL_textview_3_3));
				}
				else
				{
					textview3.setText(getResources().getString(R.string.GER_textview_3_3));
				}
			}
			
			mAccelLast = mAccelCurrent;
	        mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
	        float delta = mAccelCurrent - mAccelLast;
	        mAccel = mAccel * 0.9f + delta;
			
			if (mAccel >= (10 - SharedMethods.SensitiveFactor()) && mAccel <= (15 - SharedMethods.SensitiveFactor()))
			{
				holeSize = 1;
				if (SharedVariables.language() == 0)
				{
					textview8.setText(getResources().getString(R.string.ENG_textview_8_1));
				}
				else if (SharedVariables.language() == 1)
				{
					textview8.setText(getResources().getString(R.string.POL_textview_8_1));
				}
				else
				{
					textview8.setText(getResources().getString(R.string.GER_textview_8_1));
				}
			}
			else if (mAccel > (15 - SharedMethods.SensitiveFactor()) && mAccel <= (20 - SharedMethods.SensitiveFactor()))
			{
				holeSize = 2;
				if (SharedVariables.language() == 0)
				{
					textview8.setText(getResources().getString(R.string.ENG_textview_8_2));
				}
				else if (SharedVariables.language() == 1)
				{
					textview8.setText(getResources().getString(R.string.POL_textview_8_2));
				}
				else
				{
					textview8.setText(getResources().getString(R.string.GER_textview_8_2));
				}
			}
			else if (mAccel > (20 - SharedMethods.SensitiveFactor()))
			{
				holeSize = 3;
				if (SharedVariables.language() == 0)
				{
					textview8.setText(getResources().getString(R.string.ENG_textview_8_3));
				}
				else if (SharedVariables.language() == 1)
				{
					textview8.setText(getResources().getString(R.string.POL_textview_8_3));
				}
				else
				{
					textview8.setText(getResources().getString(R.string.GER_textview_8_3));
				}
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
			
			sensorManager.registerListener(MainActivity.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		 }
	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		if(!onLocationChangePermission) return;
		
		lat = String.valueOf(arg0.getLatitude());
		lon = String.valueOf(arg0.getLongitude());
		
		if (SharedVariables.language() == 0)
		{
			textview5.setText(getResources().getString(R.string.ENG_textview_5) + " " + lat);
			textview6.setText(getResources().getString(R.string.ENG_textview_6) + " " + lon);
		}
		else if (SharedVariables.language() == 1)
		{
			textview5.setText(getResources().getString(R.string.POL_textview_5) + " " + lat);
			textview6.setText(getResources().getString(R.string.POL_textview_6) + " " + lon);
		}
		else
		{
			textview5.setText(getResources().getString(R.string.GER_textview_5) + " " + lat);
			textview6.setText(getResources().getString(R.string.GER_textview_6) + " " + lon);
		}
		
		if (!lat.equals("") && !lon.equals("") && !sensorRegistered)
		{
			sensorRegistered = true;
			sensorManager.registerListener(MainActivity.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
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