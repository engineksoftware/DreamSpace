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

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int totalDreams;
    String year, month;

        @Override
        public void onReceive(Context context, Intent intent) {

                pref = context.getSharedPreferences(context.getString(R.string.statistics_data), 0);
                editor = pref.edit();

                if(pref.getInt(context.getString(R.string.first_dream_added),context.MODE_PRIVATE) == 1){
                    //Gets the current year and month.
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(calendar.getTime());
                    year = date.substring(0,4);
                    month = date.substring(5,7);

                    totalDreams = pref.getInt(context.getString(R.string.total_amount_of_dreams), 0); //Gets the total number of dreams.

                    editor.putInt(context.getString(R.string.current_day_counter), 0); //Resets the current day counter back to zero.

                    int weekDayCounter = pref.getInt(context.getString(R.string.week_day_counter), 0); //Gets the week day counter

                    if(weekDayCounter == 1){ //Checks if the week day counter is equal to seven.
                        //Gets the total num of weeks since the first post, and adds one to it.
                        int weekCounter = pref.getInt(context.getString(R.string.week_counter), 0);
                        weekCounter++;

                        //finds the average by dividing the total number of dreams by the total number of weeks.
                        int average = totalDreams / weekCounter;

                        int weekDreamCounter = pref.getInt(context.getString(R.string.week_dream_counter), 0); //Gets the weekDreamCounter value
                        int oldWeekMost = pref.getInt(context.getString(R.string.most_in_a_week), 0); //Gets the old most in one week value
                        if(weekDreamCounter > oldWeekMost){ //Checks if the week dream counter is larger than it.
                            editor.putInt(context.getString(R.string.most_in_a_week), weekDreamCounter);//If so, it changes the value to the week dream counter.
                        }
                        editor.putInt(context.getString(R.string.week_dream_counter), 0);//Resets the week dream counter back to zero.

                        editor.putInt(context.getString(R.string.week_counter), weekCounter); //Sets the new total number of weeks.
                        editor.putInt(context.getString(R.string.week_day_counter), 0); //Resets the week day counter back to zero.
                        editor.putInt(context.getString(R.string.week_average), average); //Sets the new average number of dreams per week.
                        editor.putInt(context.getString(R.string.week_dream_counter), 0);
                    }else{
                        weekDayCounter += 1; //If not, one is added to the week day counter.
                        editor.putInt(context.getString(R.string.week_day_counter), weekDayCounter); //Sets the weekday counter to its new value.
                    }

                    String currentYear = pref.getString(context.getString(R.string.current_year), ""); //Gets the current year to be compared to the new year after the date changed.
                    String currentMonth = pref.getString(context.getString(R.string.current_month), ""); //Gets the current month to be compared to the new year after the date changed.

                    if(!currentMonth.equals(month)){ //Compares the current month and the new month.
                        int monthCounter = pref.getInt(context.getString(R.string.month_counter), 0); //Get the old value for the month counter.

                        monthCounter++; //Adds one to old month value.
                        int average = totalDreams / monthCounter;

                        int monthDreamCounter = pref.getInt(context.getString(R.string.month_dream_counter), 0); //Gets the month dream counter value
                        int oldMonthMost = pref.getInt(context.getString(R.string.most_in_a_month), 0); //Gets the old most in one month value
                        if(monthDreamCounter > oldMonthMost){ //Checks if the month dream counter is larger than it.
                            editor.putInt(context.getString(R.string.most_in_a_month), monthDreamCounter);//If so, it changes the value to the month dream counter.
                        }
                        editor.putInt(context.getString(R.string.month_dream_counter), 0);//Resets the month dream counter back to zero.

                        editor.putInt(context.getString(R.string.month_counter), monthCounter); //Sets the month counter to the new value.
                        editor.putString(context.getString(R.string.current_month), month); //Sets the current month to the new month
                        editor.putInt(context.getString(R.string.month_average), average); //Sets the average per month
                        editor.putInt(context.getString(R.string.month_dream_counter), 0);
                    }

                    if(!currentYear.equals(year)){ //Compares the current year and the new year
                        int yearCounter = pref.getInt(context.getString(R.string.year_counter), 0); //Get the old value for the year counter.

                        yearCounter++; //Adds one to old year value.
                        int average = totalDreams / yearCounter;

                        int yearDreamCounter = pref.getInt(context.getString(R.string.year_dream_counter), 0); //Gets the year dream counter value
                        int oldYearMost = pref.getInt(context.getString(R.string.most_in_a_year), 0); //Gets the old most in one year value
                        if(yearDreamCounter > oldYearMost){ //Checks if the year dream counter is larger than it.
                            editor.putInt(context.getString(R.string.most_in_a_year), yearDreamCounter);//If so, it changes the value to the year dream counter.
                        }
                        editor.putInt(context.getString(R.string.year_dream_counter), 0);//Resets the year dream counter back to zero.

                        editor.putInt(context.getString(R.string.year_counter), yearCounter); //Sets the year counter to the new value.
                        editor.putString(context.getString(R.string.current_year), year); //Sets the current year to the new year.
                        editor.putInt(context.getString(R.string.year_average), average); //Sets the average per year.
                        editor.putInt(context.getString(R.string.year_dream_counter), 0);
                    }

                    editor.commit();

                }

            }


}
