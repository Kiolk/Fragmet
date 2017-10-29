package comkiolk.github.fragmetservice;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import fragments.Fragment1;
import fragments.Fragment2;

public class MainActivity extends AppCompatActivity {

    public static final String MY_LOGS = "MyLogs";
    Fragment1 mFragment1;
    Fragment2 mFragment2;
    FragmentTransaction mTransaction;
    CheckBox mCheckBox;
    Button mAdd;
    Button mRem;
    Button mRep;



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
    }
}
