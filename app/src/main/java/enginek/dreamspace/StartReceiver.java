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

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){


            //eadawsdsadgh
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.add(Calendar.DATE,1);
                Intent i = new Intent("DATE_RECEIVER");
                PendingIntent pintent = PendingIntent.getBroadcast(context, 0, i, 0);
                AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);


        }
    }
}
