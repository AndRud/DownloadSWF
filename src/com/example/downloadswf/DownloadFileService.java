package com.example.downloadswf;

import java.io.BufferedInputStream; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class DownloadFileService extends Service{

	private static final String LOG_TAG = "myLogs";
	private static final String DIRECTORY_NAME = "/DownloadFiles";
	
	private DownloadTask downloadTask;
	
	public void download(String url){
		if(downloadTask == null) 
			downloadTask = new DownloadTask();;
		downloadTask.execute(url);
	}
	
	public void onCreate(){
		super.onCreate();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		download("http://gamegame.2974600.pix-cdn.org/4876.jpg");
		return START_STICKY;
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, Integer>{
		
		@Override
		protected Integer doInBackground(String... url) {
			downloadFromURL(url[0]);
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values){
			super.onProgressUpdate(values);
		
		}
		
		private void downloadFromURL(String _url){
			try {
			       File root = android.os.Environment.getExternalStorageDirectory();
			       File dir = new File (root.getAbsolutePath() + DIRECTORY_NAME);
			       if(dir.exists()==false) {
			            dir.mkdirs();
			       }

			       //URL url = new URL(_url); //you can write here any link
			       URL url = new URL("http://gamegame.2970143.pix-cdn.org/games/13613.swf");
			       
			       Uri uriName = Uri.parse(_url);
			       String fileName = uriName.getLastPathSegment();
			       
			       File file = new File(dir, fileName);

			       long startTime = System.currentTimeMillis();
			       Log.d(LOG_TAG, "download url:" + url);

			       /* Open a connection to that URL. */
			       URLConnection ucon = url.openConnection();
			       
			       /*
			        * Define InputStreams to read from the URLConnection.
			        */
			       InputStream is = ucon.getInputStream();
			       BufferedInputStream bis = new BufferedInputStream(is);

			       /*
			        * Read bytes to the Buffer until there is nothing more to read(-1).
			        */
			       ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			       int current = 0;
			       while ((current = bis.read()) != -1) {
			          baf.append((byte) current);
			       }

			       /* Convert the Bytes read to a String. */
			       FileOutputStream fos = new FileOutputStream(file);
			       fos.write(baf.toByteArray());
			       fos.flush();
			       fos.close();
			       Log.d(LOG_TAG, "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
			  } catch (IOException e) {
			   Log.d(LOG_TAG, "Error: " + e);
			  }
			stopSelf();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
