package com.example.downloadswf;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	final String LOG_TAG = "myLogs";
	
	ProgressBar pbLoading;
	Button btnLoad;
	TextView tvUrl;
	EditText etUrl;
	EditText etClassName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLoad = (Button) findViewById(R.id.btnLoad);
		tvUrl = (TextView) findViewById(R.id.tvUrl);
		etUrl = (EditText) findViewById(R.id.etUrl);
		//etUrl.setText("http://stackoverflow.com"); 
		etUrl.setText("http://habrahabr.ru/");
		etClassName = (EditText) findViewById(R.id.etClassName);
		//etClassName.setText("question-hyperlink");
		etClassName.setText("content html_format");
	}
	
	public void onClickOk(View view){
		if (isNetworkAvaliable(this)){
			Intent intent = new Intent(this, FilesList.class);
			intent.putExtra(FilesList.URL_FOR_PARSING, etUrl.getText().toString()) ;
			intent.putExtra(FilesList.CLASS_NAME_FOR_PARSING, etClassName.getText().toString());
			startActivity(intent);
		} else {
			Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
		}
 	}
	
	public static boolean isNetworkAvaliable(Context context){
		return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
}