package com.example.eric.gamepalfinal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserPage extends Fragment{

    Context appState;
    EditText name;
    private static final String[] paths = {"FPS", "MMO", "RPG", "Other"};
    private static final String[] region = {"NA-East", "NA-West", "NA-Mid", "S.America"};
    Bundle bundle = new Bundle();
    Bundle bundle2 = new Bundle();
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String userRegion,gameGenre,usrName, mAuthID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.newuserpage, container, false);
        appState = v.getContext();
        name = (EditText) v.findViewById(R.id.txtName);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            usrName = getArguments().getString("txt");
            mAuthID = getArguments().getString("auth");
            name.setText(usrName);
        }

        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final Spinner spinnerRegion = (Spinner) v.findViewById(R.id.spinnerRegion);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, region);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRegion.setAdapter(adapter2);

        Button game = (Button) v.findViewById(R.id.btnPickGames);
        game.setOnClickListener(mGameListener);

        Button sub = (Button) v.findViewById(R.id.subInfo);
        sub.setOnClickListener(mSubListener);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

               // gameGenre = spinner.getSelectedItem().toString();
                String gameType = spinner.getSelectedItem().toString();
                //Should change data base to avoid all these if statements.
                if(gameType.equals("FPS")){
                    gameGenre = "FPS";
                }
                if(gameType.equals("MMO")){
                    gameGenre = "MMO";
                }
                if(gameType.equals("RPG")){
                    gameGenre = "RPG";
                }
                if(gameType.equals("Other")){
                    gameGenre = "Other";
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            spinner.setSelection(1);
            }

        });
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String regionType = spinnerRegion.getSelectedItem().toString();
                //Should change data base to avoid all these if statements.
                if(regionType.equals("NA-East")){
                    userRegion = "East";
                }
                if(regionType.equals("NA-West")){
                    userRegion = "West";
                }
                if(regionType.equals("NA-Mid")){
                    userRegion = "Mid";
                }
                if(regionType.equals("S.America")){
                    userRegion = "SAmerica";
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                spinnerRegion.setSelection(1);
            }

        });

        return v;
    }

    private View.OnClickListener mGameListener = new View.OnClickListener() {
        public void onClick(View v) {
            String usrAuth = mAuth.getCurrentUser().getUid();

           // Toast.makeText(appState, usrAuth, Toast.LENGTH_LONG).show();

            DatabaseReference curUsrDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userRegion).child(gameGenre).child(usrAuth).child("Name");
            curUsrDB.setValue(usrName);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SettingsPage settingsPage = new SettingsPage();
            bundle2.putString("region", userRegion);
            bundle2.putString("genre", gameGenre);
            settingsPage.setArguments(bundle2);


            fragmentTransaction.replace(R.id.content_frame, settingsPage);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };
    private View.OnClickListener mSubListener = new View.OnClickListener() {
        public void onClick(View v) {


            //mAuthID = FirebaseAuth.getInstance().toString();
            String usrAuth = mAuth.getCurrentUser().getUid();

            Toast.makeText(appState, usrAuth, Toast.LENGTH_LONG).show();

            DatabaseReference curUsrDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userRegion).child(gameGenre).child(usrAuth).child("Name");
            curUsrDB.setValue(usrName);

            //here not working second time after logout.


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            HomePage homePage = new HomePage();
            /*
            gridActivity.setArguments(bundle);
            bundle.putString("txt", name);
            */
            homePage.setArguments(bundle2);
            bundle2.putString("region", userRegion);
            bundle2.putString("genre", gameGenre);

            fragmentTransaction.replace(R.id.content_frame, homePage);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };
}
