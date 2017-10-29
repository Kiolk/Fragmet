package comkiolk.github.fragmetservice;

import android.os.Environment;

public class CheckForSdCard {

    public boolean isCDCardPresent(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

}
