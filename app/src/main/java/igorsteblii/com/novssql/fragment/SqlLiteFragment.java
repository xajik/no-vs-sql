package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import igorsteblii.com.novssql.dto.Song;
import igorsteblii.com.novssql.sql.DataBaseTableOpenHelper;

/**
 * @author igorsteblii on 05.08.16.
 */
public class SqlLiteFragment extends BaseFragment {

    private Gson gson;
    private DataBaseTableOpenHelper dbOpenHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpenHelper = new DataBaseTableOpenHelper(getActivity());
        dbOpenHelper.addDateIfDoNotExist();
        gson = new GsonBuilder().create();
    }

    /*Select all songs where 'year' is greater than '2001'*/
    @Override
    protected void selectComplex() {
        long time = System.currentTimeMillis();
        List<Song> list = dbOpenHelper.selectComplex();
        long time2 = System.currentTimeMillis() - time;
        String s = "count: " + list.size() + " select * by params: " + time2 + "ms";
        mAdapter.add(list);
        mAdapter.notifyDataSetChanged();
        timeView.setText(s);
    }

    @Override
    protected void selectAll() {
        long time = System.currentTimeMillis();
        ArrayList<Song> allSongs = dbOpenHelper.getAllSongs();
        long time2 = System.currentTimeMillis() - time;
        String s = "count: " + allSongs.size() + " select *: " + time2 + "ms";
        mAdapter.add(allSongs);
        mAdapter.notifyDataSetChanged();
        timeView.setText(s);
    }

    @Override
    protected void addFromJson() {
        long time = System.currentTimeMillis();
        JSONArray jsonSonArray = listener.getJsonSonArray();
        Song[] song = gson.fromJson(jsonSonArray.toString(), Song[].class);
        long time1 = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        dbOpenHelper.addAll(Arrays.asList(song));
        long time2 = System.currentTimeMillis() - time;
        String s = "count: " + song.length + ", parse JSON to List: " + time1 + "ms, insert in DB: " + time2 + "ms";
        timeView.setText(s);
    }

    @Override
    protected void addRandom() {
        List<Song> list = listener.createRandomSong();
        long time = System.currentTimeMillis();
        dbOpenHelper.addAll(list);
        long time2 = System.currentTimeMillis() - time;
        String s = "generated random, count: " + list.size() + " ,insert in DB: " + time2 + "ms";
        timeView.setText(s);
    }

}
