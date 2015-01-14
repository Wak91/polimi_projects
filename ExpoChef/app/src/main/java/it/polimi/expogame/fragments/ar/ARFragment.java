package it.polimi.expogame.fragments.ar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.polimi.expogame.MainActivity;
import it.polimi.expogame.R;


/**
 * @degrigis
 *This fragment is merely a launcher for the Metaio view.
 * It implements View.OnClickListener because I want to implements the
 * callback method for the button here and not in the MainActivity.
 *
 */
public class ARFragment extends Fragment implements View.OnClickListener {

    public ARFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ar, container, false);

        //Retrieve the button and register this fragment as the listener for him
        Button upButton = (Button)view.findViewById(R.id.buttonStartMetaio);
        upButton.setOnClickListener(this);


        return view;  // Inflate the layout for this fragment

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * Listener for the button "Catch it!" that will
     * spawn the Metaio activity.
     */
    @Override
    public void onClick(View v) {

        Intent i = new Intent(this.getActivity(),ARActivity.class);
        startActivity(i);

    }

    @Override
    public void onResume(){
        super.onResume();

    }
}
