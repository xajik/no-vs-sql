package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import org.json.JSONArray;

import java.util.List;

import igorsteblii.com.novssql.dto.Date;
import igorsteblii.com.novssql.dto.Song;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import static igorsteblii.com.novssql.Constants.DEFAULT_YEARS;
import static igorsteblii.com.novssql.sql.Columns.COLUMN_NAME_YEAR;

/**
 * @author igorsteblii on 05.08.16.
 */
public class RealmFragment extends BaseFragment {

    private Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        addDatesIfDoNotExist();
    }

    private void addDatesIfDoNotExist() {
        long count = mRealm.where(Date.class).count();
        if (count == 0) {
            Date d;
            mRealm.beginTransaction();
            for (int year : DEFAULT_YEARS) {
                d = new Date(year);
                mRealm.copyToRealm(d);
            }
            mRealm.commitTransaction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }

    /*Select all songs where 'year' is greater than '2001'*/
    @Override
    protected void selectComplex() {
        long time = System.currentTimeMillis();
        RealmResults<Song> all = getByYear();
//        RealmResults<Song> all = getByYearFromDateTable();
        long time2 = System.currentTimeMillis() - time;
        String s = "count: " + all.size() + " ,select * by params: " + time2 + "ms";
        mAdapter.add(all);
        mAdapter.notifyDataSetChanged();
        timeView.setText(s);
    }

    private RealmResults<Song> getByYearFromDateTable() {
        RealmResults<Date> dates = mRealm.where(Date.class).findAll();
        RealmQuery<Song> query = mRealm.where(Song.class);
        query.beginGroup();
        for (int i = 0; i < dates.size(); i++) {
            if (i != 0) {
                query.or();
            }
            query.equalTo(COLUMN_NAME_YEAR, dates.get(i).getYear());
        }
        query.endGroup();
        return query.findAll();
    }

    @NonNull
    private RealmResults<Song> getByYear() {
        return mRealm.where(Song.class).greaterThan("year", 2001).findAll();
    }

    @Override
    protected void selectAll() {
        long time = System.currentTimeMillis();
        RealmResults<Song> all = mRealm.where(Song.class).findAll();
        //mRealm.copyFromRealm(all); // if you have multithreading
        long time2 = System.currentTimeMillis() - time;
        String s = "count: " + all.size() + " ,select all: " + time2 + "ms";
        mAdapter.add(all);
        mAdapter.notifyDataSetChanged();
        timeView.setText(s);
    }

    @Override
    protected void addFromJson() {
        JSONArray array = listener.getJsonSonArray();
        long time = System.currentTimeMillis();
        mRealm.beginTransaction();
        try {
            mRealm.createAllFromJson(Song.class, array);
        } catch (RealmPrimaryKeyConstraintException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
            return;
        } finally {
            mRealm.commitTransaction();
        }
        time = System.currentTimeMillis() - time;
        String s = "count : " + array.length() + ", " + "from JSON to Realm :" + time + "ms";
        timeView.setText(s);
    }

    @Override
    protected void addRandom() {
        List<Song> song = listener.createRandomSong();
        long time = System.currentTimeMillis();
        mRealm.beginTransaction();
        try {
            mRealm.copyToRealm(song);
        } catch (RealmPrimaryKeyConstraintException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
            return;
        } finally {
            mRealm.commitTransaction();
        }
        time = System.currentTimeMillis() - time;
        String s = "count : " + song.size() + ", " + "insert:" + time + "ms";
        timeView.setText(s);
    }

}
