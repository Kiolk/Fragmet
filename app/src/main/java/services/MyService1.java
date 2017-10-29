package services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import comkiolk.github.fragmetservice.MainActivity;

public class MyService1 extends Service {

    public static final String TAG = "MyLogs";
    ExecutorService mExecutorService;
//    Object mObject;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "My service onCreate");
        mExecutorService = Executors.newFixedThreadPool(1);
//        mObject = new Object();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,  "onStartCommand");
        int time = intent.getIntExtra(MainActivity.PARAMETR_TIME, 0);
        PendingIntent pI = intent.getParcelableExtra(MainActivity.PENDING_INTENT);
        MyRun myRun = new MyRun(time, startId, pI);
        mExecutorService.execute(myRun);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, getPackageName() + "onDestroy");
//        mObject =null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    class MyRun implements Runnable{

        int time;
        int startId;
        PendingIntent pI;

        public MyRun(int pTime, int pStartId, PendingIntent pV) {
            time = pTime;
            startId = pStartId;
            pI = pV;
            Log.d(TAG, "MyRun# " + pStartId + " creat");
        }

        @Override
        public void run() {
            Log.d(TAG, "MyRun# " + startId + " start. Time = " + time);
            try {
                pI.send(MainActivity.STATUS_START);

                TimeUnit.SECONDS.sleep(time);

//                Intent intent = new Intent().putExtra(MainActivity.PARAMETR_RESULT, time * 100);
//                pI.send(MyService1.this, MainActivity.STATUS_FINISH, intent);
                pI.send(MainActivity.STATUS_FINISH);
            } catch (InterruptedException pE) {
                pE.printStackTrace();
            } catch (PendingIntent.CanceledException pE) {
                pE.printStackTrace();
            }
            try {
                Log.d(TAG, "MyRun# " + startId + " mObject = ");//
            }catch (NullPointerException pE){
                pE.printStackTrace();
                Log.d(TAG, "MyRun# " + startId + pE.getMessage() + "Error, Null pointer");
            }
            stop();
        }

        public void stop(){
            Log.d(TAG, "MyRun# " + startId + "stopped");
            stopSelf();
        }
    }
}
