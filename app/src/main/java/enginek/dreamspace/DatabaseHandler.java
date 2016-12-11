package enginek.dreamspace;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dreamsManager";

    private static final String TABLE_DREAMS = "dreams";
    private static final String TABLE_CONNECTIONS = "dreams";


    //Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DREAM = "dream";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_VECTOR = "vector";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates the tables
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_DREAMS_TABLE = "CREATE TABLE " + TABLE_DREAMS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +
                KEY_DREAM + " TEXT," + KEY_TIME + " TEXT," +
                KEY_DATE + " TEXT," + KEY_VECTOR + " TEXT" + ")";
        db.execSQL(CREATE_DREAMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drops old table if it exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DREAMS);

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
        db.close();
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

        return dreamList;
    }

    public int getDreamCount(){
        String countQuery = "SELECT * FROM " + TABLE_DREAMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(countQuery,null);
        cursor.close();

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

        return db.update(TABLE_DREAMS, values, KEY_ID + " = ?",
                new String[]{ String.valueOf(dream.getId())});
    }

    public void deleteDream(Dream dream){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAMS, KEY_ID + " = ?",
                new String[] {String.valueOf(dream.getId())});
        db.close();
    }

    //Removes anything that's not a letter, and splits the string by spaces into an array.
    //It then uses a hashmap to count the occurences of each word.
    public String findVectorOnSave(Dream dream){
        Map<String, Integer> occurences = new LinkedHashMap<String, Integer>();
        String dreamOnlyLetters = dream.getDream().replaceAll("[^A-Za-z0-9]/", "");
        String[] splitDream = dream.getDream().split("\\s+");

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

        /*//Creates an int array using the number of occurences created before.
        //Going to be used as the vector array for cosine similarity.
        int[] vectorArray = new int[occurences.size()];
        int index = 0;
        for(String word : occurences.keySet()){
            vectorArray[index] = occurences.get(word);
            index ++;
        }

        return Arrays.toString(vectorArray);*/
    }
}
