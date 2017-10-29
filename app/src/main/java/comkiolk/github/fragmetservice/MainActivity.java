package comkiolk.github.fragmetservice;

import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import fragments.Fragment1;
import fragments.Fragment2;
import services.MyService1;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "https://github.com/Kiolk/HelloWorld/blob/master/app-Amazon-release%5B1%5D.apk?raw=true";
    public static final String MY_LOGS = "MyLogs";
    public static final int  REQUEST_CODE_1 = 1;
    public static final int  REQUEST_CODE_2 = 2;
    public static final int  REQUEST_CODE_3 = 3;
    public static final String PARAMETR_TIME = "time";
    public static final String PENDING_INTENT = "pendingIntent";
    public static final int STATUS_START = 100;
    public static final int STATUS_FINISH = 200;
    public static final String PARAMETR_RESULT = "parametr_result";
    Fragment1 mFragment1;
    Fragment2 mFragment2;
    FragmentTransaction mTransaction;
    CheckBox mCheckBox;
    Button mAdd;
    Button mRem;
    Button mRep;
    Button mStartService1Button;
    TextView mText1;
    TextView mText2;
    TextView mText3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MY_LOGS, "ocCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment1 = new Fragment1();
        mFragment2 = new Fragment2();
        mCheckBox = (CheckBox) findViewById(R.id.stack_check_box);
        mAdd = (Button) findViewById(R.id.add_fragment_button);
        mRem = (Button) findViewById(R.id.del_fragment_button);
        mRep = (Button) findViewById(R.id.repl_fragment_button);
        mStartService1Button = (Button) findViewById(R.id.start_service_1_button);
        mText1 = (TextView) findViewById(R.id.text_1_text_view);
        mText2 = (TextView) findViewById(R.id.text_2_text_view);
        mText2 = (TextView) findViewById(R.id.text_3_text_view);

        View.OnClickListener clickBtn = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                mTransaction = getFragmentManager().beginTransaction();
                switch (pView.getId()){
                    case R.id.add_fragment_button:
                        mTransaction.add(R.id.container, mFragment1);
                        break;
                    case R.id.del_fragment_button:
                        mTransaction.remove(mFragment1);
                        break;
                    case R.id.repl_fragment_button:
                        mTransaction.replace(R.id.container, mFragment2);
                        openFolder();
                        break;
                    default:
                        break;
                }
                if(mCheckBox.isChecked())mTransaction.addToBackStack(null);
                mTransaction.commit();
            }
        };

        mAdd.setOnClickListener(clickBtn);
        mRem.setOnClickListener(clickBtn);
        mRep.setOnClickListener(clickBtn);
        mStartService1Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                Log.d(MY_LOGS, "onClick");
//                PendingIntent pI;
//                Intent intent;
//                pI = createPendingResult(REQUEST_CODE_1, new Intent(), PendingIntent.FLAG_ONE_SHOT);
//                intent = new Intent(MainActivity.this, MyService1.class).putExtra(PARAMETR_TIME, 8).putExtra(PENDING_INTENT, pI);
//                startService(intent);
//
//                pI = createPendingResult(REQUEST_CODE_2, new Intent(), PendingIntent.FLAG_ONE_SHOT);
//                intent = new Intent(MainActivity.this, MyService1.class).putExtra(PARAMETR_TIME, 2).putExtra(PENDING_INTENT, pI);
//                startService(intent);
//
//                pI = createPendingResult(REQUEST_CODE_3, new Intent(), PendingIntent.FLAG_ONE_SHOT);
//                intent = new Intent(MainActivity.this, MyService1.class).putExtra(PARAMETR_TIME, 5).putExtra(PENDING_INTENT, pI);
//                startService(intent);
                if(isConnectionToInternet()) {
                    new DownloadTask(MainActivity.this, mStartService1Button, URL);
                }else{
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(MY_LOGS, "RequestCode = " + requestCode + ". ResultCode " + resultCode);

        if(resultCode == STATUS_START){
            switch (requestCode){
                case REQUEST_CODE_1:
                    mText1.setText(R.string.TASK1_START);
                    break;
                case REQUEST_CODE_2:
                    mText2.setText(R.string.TASK2_START);
                    break;
                case REQUEST_CODE_3:
                    mText3.setText(R.string.TASK3_START);
                    break;
                default:
                    break;
            }

        }
        if(resultCode == STATUS_FINISH){
            //int result = data.getIntExtra(PARAMETR_RESULT, 0);
            switch (requestCode){
                case REQUEST_CODE_1:
                    mText1.setText(R.string.TASK1_FINISH);// + " result=" + result);
                    break;
                case REQUEST_CODE_2:
                    mText2.setText(R.string.TASK2_FINISH);// + " result=" + result);
                    break;
                case REQUEST_CODE_3:
                    mText3.setText(R.string.TASK3_FINISH);//+ " result=" + result);
                    break;
                default:
                    break;
            }
        }
    }
    public boolean isConnectionToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
return true;
        }
        return false;

    }

    public void openFolder(){
        if(new CheckForSdCard().isCDCardPresent()){
            File apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Downloads");
            if(!apkStorage.exists()){
                Toast.makeText(MainActivity.this, "File not exist", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/" + "Downloads");
            intent.setDataAndType(uri, "*/*");
            startActivity(Intent.createChooser(intent, "Open Download Folder"));
        }else{
            Toast.makeText(MainActivity.this, "SD card not present", Toast.LENGTH_LONG).show();
        }

    }
}
