package br.com.edsb.dutils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class UPermissions {

    public static boolean checkPermission(String permission, Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void requestPermision(String[] permissions, Context context, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsToAskList = new ArrayList<>();
            for (String perm : permissions) {
                if (context.checkSelfPermission(perm) !=
                        PackageManager.PERMISSION_GRANTED) {
                    permissionsToAskList.add(perm);
                }
            }
            if (permissionsToAskList.size() != 0) {
                String[] permissionsToAsk = permissionsToAskList.toArray(new
                        String[permissionsToAskList.size()]);
                ActivityCompat.requestPermissions((Activity) context, permissionsToAsk, requestCode);
            }
        }
    }

    public static void requestPermission(String permission, Context context, int requestCode) {
        String[] arr = new String[]{permission};
        if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions((Activity) context, arr, requestCode);
        }
    }
}
