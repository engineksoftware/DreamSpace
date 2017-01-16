package enginek.dreamspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

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
    Context context;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;

            if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
                pref = context.getSharedPreferences(context.getString(R.string.statistics_data), 0);
                editor = pref.edit();

                if(pref.getInt(context.getString(R.string.first_dream_added),context.MODE_PRIVATE) == 1){
                    //Gets the current year and month.
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(calendar.getTime());
                    year = date.substring(0,4);
                    month = date.substring(5,7);

                    int currentDayCounter = pref.getInt(context.getString(R.string.current_day_counter), 0); //Gets the number of dreams added yesterday.
                    totalDreams = pref.getInt(context.getString(R.string.total_amount_of_dreams), 0); //Gets the total number of dreams.

                    editor.putInt(context.getString(R.string.current_day_counter), 0); //Resets the current day counter back to zero.
                    editor.commit();

                    int weekDayCounter = pref.getInt(context.getString(R.string.week_day_counter), -1); //Gets the week day counter
                    if(weekDayCounter == -1){ //Checks if the week day counter is set to its default value
                        weekDayCounter = 0; //If so, week day counter gets set to zero because the value will be added to later on.
                    }

                    if(weekDayCounter == 7){ //Checks if the week day counter is equal to seven.
                        newWeek(); //If so, a new week has started and it calls the newWeek method.
                    }else{
                        weekDayCounter += 1; //If not, one is added to the week day counter.
                        editor.putInt(context.getString(R.string.week_day_counter), weekDayCounter); //Sets the weekday counter to its new value.
                        editor.commit();
                    }

                    String currentYear = pref.getString(context.getString(R.string.current_year), ""); //Gets the current year to be compared to the new year after the date changed.
                    String currentMonth = pref.getString(context.getString(R.string.current_month), ""); //Gets the current month to be compared to the new year after the date changed.

                    if(!currentMonth.equals(month)){ //Compares the current month and the new month.
                        newMonth(); //Calls the newMonth method if it isn't
                    }else{
                        int monthDreamCounter = pref.getInt(context.getString(R.string.month_dream_counter), -1);

                        if(monthDreamCounter == -1){//Checks if the dream counter for that month is set to its default value (which means it hasn't been set at all).
                            monthDreamCounter = 0;//If it is, it gets set to zero.
                        }

                        editor.putInt(context.getString(R.string.month_dream_counter), monthDreamCounter); //Sets the month dream counter to its new value.
                        editor.commit();
                    }

                    if(!currentYear.equals(year)){ //Compares the current year and the new year
                        newYear(); //Calls the newYear method if it isn't.
                    }else{
                        int yearDreamCounter = pref.getInt(context.getString(R.string.year_dream_counter), -1);

                        if(yearDreamCounter == -1){//Checks if the dream counter for that year is set to its default value (which means it hasn't been set at all).
                            yearDreamCounter = 0;//If it is, it gets set to zero.
                        }

                        editor.putInt(context.getString(R.string.year_dream_counter), yearDreamCounter); //Sets the year dream counter to its new value.
                        editor.commit();
                    }



                }

            }
        }

    private void newWeek(){
        //Gets the total num of weeks since the first post, and adds one to it.
        int weekCounter = pref.getInt(context.getString(R.string.week_counter), 0);
        weekCounter += 1;

        //finds the average by dividing the total number of dreams by the total number of weeks.
        int average = totalDreams / weekCounter;

        int weekDreamCounter = pref.getInt(context.getString(R.string.week_dream_counter), -1); //Gets the weekDreamCounter value
        int oldWeekMost = pref.getInt(context.getString(R.string.most_in_a_week), 0); //Gets the old most in one week value
        if(weekDreamCounter > oldWeekMost){ //Checks if the week dream counter is larger than it.
            editor.putInt(context.getString(R.string.most_in_a_week), weekDreamCounter);//If so, it changes the value to the week dream counter.
        }
        editor.putInt(context.getString(R.string.week_dream_counter), 0);//Resets the week dream counter back to zero.

        editor.putInt(context.getString(R.string.week_counter), weekCounter); //Sets the new total number of weeks.
        editor.putInt(context.getString(R.string.week_day_counter), 0); //Resets the week day counter back to zero.
        editor.putInt(context.getString(R.string.week_average), average); //Sets the new average number of dreams per week.
        editor.commit();


    }

    private void newMonth(){
        int monthCounter = pref.getInt(context.getString(R.string.month_counter), 0); //Get the old value for the month counter.

        monthCounter++; //Adds one to old month value.
        int average = totalDreams / monthCounter;

        int monthDreamCounter = pref.getInt(context.getString(R.string.month_dream_counter), -1); //Gets the month dream counter value
        int oldMonthMost = pref.getInt(context.getString(R.string.most_in_a_month), 0); //Gets the old most in one month value
        if(monthDreamCounter > oldMonthMost){ //Checks if the month dream counter is larger than it.
            editor.putInt(context.getString(R.string.most_in_a_month), monthDreamCounter);//If so, it changes the value to the month dream counter.
        }
        editor.putInt(context.getString(R.string.month_dream_counter), 0);//Resets the month dream counter back to zero.

        editor.putInt(context.getString(R.string.month_counter), monthCounter); //Sets the month counter to the new value.
        editor.putString(context.getString(R.string.current_month), month); //Sets the current month to the new month
        editor.putInt(context.getString(R.string.month_average), average); //Sets the average per month
        editor.commit();
    }

    private void newYear(){
        int yearCounter = pref.getInt(context.getString(R.string.year_counter), 0); //Get the old value for the year counter.

        yearCounter++; //Adds one to old year value.
        int average = totalDreams / yearCounter;

        int yearDreamCounter = pref.getInt(context.getString(R.string.year_dream_counter), -1); //Gets the year dream counter value
        int oldYearMost = pref.getInt(context.getString(R.string.most_in_a_year), 0); //Gets the old most in one year value
        if(yearDreamCounter > oldYearMost){ //Checks if the year dream counter is larger than it.
            editor.putInt(context.getString(R.string.most_in_a_year), yearDreamCounter);//If so, it changes the value to the year dream counter.
        }
        editor.putInt(context.getString(R.string.year_dream_counter), 0);//Resets the year dream counter back to zero.

        editor.putInt(context.getString(R.string.year_counter), yearCounter); //Sets the year counter to the new value.
        editor.putString(context.getString(R.string.current_year), year); //Sets the current year to the new year.
        editor.putInt(context.getString(R.string.year_average), average); //Sets the average per year.
        editor.commit();
    }

}
