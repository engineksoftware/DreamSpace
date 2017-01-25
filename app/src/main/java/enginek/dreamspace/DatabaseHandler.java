package enginek.dreamspace;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandomSpi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "dreamsManager";

    private static final String TABLE_DREAMS = "dreams";
    private static final String TABLE_CONNECTIONS = "connections";


    //dreams table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DREAM = "dream";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_VECTOR = "vector";

    //connections table columns
    private static final String KEY_CONNECTION_ID = "id";
    private static final String KEY_DREAM_A_ID = "dreamA_id";
    private static final String KEY_DREAM_B_ID = "dreamB_id";
    private static final String KEY_ACCEPTED = "accepted";

    private static final double SIMILARITY_PERCENTAGE = 65.0;

    private static Context context;

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Creates the tables
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_DREAMS_TABLE = "CREATE TABLE " + TABLE_DREAMS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +
                KEY_DREAM + " TEXT," + KEY_TIME + " TEXT," +
                KEY_DATE + " TEXT," + KEY_VECTOR + " TEXT" + ")";

        String CREATE_CONNECTIONS_TABLE = "CREATE TABLE " + TABLE_CONNECTIONS + "(" +
                KEY_CONNECTION_ID + " INTEGER PRIMARY KEY," + KEY_DREAM_A_ID + " INTEGER," +
                KEY_DREAM_B_ID + " INTEGER," + KEY_ACCEPTED + " INTEGER" + ")";

        db.execSQL(CREATE_DREAMS_TABLE);
        db.execSQL(CREATE_CONNECTIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drops old table if it exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DREAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS);

        //Creates the new table
        onCreate(db);
    }

    public void addDream(Dream dream){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, dream.getTitle());
        values.put(KEY_DREAM, dream.getDream());
        values.put(KEY_TIME, dream.getTime());
        values.put(KEY_DATE, dream.getDate());
        values.put(KEY_VECTOR, findVectorOnSave(dream));

        db.insert(TABLE_DREAMS,null,values);

        compareToOtherDreamsOnSave(getLastAddedDream());
        checkStatistics(dream.getDate(),dream.getDream());

    }

    public void addConnection(Connection connection){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DREAM_A_ID, connection.getDreamA_id());
        values.put(KEY_DREAM_B_ID, connection.getDreamB_id());
        values.put(KEY_ACCEPTED, connection.getAccepted());

        db.insert(TABLE_CONNECTIONS,null,values);
    }

    public Dream getLastAddedDream(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_DREAMS + " ORDER BY "
                + KEY_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null)
            cursor.moveToFirst();

        Dream dream = new Dream();
        dream.setId(Integer.parseInt(cursor.getString(0)));
        dream.setTitle(cursor.getString(1));
        dream.setDream(cursor.getString(2));
        dream.setTime(cursor.getString(3));
        dream.setDate(cursor.getString(4));
        dream.setVector(cursor.getString(5));

        return dream;
    }

    public Dream getDream(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DREAMS,
                new String[] {KEY_ID,KEY_TITLE,KEY_DREAM,KEY_TIME,KEY_DATE,KEY_VECTOR}, KEY_ID + "=?",
                new String[] {String.valueOf(id)} , null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        Dream dream = new Dream(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        return dream;
    }

    public Connection getConnection(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONNECTIONS,
                new String[] {KEY_CONNECTION_ID,KEY_DREAM_A_ID,KEY_DREAM_B_ID,KEY_ACCEPTED}, KEY_CONNECTION_ID + "=?",
                new String[] {String.valueOf(id)} , null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        Connection connection = new Connection(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)));

        return connection;
    }

    public List<Dream> getDreams(){
        List<Dream> dreamList = new ArrayList<Dream>();

        String selectQuery = "SELECT * FROM " + TABLE_DREAMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Dream dream = new Dream();
                dream.setId(Integer.parseInt(cursor.getString(0)));
                dream.setTitle(cursor.getString(1));
                dream.setDream(cursor.getString(2));
                dream.setTime(cursor.getString(3));
                dream.setDate(cursor.getString(4));
                dream.setVector(cursor.getString(5));

                dreamList.add(dream);
            }while(cursor.moveToNext());
        }

        cursor.close();

        return dreamList;
    }

    public List<Connection> getConnections(){
        List<Connection> connections = new ArrayList<Connection>();

        String selectQuery = "SELECT * FROM " + TABLE_CONNECTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Connection connection = new Connection();
                connection.setConnection_id(Integer.parseInt(cursor.getString(0)));
                connection.setDreamA_id(Integer.parseInt(cursor.getString(1)));
                connection.setDreamB_id(Integer.parseInt(cursor.getString(2)));
                connection.setAccepted(Integer.parseInt(cursor.getString(3)));

                connections.add(connection);
            }while(cursor.moveToNext());
        }

        cursor.close();

        return connections;
    }

    public int getDreamCount(){
        String countQuery = "SELECT * FROM " + TABLE_DREAMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

    public int getConnectionsCount(){
        String countQuery = "SELECT * FROM " + TABLE_CONNECTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

    public int updateDream(Dream dream){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, dream.getTitle());
        values.put(KEY_DREAM, dream.getDream());
        values.put(KEY_TIME, dream.getTime());
        values.put(KEY_DATE, dream.getDate());
        values.put(KEY_VECTOR, findVectorOnSave(dream));

        dream.setVector(findVectorOnSave(dream));
        removeConnectionsThatContainDreamId(dream.getId());
        compareToOtherDreamsOnSave(dream);

        return db.update(TABLE_DREAMS, values, KEY_ID + " = ?",
                new String[]{ String.valueOf(dream.getId())});
    }

    public int updateConnection(Connection connection){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DREAM_A_ID, connection.getDreamA_id());
        values.put(KEY_DREAM_B_ID, connection.getDreamB_id());
        values.put(KEY_ACCEPTED, connection.getAccepted());

        return db.update(TABLE_CONNECTIONS, values, KEY_ID + " = ?",
                new String[]{ String.valueOf(connection.getConnection_id())});
    }

    public void deleteDream(Dream dream){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAMS, KEY_ID + " = ?",
                new String[] {String.valueOf(dream.getId())});

        removeConnectionsThatContainDreamId(dream.getId()); //Deletes any connections that contain that id.

        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.statistics_data), context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();

        int numberOfDreamsTotal = getDreamCount();//Gets the total number of dreams
        if(numberOfDreamsTotal == 0){//Checks if all dreams have been deleted.

            //If so, all of the counter values get set to zero, and it sets the first dream added to zero which means that no dreams have been added yet.
            prefEditor.putInt(context.getString(R.string.first_dream_added), 0);
            prefEditor.putInt(context.getString(R.string.current_day_counter), 0);
            prefEditor.putInt(context.getString(R.string.week_counter), 0);
            prefEditor.putInt(context.getString(R.string.month_counter), 0);
            prefEditor.putInt(context.getString(R.string.year_counter), 0);
            prefEditor.putInt(context.getString(R.string.total_amount_of_dreams), 0);
            prefEditor.putInt(context.getString(R.string.week_average), 0);
            prefEditor.putInt(context.getString(R.string.month_average), 0);
            prefEditor.putInt(context.getString(R.string.year_average), 0);
            prefEditor.putInt(context.getString(R.string.most_in_a_week), 0);
            prefEditor.putInt(context.getString(R.string.most_in_a_month), 0);
            prefEditor.putInt(context.getString(R.string.most_in_a_year), 0);
            prefEditor.putInt(context.getString(R.string.week_dream_counter), 0);
            prefEditor.putInt(context.getString(R.string.month_dream_counter), 0);
            prefEditor.putInt(context.getString(R.string.year_dream_counter), 0);
            prefEditor.putInt(context.getString(R.string.week_day_counter),0);
            prefEditor.apply();
        }else{
            int counterValue = pref.getInt(context.getString(R.string.current_day_counter), 0); //Gets the current day counter value
            int totalDreams = pref.getInt(context.getString(R.string.total_amount_of_dreams), 0); //Gets the total amount of dreams
            int weekDreamCounter = pref.getInt(context.getString(R.string.week_dream_counter), 0);
            int monthDreamCounter = pref.getInt(context.getString(R.string.month_dream_counter), 0);
            int yearDreamCounter = pref.getInt(context.getString(R.string.year_dream_counter), 0);
            counterValue--; //Subtracts one from that value
            totalDreams--; //Subtract from total amount of dreams.
            weekDreamCounter--;
            monthDreamCounter--;
            yearDreamCounter--;
            prefEditor.putInt(context.getString(R.string.total_amount_of_dreams), totalDreams); //update the total amount of dreams
            prefEditor.putInt(context.getString(R.string.current_day_counter), counterValue);//Sets the current day counter to the new value.
            prefEditor.putInt(context.getString(R.string.week_dream_counter), weekDreamCounter);
            prefEditor.putInt(context.getString(R.string.month_dream_counter), monthDreamCounter);
            prefEditor.putInt(context.getString(R.string.year_dream_counter), yearDreamCounter);
            prefEditor.apply();
        }



    }

    public void deleteAllDreams(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAMS, null, null);

        Intent intent = new Intent("DATE_RECEIVER");
        PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pintent);
        Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show();


        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.statistics_data), context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();

        //All of the counter values get set to zero, and it sets the first dream added to zero which means that no dreams have been added yet.
        prefEditor.putInt(context.getString(R.string.first_dream_added), 0);
        prefEditor.putInt(context.getString(R.string.current_day_counter), 0);
        prefEditor.putInt(context.getString(R.string.week_counter), 0);
        prefEditor.putInt(context.getString(R.string.month_counter), 0);
        prefEditor.putInt(context.getString(R.string.year_counter), 0);
        prefEditor.putInt(context.getString(R.string.total_amount_of_dreams), 0);
        prefEditor.putInt(context.getString(R.string.week_average), 0);
        prefEditor.putInt(context.getString(R.string.month_average), 0);
        prefEditor.putInt(context.getString(R.string.year_average), 0);
        prefEditor.putInt(context.getString(R.string.most_in_a_week), 0);
        prefEditor.putInt(context.getString(R.string.most_in_a_month), 0);
        prefEditor.putInt(context.getString(R.string.most_in_a_year), 0);
        prefEditor.putInt(context.getString(R.string.week_dream_counter), 0);
        prefEditor.putInt(context.getString(R.string.month_dream_counter), 0);
        prefEditor.putInt(context.getString(R.string.year_dream_counter), 0);
        prefEditor.putInt(context.getString(R.string.week_day_counter), 0);
        prefEditor.apply();
    }

    public void deleteConnection(Connection connection){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONNECTIONS, KEY_ID + " = ?",
                new String[] {String.valueOf(connection.getConnection_id())});

    }

    public void removeConnectionsThatContainDreamId(int dreamId){
        List<Connection> connections = getConnections();
        List<Connection> newConnections = new ArrayList<Connection>();

        for(int x = 0; x < connections.size(); x++){
            if(connections.get(x).getDreamA_id() == dreamId || connections.get(x).getDreamB_id() == dreamId)
                newConnections.add(connections.get(x));
        }

        if(newConnections.size() > 0){
            for(int x = 0; x < newConnections.size(); x++){
                deleteConnection(newConnections.get(x));
            }
        }
    }

    //Starts the statistics data once a dream has been added, and adds to the dream counter for that day.
    private void checkStatistics(String date, String dream){
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.statistics_data), context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        String year, month;
        year = date.substring(0,4);
        month = date.substring(5,7);

        //This makes sure that it only starts counting statistics once you've added a dream.
        //It checks if the first dream has been added, and if not it sets the current month and year.
        int firstDreamValue = pref.getInt(context.getString(R.string.first_dream_added), 0);
        if(firstDreamValue == 0){
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent("DATE_RECEIVER");
            PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), ((1000* 60 * 60) * 12), pintent);
            Toast.makeText(context, "Alarm Created", Toast.LENGTH_SHORT).show();

            prefEditor.putInt(context.getString(R.string.first_dream_added),1);
            prefEditor.putString(context.getString(R.string.current_month),month);
            prefEditor.putString(context.getString(R.string.current_year),year);
            prefEditor.putInt(context.getString(R.string.month_counter),0);
            prefEditor.putInt(context.getString(R.string.year_counter),0);
            prefEditor.apply();

        }

        //Adds one to the current day counter, and the total amount of dreams.
        //The DateReceiver will reset it back to zero once the day has changed.
        int oldDayCounterValue = pref.getInt(context.getString(R.string.current_day_counter), 0);
        int oldWeekCounterValue = pref.getInt(context.getString(R.string.week_dream_counter), 0);
        int oldMonthCounterValue = pref.getInt(context.getString(R.string.month_dream_counter), 0);
        int oldYearCounterValue = pref.getInt(context.getString(R.string.year_dream_counter), 0);
        int totalDreams = pref.getInt(context.getString(R.string.total_amount_of_dreams), 0);

        totalDreams++;
        oldDayCounterValue++;
        oldWeekCounterValue++;
        oldMonthCounterValue++;
        oldYearCounterValue++;

        prefEditor.putInt(context.getString(R.string.total_amount_of_dreams), totalDreams);
        prefEditor.putInt(context.getString(R.string.current_day_counter), oldDayCounterValue);
        prefEditor.putInt(context.getString(R.string.week_dream_counter), oldWeekCounterValue);
        prefEditor.putInt(context.getString(R.string.month_dream_counter), oldMonthCounterValue);
        prefEditor.putInt(context.getString(R.string.year_dream_counter), oldYearCounterValue);
        prefEditor.apply();



    }

    //Removes anything that's not a letter, and splits the string by spaces into an array.
    //It then uses a hashmap to count the occurrences of each word.
    private String findVectorOnSave(Dream dream){
        Map<String, Integer> occurences = new LinkedHashMap<String, Integer>();
        //Replaces all non alphanumeric characters but leaves spaces(\\s)
        String dreamOnlyLetters = dream.getDream().replaceAll("[^A-Za-z0-9\\s]", "");
        String lowerCased = dreamOnlyLetters.toLowerCase();
        String[] splitDream = lowerCased.split("\\s+");

        for(String word : splitDream){
            Integer count = occurences.get(word);
            if(count == null){
                count = 0;
            }

            occurences.put(word,count + 1);

        }

        //Uses a JSONObject to hold the data, then converts it to a string to be saved in the db.
        JSONObject map = new JSONObject();
        int index = 0;
        for(String word : occurences.keySet()){
            try{
                map.put(word,occurences.get(word));
            }catch(JSONException exception){
                System.out.println(exception);
            }

        }

        return map.toString();

    }

    private void compareToOtherDreamsOnSave(Dream dream){
        List<Dream> dreamList = this.getDreams();

        //do nothing if there's only one dream or none.
        if(dreamList.size() <= 1)
            return;

        for(int x  = 0; x < dreamList.size(); x++){
            if(dream.getId() != dreamList.get(x).getId()){
                try{
                    JSONObject dreamA = new JSONObject(dream.getVector());
                    JSONObject dreamB = new JSONObject(dreamList.get(x).getVector());
                    int[] vectorA, vectorB;
                    String[] wordList;
                    double similarity;

                    wordList = createWordList(dreamA, dreamB);
                    vectorA = createVector(wordList, dreamA);
                    vectorB = createVector(wordList, dreamB);

                    similarity = calculateSimilarity(vectorA, vectorB);

                    //if its over 60% similar it will create a connections.
                    if((similarity * 100) > SIMILARITY_PERCENTAGE){
                        //-1 means that it's not accepted yet.
                        Connection connection = new Connection(dream.getId(), dreamList.get(x).getId(), -1);
                        this.addConnection(connection);
                    }

                }catch (JSONException e ){
                    System.out.println(e);
                }
            }


        }
    }

    //Adds all of the words together. Uses a set so it will exclude repeated words.
    private String[] createWordList(JSONObject v1, JSONObject v2){

        Iterator<?> v1Keys = v1.keys();
        Iterator<?> v2Keys = v2.keys();
        Set<String> words = new HashSet<>();

        while(v1Keys.hasNext()){
            words.add((String)v1Keys.next());
        }

        while(v2Keys.hasNext()){
            words.add((String)v2Keys.next());
        }

        return words.toArray(new String[words.size()]);

    }

    //Creates a vector based on the words list and the data passed to it.
    private int[] createVector(String[] words, JSONObject obj){
        int[] occurences = new int[words.length];
        for(int x = 0; x < words.length;x++){
            if(!obj.has(words[x])){
                occurences[x] = 0;
            }else{

                try{
                    occurences[x] = obj.getInt(words[x]);
                }catch (JSONException e){
                    System.out.println(e);
                }

            }

        }

        return occurences;
    }

    //Uses cosine similarity to compare the two vectors.
    private double calculateSimilarity(int[] vectorA, int[] vectorB){
        int sumA = 0;
        int sumB = 0;
        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;

        //Finds the dot product.
        for(int x = 0; x < vectorA.length; x++){
            dotProduct = dotProduct + (vectorA[x] * vectorB[x]);
        }

        //finds the sum of vectorA
        for(int x = 0; x < vectorA.length; x++){
            sumA += (vectorA[x] * vectorA[x]);
        }

        //finds the sum of vectorB
        for(int x = 0; x < vectorA.length; x++){
            sumB += (vectorB[x] * vectorB[x]);
        }

        //finds the magnitude using the sums.
        magnitudeA = Math.sqrt(sumA);
        magnitudeB = Math.sqrt(sumB);

        return dotProduct / (magnitudeA * magnitudeB);

    }


}
