package igorsteblii.com.novssql.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import igorsteblii.com.novssql.R;
import igorsteblii.com.novssql.dto.Song;

/**
 * @author igorsteblii on 05.08.16.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> mSongs;

    public SongAdapter() {
        mSongs = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBindViewHolder(mSongs.get(position));
    }

    public void add(List<Song> songs) {
        mSongs.addAll(songs);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mArtistView;
        private TextView mTitleView;
        private TextView mDurationView;
        private TextView mYearView;

        public ViewHolder(View view) {
            super(view);
            setupUi(view);
        }

        private void setupUi(View v) {
            mArtistView = (TextView) v.findViewById(R.id.song_artist);
            mTitleView = (TextView) v.findViewById(R.id.song_title);
            mDurationView = (TextView) v.findViewById(R.id.song_duration);
            mYearView = (TextView) v.findViewById(R.id.song_year);
        }

        public void onBindViewHolder(Song song) {
            mArtistView.setText(song.getArtist());
            mTitleView.setText(song.getTitle());
            mYearView.setText(String.valueOf(song.getYear()));
            mDurationView.setText(String.valueOf(song.getDuration()));
        }

    }

}
