package comkiolk.github.fragmetservice;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask {

    private static final String TAG = "DownloadTask";
    public static final String TAG1 = "MyLogs";
    private Context mContext;
    private Button mButton;
    private String downloadUrl = "";
    private String downLoudFileName = "";

    public DownloadTask(Context pContext, Button pButton, String pDownloadUrl) {
        mContext = pContext;
        mButton = pButton;
        downloadUrl = pDownloadUrl;

        downLoudFileName = "Myapp.apk";
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File outPutFile;
        File apkStorage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mButton.setEnabled(false);
            mButton.setText("Down load start");
        }

        @Override
        protected void onPostExecute(Void pVoid) {
            try {
                if (outPutFile != null) {
                    mButton.setEnabled(true);
                    mButton.setText("Download completed");
                } else {
                    mButton.setText("Download failed");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mButton.setEnabled(true);
                            mButton.setText("Try download again");
                        }
                    }, 3000);
                    Log.d(TAG1, "Download file failed");
                }
            }catch (Exception pE){
                mButton.setText("Download failed");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mButton.setEnabled(true);
                        mButton.setText("Try download again");
                    }
                }, 3000);
                Log.d(TAG1, "Download file failed" + pE.getLocalizedMessage());
            }
            super.onPostExecute(pVoid);
        }

        @Override
        protected Void doInBackground(Void... pVoids) {
            try {
                URL mUrl = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    //some message
                }
                if (new CheckForSdCard().isCDCardPresent()) {
                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Downloads");
                } else {
                    Log.d(TAG1, "SD card not present");
                }
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.d(TAG1, "Directory created");
                }
                outPutFile = new File(apkStorage, downLoudFileName);
                if (!outPutFile.exists()) {
                    outPutFile.createNewFile();
                    Log.d(TAG1, "New file created");
                }
                FileOutputStream fOS = new FileOutputStream(outPutFile);
                InputStream in = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) != -1) {
                    fOS.write(buffer, 0, len1);
                }
                fOS.close();
                in.close();
            } catch (IOException pE) {
                pE.printStackTrace();
                outPutFile = null;
                Log.d(TAG1, "Error message: " + pE.getMessage());
            }

            return null;
        }
    }
}
