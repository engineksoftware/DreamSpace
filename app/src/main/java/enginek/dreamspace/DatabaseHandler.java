package enginek.dreamspace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dreamsManager";

    private static final String TABLE_DREAMS = "dreams";

    //Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DREAM = "dream";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates the tables
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_DREAMS_TABLE = "CREATE TABLE " + TABLE_DREAMS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +
                KEY_DREAM + " TEXT," + KEY_TIME + " TEXT," +
                KEY_DATE + " TEXT" + ")";
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

        db.insert(TABLE_DREAMS,null,values);
        db.close();
    }

    public Dream getDream(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DREAMS,
                new String[] {KEY_ID,KEY_TITLE,KEY_DREAM,KEY_TIME,KEY_DATE}, KEY_ID + "=?",
                new String[] {String.valueOf(id)} , null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        Dream dream = new Dream(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

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

        return db.update(TABLE_DREAMS, values, KEY_ID + " = ?",
                new String[]{ String.valueOf(dream.getId())});
    }

    public void deleteDream(Dream dream){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DREAMS, KEY_ID + " = ?",
                new String[] {String.valueOf(dream.getId())});
        db.close();
    }
}
