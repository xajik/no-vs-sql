package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import igorsteblii.com.novssql.R;

/**
 * @author igorsteblii on 12.08.16.
 *         To be done : create Berkely DB implementation
 *         <p/>
 *         http://www.oracle.com/technetwork/database/database-technologies/berkeleydb/bdb-je-android-160932.pdf
 */
public class BerkeleyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.to_be_done_fragment, container, false);
    }

}
