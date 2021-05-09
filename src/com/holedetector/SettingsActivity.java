package com.holedetector;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity
{
	private TextView textview9, textview10, textview11, textview12;
	private ImageView imageview1;
	private Switch switch2;
	private SeekBar seekbar1;
	private Button button3, button4, button5, button6;
	
	public String getMimeType(String url)
    {
        String parts[]=url.split("\\.");
        String extension=parts[parts.length-1];
        String type = null;
        if (extension != null)
        {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
	
	public void OpenFile(File dir)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(dir),getMimeType(dir.getAbsolutePath()));
		startActivity(intent); 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		textview9 = (TextView) findViewById(R.id.textView9);
		textview10 = (TextView) findViewById(R.id.TextView10);
		textview11 = (TextView) findViewById(R.id.TextView11);
		textview12 = (TextView) findViewById(R.id.textView12);
		
		imageview1 = (ImageView) findViewById(R.id.imageButton1);
		imageview1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (SharedVariables.language() == 0)
				{
					imageview1.setImageResource(R.drawable.pol);
					SharedVariables.language(1);
					
					setTitle(getResources().getString(R.string.POL_title_activity_settings));
					textview9.setText(getResources().getString(R.string.POL_textview_9));
					textview10.setText(getResources().getString(R.string.POL_textview_10));
					textview11.setText(getResources().getString(R.string.POL_textview_11));
					textview12.setText(getResources().getString(R.string.POL_textview_12));
					switch2.setText(getResources().getString(R.string.POL_switch_2));
					button3.setText(getResources().getString(R.string.POL_button_3));
					button4.setText(getResources().getString(R.string.POL_button_4));
					button5.setText(getResources().getString(R.string.POL_button_5));
					button6.setText(getResources().getString(R.string.POL_button_6));
				}
				else if (SharedVariables.language() == 1)
				{
					imageview1.setImageResource(R.drawable.ger);
					SharedVariables.language(2);
					
					setTitle(getResources().getString(R.string.GER_title_activity_settings));
					textview9.setText(getResources().getString(R.string.GER_textview_9));
					textview10.setText(getResources().getString(R.string.GER_textview_10));
					textview11.setText(getResources().getString(R.string.GER_textview_11));
					textview12.setText(getResources().getString(R.string.GER_textview_12));
					switch2.setText(getResources().getString(R.string.GER_switch_2));
					button3.setText(getResources().getString(R.string.GER_button_3));
					button4.setText(getResources().getString(R.string.GER_button_4));
					button5.setText(getResources().getString(R.string.GER_button_5));
					button6.setText(getResources().getString(R.string.GER_button_6));
				}
				else
				{
					imageview1.setImageResource(R.drawable.eng);
					SharedVariables.language(0);
					
					setTitle(getResources().getString(R.string.title_activity_settings));
					textview9.setText(getResources().getString(R.string.ENG_textview_9));
					textview10.setText(getResources().getString(R.string.ENG_textview_10));
					textview11.setText(getResources().getString(R.string.ENG_textview_11));
					textview12.setText(getResources().getString(R.string.ENG_textview_12));
					switch2.setText(getResources().getString(R.string.ENG_switch_2));
					button3.setText(getResources().getString(R.string.ENG_button_3));
					button4.setText(getResources().getString(R.string.ENG_button_4));
					button5.setText(getResources().getString(R.string.ENG_button_5));
					button6.setText(getResources().getString(R.string.ENG_button_6));
				}
				
				SharedMethods.ChangeLogsLanguage();
				SharedVariables.languageChanged(true);
			}
		});
		
		switch2 = (Switch) findViewById(R.id.switch2);
		switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
				if (arg1)
				{
					SharedVariables.sending(true);
				}
				else
				{
					SharedVariables.sending(false);
				}
			}
		});
		
		seekbar1 = (SeekBar) findViewById(R.id.seekBar1);
		seekbar1.setProgress(SharedVariables.sensitive());
		seekbar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
			{
				SharedVariables.sensitive(seekbar1.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{
				
			}
		});
		
		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dziury.ml/")));
			}
		});
		
		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
		    	SharedMethods.ReplaceTextInFile("", "Logs.txt");
				SharedMethods.FormatLogs(false);
				
				if (SharedVariables.language() == 0)
				{
			    	Toast.makeText(SettingsActivity.this, getResources().getString(R.string.ENG_clear_logs), Toast.LENGTH_SHORT).show();
				}
				else if (SharedVariables.language() == 1)
				{
			    	Toast.makeText(SettingsActivity.this, getResources().getString(R.string.POL_clear_logs), Toast.LENGTH_SHORT).show();
				}
				else
				{
			    	Toast.makeText(SettingsActivity.this, getResources().getString(R.string.GER_clear_logs), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				OpenFile(new File((Environment.getExternalStorageDirectory() + "/HoleDetector"), "Logs.txt"));
			}
		});
		
		button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				OpenFile(new File((Environment.getExternalStorageDirectory() + "/HoleDetector"), "Config.txt"));
			}
		});
		
		if (SharedVariables.language() == 0)
		{
			imageview1.setImageResource(R.drawable.eng);
			
			setTitle(getResources().getString(R.string.title_activity_settings));
			textview9.setText(getResources().getString(R.string.ENG_textview_9));
			textview10.setText(getResources().getString(R.string.ENG_textview_10));
			textview11.setText(getResources().getString(R.string.ENG_textview_11));
			textview12.setText(getResources().getString(R.string.ENG_textview_12));
			switch2.setText(getResources().getString(R.string.ENG_switch_2));
			button3.setText(getResources().getString(R.string.ENG_button_3));
			button4.setText(getResources().getString(R.string.ENG_button_4));
			button5.setText(getResources().getString(R.string.ENG_button_5));
			button6.setText(getResources().getString(R.string.ENG_button_6));
		}
		else if (SharedVariables.language() == 1)
		{
			imageview1.setImageResource(R.drawable.pol);
			
			setTitle(getResources().getString(R.string.POL_title_activity_settings));
			textview9.setText(getResources().getString(R.string.POL_textview_9));
			textview10.setText(getResources().getString(R.string.POL_textview_10));
			textview11.setText(getResources().getString(R.string.POL_textview_11));
			textview12.setText(getResources().getString(R.string.POL_textview_12));
			switch2.setText(getResources().getString(R.string.POL_switch_2));
			button3.setText(getResources().getString(R.string.POL_button_3));
			button4.setText(getResources().getString(R.string.POL_button_4));
			button5.setText(getResources().getString(R.string.POL_button_5));
			button6.setText(getResources().getString(R.string.POL_button_6));
		}
		else
		{
			imageview1.setImageResource(R.drawable.ger);
			
			setTitle(getResources().getString(R.string.GER_title_activity_settings));
			textview9.setText(getResources().getString(R.string.GER_textview_9));
			textview10.setText(getResources().getString(R.string.GER_textview_10));
			textview11.setText(getResources().getString(R.string.GER_textview_11));
			textview12.setText(getResources().getString(R.string.GER_textview_12));
			switch2.setText(getResources().getString(R.string.GER_switch_2));
			button3.setText(getResources().getString(R.string.GER_button_3));
			button4.setText(getResources().getString(R.string.GER_button_4));
			button5.setText(getResources().getString(R.string.GER_button_5));
			button6.setText(getResources().getString(R.string.GER_button_6));
		}
		
		if (SharedVariables.sending())
		{
			switch2.setChecked(true);
		}
		else
		{
			switch2.setChecked(false);
		}
	}
}
