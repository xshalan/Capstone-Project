package app.com.shalan.spacego.Handler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by noura on 12/08/2017.
 */

public class Utils {
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void askPermissionsForStorage(Activity activity) {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static void askPermissionsForLocatoin(Activity activity){
        String[] permissions = {
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION"
        };
        int requestCode = 200;
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean AskForPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
