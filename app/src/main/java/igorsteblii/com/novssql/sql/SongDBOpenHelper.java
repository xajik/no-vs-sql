package igorsteblii.com.novssql.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import igorsteblii.com.novssql.dto.Song;

/**
 * @author igorsteblii on 05.08.16.
 */
public class SongDBOpenHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Songs.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SongColumns.TABLE_NAME + " (" +
                    SongColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SongColumns.COLUMN_NAME_UID + TEXT_TYPE + COMMA_SEP +
                    SongColumns.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongColumns.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    SongColumns.COLUMN_NAME_DURATION + INTEGER_TYPE + COMMA_SEP +
                    SongColumns.COLUMN_NAME_YEAR + INTEGER_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SongColumns.TABLE_NAME;

    public SongDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void dropTable() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addAll(List<Song> songs) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Song s : songs) {
            values.put(SongColumns.COLUMN_NAME_UID, s.getUid());
            values.put(SongColumns.COLUMN_NAME_ARTIST, s.getArtist());
            values.put(SongColumns.COLUMN_NAME_TITLE, s.getTitle());
            values.put(SongColumns.COLUMN_NAME_DURATION, s.getDuration());
            values.put(SongColumns.COLUMN_NAME_YEAR, s.getYear());
            db.insert(
                    SongColumns.TABLE_NAME,
                    null,
                    values);
        }
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> list = new ArrayList<Song>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + SongColumns.TABLE_NAME, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            list.add(
                    new Song(
                            c.getString(c.getColumnIndex(SongColumns.COLUMN_NAME_UID)),
                            c.getString(c.getColumnIndex(SongColumns.COLUMN_NAME_ARTIST)),
                            c.getString(c.getColumnIndex(SongColumns.COLUMN_NAME_TITLE)),
                            c.getLong(c.getColumnIndex(SongColumns.COLUMN_NAME_DURATION)),
                            c.getInt(c.getColumnIndex(SongColumns.COLUMN_NAME_YEAR))
                    )
            );
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public static class SongColumns implements BaseColumns {

        private static final String TABLE_NAME = "song_table";
        private static final String COLUMN_NAME_UID = "uid";
        private static final String COLUMN_NAME_ARTIST = "artist";
        private static final String COLUMN_NAME_TITLE = "title";
        private static final String COLUMN_NAME_DURATION = "duration";
        private static final String COLUMN_NAME_YEAR = "year";

    }

}
