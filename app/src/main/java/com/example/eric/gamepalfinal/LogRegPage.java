package com.example.eric.gamepalfinal;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.text.TextUtils;
        import android.util.Log;
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
        import com.google.firebase.FirebaseError;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.util.Map;

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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.logregpage, container, false);
        appState = v.getContext();

        Button b = (Button) v.findViewById(R.id.btnLogin);
        b.setOnClickListener(mLoginListener);

        Button g = (Button) v.findViewById(R.id.btnPickGames);
        g.setOnClickListener(mGameListener);

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
        //firebaseAuth = FirebaseAuth.getInstance();

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
            //create user may not work yet.
            firebaseAuth.signInWithEmailAndPassword(logEmail, logPassword)
                    .addOnCompleteListener((Activity) appState, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            /*
                            Toast.makeText(appState, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            put in Welcome + name to be displayed on the main page when logged in.
                            */
                            if (!task.isSuccessful()) {
                                Toast.makeText(appState, "Login Failed" + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                Bundle bundle = new Bundle();
                                String usrID = firebaseAuth.getUid();

                                region = userRegion;
                                genre = gameGenre;
                                Toast.makeText(appState, usrID, Toast.LENGTH_LONG).show();

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
/*
    private String getRegion() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String east = "East";
        String west = "West";
        String mid = "Mid";
        String sa = "SAmerica";
        String fps = "FPS";
        String mmo = "MMO";
        String rpg = "RPG";
        String other = "Other";
        String reg,gen;
        int checker = 0;
        for (checker =0  ; checker <= 8 ;checker++){
            if(checker==1){
                reg = east;
                gen = fps;
            }
            if(checker==2){
                reg = east;
                gen = mmo;
            }
            if(checker==3){
                reg = east;
                gen =
            }
            if(checker==4){
                reg = sa;
            }
        }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(reg).child(gen);


        }
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        region = user.getUid();
        return region;

    }
    */
    private String getGenre() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        genre = ref.getKey();
        return genre;

    }



    private View.OnClickListener mTVListener = new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                RegPage regPage = new RegPage();

                fragmentTransaction.replace(R.id.content_frame, regPage);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        };
    private View.OnClickListener mGameListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Toast.makeText(appState, "Clicked",
                    Toast.LENGTH_SHORT).show();
            GridActivity gridActivity = new GridActivity();

            fragmentTransaction.replace(R.id.content_frame, gridActivity);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };
    };

