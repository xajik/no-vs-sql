package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import igorsteblii.com.novssql.adapter.SongAdapter;
import igorsteblii.com.novssql.dto.Song;
import igorsteblii.com.novssql.sql.SongDBOpenHelper;

/**
 * @author igorsteblii on 05.08.16.
 */
public class SqlLiteFragment extends BaseFragment {

    private Gson gson = new GsonBuilder().create();
    private SongAdapter mAdapter = new SongAdapter();
    private SongDBOpenHelper dbOpenHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        dbOpenHelper = new SongDBOpenHelper(getActivity());
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected void selectComplex() {

    }

    @Override
    protected void selectAll() {
        long time = System.currentTimeMillis();
        ArrayList<Song> allSongs = dbOpenHelper.getAllSongs();
        long time2 = System.currentTimeMillis() - time;
        String s = "count: " + allSongs.size() + " select All from DB: " + time2;
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
        String s = "count: " + song.length + ", from JSON to List: " + time1 + "ms, from List to DB: " + time2 + "ms";
        timeView.setText(s);
    }

    @Override
    protected void addRandom() {

    }

}
