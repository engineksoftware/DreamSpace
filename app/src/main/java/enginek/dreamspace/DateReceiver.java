package enginek.dreamspace;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.security.cert.PolicyNode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;

/**
 * Created by Joseph on 1/3/2017.
 */
public class DateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DatabaseHandler db = new DatabaseHandler(context);

            AverageData averageData = db.getAverageData();
            RecordData recordData = db.getRecordData();
            CurrentData currentData = db.getCurrentData();
            CounterData counterData = db.getCounterData();

            //Add to day counter, and then check it.
            counterData.setDayCounter(counterData.getDayCounter()+1);
            //Whens theres a new week.
            if(counterData.getDayCounter() == 6){
                //Adds to the week tracker and counter
                counterData.setWeekTracker(counterData.getWeekTracker()+1);
                counterData.setWeekCounter(counterData.getWeekCounter()+1);


                //Finds the weekly average
                if(counterData.getWeekCounter() > db.getDreamCount())
                    averageData.setWeek(0);
                else
                    averageData.setWeek(db.getDreamCount() / counterData.getWeekCounter());

                //Checks to see if this week had more dreams than the last, and then sets the record data.
                if(currentData.getWeek() > recordData.getWeekRecord())
                    recordData.setWeekRecord(currentData.getWeek());

                //Resets the current week data, and the day counter
                currentData.setWeek(0);
                counterData.setDayCounter(0);

            }

            //When theres a new month.
            //Check week counter
            if(counterData.getWeekTracker() == 3){
                //Resets the week tracker
                counterData.setWeekTracker(0);

                //Adds to the counters
                counterData.setMonthCounter(counterData.getMonthCounter()+1);
                counterData.setMonthTracker(counterData.getMonthTracker()+1);

                //Finds the monthly average
                if(counterData.getMonthTracker() > db.getDreamCount())
                    averageData.setMonth(0);
                else
                    averageData.setMonth(db.getDreamCount() / counterData.getMonthCounter());

                //Checks to see if this month has more than the last, and then sets the record data.
                if(currentData.getMonth() > recordData.getMonthRecord())
                    recordData.setMonthRecord(currentData.getMonth());

                //Resets the current month data
                currentData.setMonth(0);
            }

            //Whens theres a new year.
            //Check month tracker.
            if(counterData.getMonthTracker() == 12){
                //Resets the week tracker
                counterData.setMonthTracker(0);

                //Adds the counter
                counterData.setYearCounter(counterData.getYearCounter()+1);

                //Finds the yearly average
                if(counterData.getYearCounter() > db.getDreamCount())
                    averageData.setYear(0);
                else
                averageData.setYear(db.getDreamCount() / counterData.getYearCounter());

                //Checks to see if this year has more than the last, and then sets the record data.
                if(currentData.getYear() > recordData.getYearRecord())
                    recordData.setYearRecord(currentData.getYear());

                //Resets the current year data
                currentData.setYear(0);
            }

            //Updates the database with the new values.
            db.updateAveragesData(averageData);
            db.updateRecordData(recordData);
            db.updateCurrentData(currentData);
            db.updateCounterData(counterData);





        }


}
