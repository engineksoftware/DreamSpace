package enginek.dreamspace;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by Joseph on 1/24/2017.
 */
public class StartReceiver extends BroadcastReceiver {

    private static final String QUICKBOOT = "android.intent.action.QUICKBOOT_POWERON";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(QUICKBOOT)){
            Intent service = new Intent(context, StartService.class);
            context.startService(service);
        }
    }
}
