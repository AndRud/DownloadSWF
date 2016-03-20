package com.example.downloadswf;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlcleaner.TagNode;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle; 
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FilesList extends ActionBarActivity{
	
	public static final String URL_FOR_PARSING = "url_for_parsing";
	public static final String CLASS_NAME_FOR_PARSING = "class_name_for_parsing";
	
	private String parseUrl;
	private String className;
	
	private ParseSite parseSite;
	ListView lvFiles;
	TextView tvNoData;
	
	ProgressDialog pdParcer;
	
	private long enqueue;
	private DownloadManager dm;
	
	BroadcastReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files_list);
		
		lvFiles = (ListView) findViewById(R.id.lvFiles);
		lvFiles.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		tvNoData.setVisibility(TextView.GONE);
		
		parseUrl = getIntent().getExtras().getString(URL_FOR_PARSING);
		className = getIntent().getExtras().getString(CLASS_NAME_FOR_PARSING);
		
		parseSite(parseUrl, className);
		
		receiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
					long dowloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
					Query query = new Query();
					query.setFilterById(enqueue);
					Cursor c = dm.query(query);
					/*if(c.moveToFirst()){
						int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
						if(DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex))
					}*/						
				}
			}
			
		};
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	private void startDownloading(){
		dm = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
		Request request = new Request(Uri.parse("http://www.game-game.com.ua/ui/css/images/spacer.gif"));
		enqueue = dm.enqueue(request);
	}
	
	private void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
	
	private void parseSite(String url, String className){
		parseSite = new ParseSite();
		parseSite.execute(url, className);
	}
	
	private class ParseSite extends AsyncTask<String, Integer, List<String>>{

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> output = new ArrayList<String>();
			try {
				HTMLHelper htmlHelper = new HTMLHelper(new URL(params[0]));
				List<TagNode> links = htmlHelper.getLinksByClass(params[1]);
				
				for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();){
					TagNode divElement = (TagNode) iterator.next();
					String convertStr = divElement.getAttributeByName("src").toString();
					//String convertStr = divElement.getText().toString().trim();
					output.add(convertStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return output;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values){
			super.onProgressUpdate(values);
		}
		
		protected void onPreExecute(){
			super.onPreExecute();
			pdParcer = new ProgressDialog(FilesList.this);
			pdParcer.setTitle("Working");
			pdParcer.setMessage("Parsing URL: " + parseUrl);
			pdParcer.setCanceledOnTouchOutside(false);
			pdParcer.setCancelable(false);
			pdParcer.show();
		}
		
		protected void onPostExecute(List<String> output){
			pdParcer.dismiss();
			if (!output.isEmpty()){
				tvNoData.setVisibility(TextView.GONE);
				lvFiles.setVisibility(ListView.VISIBLE);
				lvFiles.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
						R.layout.item, output));
			} else {
				lvFiles.setVisibility(ListView.GONE);
				tvNoData.setVisibility(TextView.VISIBLE);
			}			
		}		
	}

}
