package igorsteblii.com.novssql.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

import igorsteblii.com.novssql.R;
import igorsteblii.com.novssql.adapter.SongAdapter;
import igorsteblii.com.novssql.dto.Song;

/**
 * @author igorsteblii on 05.08.16.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected Button insertRandomButton;
    protected Button insertJsonButton;
    protected Button selectAllButton;
    protected Button selectComplexButton;
    protected TextView timeView;
    protected RecyclerView recyclerView;
    protected IListener listener;
    protected SongAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SongAdapter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (IListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        insertRandomButton = (Button) view.findViewById(R.id.button_random);
        insertJsonButton = (Button) view.findViewById(R.id.button_json);
        selectAllButton = (Button) view.findViewById(R.id.button_all);
        selectComplexButton = (Button) view.findViewById(R.id.button_complex);
        insertRandomButton.setOnClickListener(this);
        insertJsonButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        selectComplexButton.setOnClickListener(this);
        timeView = (TextView) view.findViewById(R.id.time);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_random:
                addRandom();
                break;
            case R.id.button_json:
                addFromJson();
                break;
            case R.id.button_all:
                selectAll();
                break;
            case R.id.button_complex:
                selectComplex();
                break;
        }
    }

    protected abstract void selectComplex();

    protected abstract void selectAll();

    protected abstract void addFromJson();

    protected abstract void addRandom();

    public interface IListener {

        JSONArray getJsonSonArray();

        List<Song> createRandomSong();

    }

}
