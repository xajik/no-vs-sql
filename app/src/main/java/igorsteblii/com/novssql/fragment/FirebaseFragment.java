package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import igorsteblii.com.novssql.dto.Song;
import igorsteblii.com.novssql.sql.Columns;

import static igorsteblii.com.novssql.Constants.DEFAULT_YEARS_OBJECTS;

/**
 * @author igorsteblii on 12.08.16.
 */
public class FirebaseFragment extends BaseFragment {

    public static final String DATABASE_NAME = "no-vs-sql";

    private DatabaseReference mFirebaseDatabaseReference;

    private List<Song> songList;
    private List<Long> dateList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songList = new ArrayList<>();
        dateList = new ArrayList<>();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_NAME);
        mFirebaseDatabaseReference.child(Columns.DATE_TABLE_NAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    dateList.clear();
                    GenericTypeIndicator<List<Long>> t = new GenericTypeIndicator<List<Long>>() {
                    };
                    List<Long> longs = snapshot.getValue(t);
                    dateList.addAll(longs);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
        mFirebaseDatabaseReference.child(Columns.DATE_TABLE_NAME).setValue(Arrays.asList(DEFAULT_YEARS_OBJECTS));
    }

    @Override
    protected void selectComplex() {
        songList.clear();
        final int[] i = new int[1];
        final long time = System.currentTimeMillis();
        DatabaseReference ref = mFirebaseDatabaseReference.child(Columns.SONG_TABLE_NAME).getRef();
        for (long year : dateList) {
            Query orderByChild = ref.orderByChild(Columns.COLUMN_NAME_YEAR).startAt(year).endAt(year);
            orderByChild.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        songList.add(postSnapshot.getValue(Song.class));
                    }
                    i[0]++;
                    if (!songList.isEmpty() && i[0] == dateList.size()) {
                        mAdapter.add(songList);
                        mAdapter.notifyDataSetChanged();
                        String s = "count : " + songList.size() + ", " + "from JSON to Realm :" +
                                (System.currentTimeMillis() - time) + "ms";
                        timeView.setText(s);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(getView(), databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void selectAll() {
        final long time = System.currentTimeMillis();
        mFirebaseDatabaseReference.child(Columns.SONG_TABLE_NAME).orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        addAllToSongList(dataSnapshot, (System.currentTimeMillis() - time));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(getView(), databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void addFromJson() {
        Gson gson = new GsonBuilder().create();
        final Song[] songs = gson.fromJson(listener.getJsonSonArray().toString(), Song[].class);
        final long time = System.currentTimeMillis();
        mFirebaseDatabaseReference.child(Columns.SONG_TABLE_NAME).setValue(Arrays.asList(songs), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String s = "count : " + songs.length + ", " + "insert:" + (System.currentTimeMillis() - time) + "ms";
                timeView.setText(s);
            }
        });
    }

    @Override
    protected void addRandom() {
        final List<Song> songs = listener.createRandomSong();
        final long time = System.currentTimeMillis();
        mFirebaseDatabaseReference.child(Columns.SONG_TABLE_NAME).setValue(songs, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String s = "count : " + songs.size() + ", " + "insert:" + (System.currentTimeMillis() - time) + "ms";
                timeView.setText(s);
            }
        });
    }

    private void addAllToSongList(DataSnapshot snapshot, long time) {
        songList.clear();
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            songList.add(postSnapshot.getValue(Song.class));
        }
        if (!songList.isEmpty()) {
            mAdapter.add(songList);
            mAdapter.notifyDataSetChanged();
        }
        String s = "count : " + songList.size() + ", " + ", select all: " + time + "ms";
        timeView.setText(s);
    }

}
