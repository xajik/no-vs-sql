package igorsteblii.com.novssql.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import igorsteblii.com.novssql.Constants;
import igorsteblii.com.novssql.dto.Song;

/**
 * @author igorsteblii on 05.08.16.
 */
public class DataBaseTableOpenHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final String DATABASE_NAME = "sql_lite.db";

    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA = ",";
    private static final String SQL_SONG_CREATE_ENTRIES =
            "CREATE TABLE " + Columns.SONG_TABLE_NAME + " (" +
                    Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Columns.COLUMN_NAME_ID + INTEGER_TYPE + COMMA +
                    Columns.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA +
                    Columns.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA +
                    Columns.COLUMN_NAME_DURATION + INTEGER_TYPE + COMMA +
                    Columns.COLUMN_NAME_YEAR + INTEGER_TYPE +
                    " )";
    private static final String SQL_DATES_CREATE_ENTRIES =
            "CREATE TABLE " + Columns.DATE_TABLE_NAME + " (" +
                    Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Columns.COLUMN_NAME_YEAR + INTEGER_TYPE +
                    " )";
    private static final String SQL_DROP_SONG_TABLE =
            "DROP TABLE IF EXISTS " + Columns.SONG_TABLE_NAME;
    private static final String SQL_DROP_DATE_TABLE =
            "DROP TABLE IF EXISTS " + Columns.DATE_TABLE_NAME;

    public DataBaseTableOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_SONG_CREATE_ENTRIES);
        db.execSQL(SQL_DATES_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_SONG_TABLE);
        db.execSQL(SQL_DROP_DATE_TABLE);
        onCreate(db);
    }

    public void dropTable() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(SQL_DROP_SONG_TABLE);
        db.execSQL(SQL_DROP_DATE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addAll(List<Song> songs) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Song s : songs) {
            values.put(Columns.COLUMN_NAME_ID, s.getId());
            values.put(Columns.COLUMN_NAME_ARTIST, s.getArtist());
            values.put(Columns.COLUMN_NAME_TITLE, s.getTitle());
            values.put(Columns.COLUMN_NAME_DURATION, s.getDuration());
            values.put(Columns.COLUMN_NAME_YEAR, s.getYear());
            db.insert(
                    Columns.SONG_TABLE_NAME,
                    null,
                    values);
        }
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Columns.SONG_TABLE_NAME, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            list.add(
                    new Song(
                            c.getInt(c.getColumnIndex(Columns.COLUMN_NAME_ID)),
                            c.getString(c.getColumnIndex(Columns.COLUMN_NAME_ARTIST)),
                            c.getString(c.getColumnIndex(Columns.COLUMN_NAME_TITLE)),
                            c.getLong(c.getColumnIndex(Columns.COLUMN_NAME_DURATION)),
                            c.getInt(c.getColumnIndex(Columns.COLUMN_NAME_YEAR))
                    )
            );
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public List<Song> selectComplex() {
        ArrayList<Song> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        /*select all songs which date is exist in 'dates' without JOIN*/
//        Cursor c = getCursorForSelectFromBothTables(db);
        /*select all songs which date is exist in 'dates'  with JOIN*/
        Cursor c = getCursorForSelectFromBothTablesWithJoin(db);
        /*Select all songs with 'year' 2001*/
//        Cursor c = getCursorSelectByYear(db);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            list.add(
                    new Song(
                            c.getInt(c.getColumnIndex(Columns.COLUMN_NAME_ID)),
                            c.getString(c.getColumnIndex(Columns.COLUMN_NAME_ARTIST)),
                            c.getString(c.getColumnIndex(Columns.COLUMN_NAME_TITLE)),
                            c.getLong(c.getColumnIndex(Columns.COLUMN_NAME_DURATION)),
                            c.getInt(c.getColumnIndex(Columns.COLUMN_NAME_YEAR))
                    )
            );
            c.moveToNext();
        }
        c.close();
        return list;
    }

    private Cursor getCursorSelectByYear(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + Columns.SONG_TABLE_NAME + " WHERE year > ?", new String[]{"2001"});
    }

    private Cursor getCursorForSelectFromBothTables(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + Columns.SONG_TABLE_NAME + " st, " +
                        Columns.DATE_TABLE_NAME + " dt WHERE st." +
                        Columns.COLUMN_NAME_YEAR + "=dt." + Columns.COLUMN_NAME_YEAR
                , null);
    }

    private Cursor getCursorForSelectFromBothTablesWithJoin(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + Columns.SONG_TABLE_NAME + " st INNER JOIN " +
                        Columns.DATE_TABLE_NAME + " dt ON st." +
                        Columns.COLUMN_NAME_YEAR + "=dt." + Columns.COLUMN_NAME_YEAR
                , null);
    }

    public void addDateIfDoNotExist() {
        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, Columns.DATE_TABLE_NAME);
        if (count == 0) {
            ContentValues values = new ContentValues();
            int[] dates = Constants.DEFAULT_YEARS;
            for (int i : dates) {
                values.put(Columns.COLUMN_NAME_YEAR, i);
                db.insert(
                        Columns.DATE_TABLE_NAME,
                        null,
                        values);
            }
        }
    }

}
