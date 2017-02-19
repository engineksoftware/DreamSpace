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

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "dreamsManager";

    private static final String TABLE_DREAMS = "dreams";
    private static final String TABLE_CONNECTIONS = "connections";
    private static final String TABLE_AVERAGES = "averages";
    private static final String TABLE_RECORDS = "records";
    private static final String TABLE_CURRENT = "current";
    private static final String TABLE_COUNTERS = "counters";


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

    //averages table columns
    private static final String KEY_AVERAGES_ID= "averages_id";
    private static final String KEY_WEEK_AVERAGE= "week_average";
    private static final String KEY_MONTH_AVERAGE= "month_average";
    private static final String KEY_YEAR_AVERAGE= "year_average";

    //record table columns
    private static final String KEY_RECORDS_ID= "records_id";
    private static final String KEY_WEEK_RECORD= "week_record";
    private static final String KEY_MONTH_RECORD= "month_record";
    private static final String KEY_YEAR_RECORD= "year_record";

    //current table columns
    private static final String KEY_CURRENTS_ID= "currents_id";
    private static final String KEY_WEEK_CURRENT= "week_current";
    private static final String KEY_MONTH_CURRENT= "month_current";
    private static final String KEY_YEAR_CURRENT= "year_current";

    //counters table columns
    private static final String KEY_COUNTERS_ID= "counters_id";
    private static final String KEY_DAY_COUNTER= "day_current";
    private static final String KEY_WEEK_COUNTER= "week_current";
    private static final String KEY_MONTH_COUNTER= "month_current";
    private static final String KEY_YEAR_COUNTER= "year_current";
    private static final String KEY_WEEK_TRACKER= "week_tracker";
    private static final String KEY_MONTH_TRACKER= "month_tracker";

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

        String CREATE_AVERAGES_TABLE = "CREATE TABLE " + TABLE_AVERAGES + "(" +
                KEY_AVERAGES_ID + " INTEGER PRIMARY KEY," +
                KEY_WEEK_AVERAGE + " INTEGER," + KEY_MONTH_AVERAGE + " INTEGER," +
                KEY_YEAR_AVERAGE + " INTEGER" + ")";

        String CREATE_RECORD_TABLE = "CREATE TABLE " + TABLE_RECORDS + "(" +
                KEY_RECORDS_ID + " INTEGER PRIMARY KEY," +
                KEY_WEEK_RECORD + " INTEGER," + KEY_MONTH_RECORD + " INTEGER," +
                KEY_YEAR_RECORD + " INTEGER" + ")";

        String CREATE_CURRENT_TABLE = "CREATE TABLE " + TABLE_CURRENT + "(" +
                KEY_CURRENTS_ID + " INTEGER PRIMARY KEY," +
                KEY_WEEK_CURRENT + " INTEGER," + KEY_MONTH_CURRENT + " INTEGER," +
                KEY_YEAR_CURRENT + " INTEGER" + ")";

        String CREATE_COUNTERS_TABLE = "CREATE TABLE " + TABLE_COUNTERS + "(" +
                KEY_COUNTERS_ID + " INTEGER PRIMARY KEY," + KEY_DAY_COUNTER + " INTEGER," +
                KEY_WEEK_COUNTER + " INTEGER," + KEY_MONTH_COUNTER + " INTEGER," +
                KEY_YEAR_COUNTER + " INTEGER," + KEY_WEEK_TRACKER + " INTEGER," +
                KEY_MONTH_TRACKER + " INTEGER" + ")";

        db.execSQL(CREATE_DREAMS_TABLE);
        db.execSQL(CREATE_CONNECTIONS_TABLE);
        db.execSQL(CREATE_AVERAGES_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
        db.execSQL(CREATE_CURRENT_TABLE);
        db.execSQL(CREATE_COUNTERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drops old table if it exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DREAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVERAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTERS);

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
        calendar.add(Calendar.DATE,1);
        Intent i = new Intent("DATE_RECEIVER");
        PendingIntent pintent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

        //Create all of the counters to begin tracking the statistics, and also initializes the current data, record data, and average data.
        if(getCounterData() == null){
            CounterData counterData = new CounterData();
            counterData.setDayCounter(0);
            counterData.setWeekCounter(0);
            counterData.setWeekTracker(0);
            counterData.setMonthTracker(0);
            counterData.setMonthCounter(0);
            counterData.setYearCounter(0);
            addCounterData(counterData);

            CurrentData currentData = new CurrentData();
            currentData.setWeek(0);
            currentData.setMonth(0);
            currentData.setYear(0);
            addCurrentData(currentData);

            RecordData recordData = new RecordData();
            recordData.setWeekRecord(0);
            recordData.setMonthRecord(0);
            recordData.setYearRecord(0);
            addRecordData(recordData);

            AverageData averageData = new AverageData();
            averageData.setWeek(0);
            averageData.setMonth(0);
            averageData.setYear(0);
            addAverageData(averageData);
        }

        //Adds to the current data
        CurrentData currentData = getCurrentData();
        currentData.setWeek(currentData.getWeek() + 1);
        currentData.setMonth(currentData.getMonth() + 1);
        currentData.setYear(currentData.getYear() + 1);
        updateCurrentData(currentData);

        compareToOtherDreamsOnSave(getLastAddedDream());

    }

    public void addAverageData(AverageData averageData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_AVERAGE, averageData.getWeek());
        values.put(KEY_MONTH_AVERAGE, averageData.getMonth());
        values.put(KEY_YEAR_AVERAGE, averageData.getYear());

        db.insert(TABLE_AVERAGES,null,values);
    }

    public void addRecordData(RecordData recordData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_RECORD, recordData.getWeekRecord());
        values.put(KEY_MONTH_RECORD, recordData.getMonthRecord());
        values.put(KEY_YEAR_RECORD, recordData.getYearRecord());

        db.insert(TABLE_RECORDS,null,values);
    }

    public void addCurrentData(CurrentData currentData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_CURRENT, currentData.getWeek());
        values.put(KEY_MONTH_CURRENT, currentData.getMonth());
        values.put(KEY_YEAR_CURRENT, currentData.getYear());

        db.insert(TABLE_CURRENT,null,values);
    }

    public void addCounterData(CounterData counterData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY_COUNTER, counterData.getDayCounter());
        values.put(KEY_WEEK_COUNTER, counterData.getWeekCounter());
        values.put(KEY_MONTH_COUNTER, counterData.getMonthCounter());
        values.put(KEY_YEAR_COUNTER, counterData.getYearCounter());
        values.put(KEY_WEEK_TRACKER, counterData.getWeekTracker());
        values.put(KEY_MONTH_TRACKER, counterData.getMonthTracker());

        db.insert(TABLE_COUNTERS,null,values);
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

        Dream dream = new Dream();

        Cursor cursor = db.query(TABLE_DREAMS,
                new String[] {KEY_ID,KEY_TITLE,KEY_DREAM,KEY_TIME,KEY_DATE,KEY_VECTOR}, KEY_ID + "=?",
                new String[] {String.valueOf(id)} , null, null, null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            dream = new Dream(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        }else{
            dream = null;
        }




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

    public AverageData getAverageData(){
        SQLiteDatabase db = this.getReadableDatabase();

        AverageData averageData = new AverageData();

        String query = "SELECT * FROM " + TABLE_AVERAGES + " where " + KEY_AVERAGES_ID + " = 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            averageData = new AverageData(Integer.parseInt(cursor.getString(0)),
                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3));

        }else{
            averageData = null;
        }

        return  averageData;

    }

    public RecordData getRecordData(){
        SQLiteDatabase db = this.getReadableDatabase();

        RecordData recordData = new RecordData();

        String query = "SELECT * FROM " + TABLE_RECORDS + " where " + KEY_RECORDS_ID + " = 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            recordData = new RecordData(Integer.parseInt(cursor.getString(0)),
                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3));

        }else{
            recordData = null;
        }

        return  recordData;
    }

    public CurrentData getCurrentData(){
        SQLiteDatabase db = this.getReadableDatabase();

        CurrentData currentData = new CurrentData();

        String query = "SELECT * FROM " + TABLE_CURRENT + " where " + KEY_CURRENTS_ID + " = 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            currentData = new CurrentData(Integer.parseInt(cursor.getString(0)),
                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3));

        }else{
            currentData = null;
        }

        return  currentData;
    }

    public CounterData getCounterData(){
        SQLiteDatabase db = this.getReadableDatabase();

        CounterData counterData = new CounterData();

        String query = "SELECT * FROM " + TABLE_COUNTERS + " where " + KEY_COUNTERS_ID + " = 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            counterData = new CounterData(Integer.parseInt(cursor.getString(0)),
                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));

        }else{
            counterData = null;
        }

        return  counterData;
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

        return db.update(TABLE_CONNECTIONS, values, KEY_CONNECTION_ID + " = ?",
                new String[]{ String.valueOf(connection.getConnection_id())});
    }

    public int updateAveragesData(AverageData averageData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_AVERAGE, averageData.getWeek());
        values.put(KEY_MONTH_AVERAGE, averageData.getMonth());
        values.put(KEY_YEAR_AVERAGE, averageData.getYear());

        return db.update(TABLE_AVERAGES, values, KEY_AVERAGES_ID + " = ?",
                new String[]{ String.valueOf(averageData.getId())});
    }

    public int updateRecordData(RecordData recordData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_RECORD, recordData.getWeekRecord());
        values.put(KEY_MONTH_RECORD, recordData.getMonthRecord());
        values.put(KEY_YEAR_RECORD, recordData.getYearRecord());

        return db.update(TABLE_RECORDS, values, KEY_RECORDS_ID + " = ?",
                new String[]{ String.valueOf(recordData.getId())});
    }

    public int updateCurrentData(CurrentData currentData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_CURRENT, currentData.getWeek());
        values.put(KEY_MONTH_CURRENT, currentData.getMonth());
        values.put(KEY_YEAR_CURRENT, currentData.getYear());

        return db.update(TABLE_CURRENT, values, KEY_CURRENTS_ID + " = ?",
                new String[]{ String.valueOf(currentData.getId())});
    }

    public int updateCounterData(CounterData counterData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY_COUNTER, counterData.getDayCounter());
        values.put(KEY_WEEK_COUNTER, counterData.getWeekCounter());
        values.put(KEY_MONTH_COUNTER, counterData.getMonthCounter());
        values.put(KEY_YEAR_COUNTER, counterData.getYearCounter());
        values.put(KEY_WEEK_TRACKER, counterData.getWeekTracker());
        values.put(KEY_MONTH_TRACKER, counterData.getMonthTracker());

        return db.update(TABLE_COUNTERS, values, KEY_COUNTERS_ID + " = ?",
                new String[]{ String.valueOf(counterData.getId())});
    }

    public void deleteDream(Dream dream){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAMS, KEY_ID + " = ?",
                new String[] {String.valueOf(dream.getId())});

        removeConnectionsThatContainDreamId(dream.getId()); //Deletes any connections that contain that id.

        //Removes one from the current data
        CurrentData currentData = getCurrentData();
        currentData.setWeek(currentData.getWeek() - 1);
        currentData.setMonth(currentData.getMonth() - 1);
        currentData.setYear(currentData.getYear() - 1);
        updateCurrentData(currentData);

        if(getDreamCount() == 0){//Checks if all dreams have been deleted.
            Intent intent = new Intent("DATE_RECEIVER");
            PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pintent);

            deleteStatisticsData();
        }



    }

    public void deleteStatisticsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AVERAGES, null, null);
        db.delete(TABLE_RECORDS, null, null);
        db.delete(TABLE_CURRENT, null, null);
        db.delete(TABLE_COUNTERS, null, null);
    }

    public void deleteAllDreams(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAMS, null, null);

        deleteAllConnections();
        deleteStatisticsData();

        Intent intent = new Intent("DATE_RECEIVER");
        PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pintent);
    }

    public void deleteConnection(Connection connection){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONNECTIONS, KEY_ID + " = ?",
                new String[] {String.valueOf(connection.getConnection_id())});

    }

    public void deleteAllConnections(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONNECTIONS, null, null);
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
