package com.example.eric.gamepalfinal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LogRegPage extends Fragment {

    View v;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public EditText email, password;
    public String logEmail, logPassword;
    Context appState;
    String region, genre, gameGenre, userRegion;

    private static final String[] paths = {"FPS", "MMO", "RPG", "Other"};
    private static final String[] region2 = {"NA-East", "NA-West", "NA-Mid", "S.America"};

    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.logregpage, container, false);
        appState = v.getContext();

        Button b = (Button) v.findViewById(R.id.btnLogin);
        b.setOnClickListener(mLoginListener);

        TextView t = (TextView) v.findViewById(R.id.lnkRegister);
        t.setOnClickListener(mTVListener);

        email = (EditText) v.findViewById(R.id.txtEmail);
        password = (EditText) v.findViewById(R.id.txtPwd);

        final Spinner spinner = (Spinner) v.findViewById(R.id.gameGenre);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final Spinner spinnerRegion = (Spinner) v.findViewById(R.id.spinnerRegion);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, region2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRegion.setAdapter(adapter2);

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

    private View.OnClickListener mLoginListener = new View.OnClickListener() {
        public void onClick(View v) {

            logEmail = email.getText().toString();
            logPassword = password.getText().toString();

            if (TextUtils.isEmpty(logEmail)) {
                Toast.makeText(appState, "Email field empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(logPassword)) {
                Toast.makeText(appState, "Password field empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(logEmail, logPassword).addOnCompleteListener((Activity) appState, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(appState, "Invalid Username/Password" + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                Bundle bundle = new Bundle();
                                String usrID = firebaseAuth.getUid();

                                region = userRegion;
                                genre = gameGenre;

                                bundle.putString("region", region);
                                bundle.putString("genre", genre);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                HomePage homePage = new HomePage();
                                homePage.setArguments(bundle);
                                fragmentTransaction.replace(R.id.content_frame, homePage);
                                fragmentTransaction.addToBackStack(null);

                                fragmentTransaction.commit();

                            }
                        }
                    });
        }};

    private View.OnClickListener mTVListener = new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                firebaseAuth = FirebaseAuth.getInstance();
                String usrID = firebaseAuth.getUid();

                region = userRegion;
                genre = gameGenre;

                bundle.putString("region", region);
                bundle.putString("genre", genre);

                RegPage regPage = new RegPage();
                regPage.setArguments(bundle);
                fragmentTransaction.replace(R.id.content_frame, regPage);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        };
    };


