package com.example.eric.gamepalfinal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegPage extends Fragment {

    public String email, password, name, region, genre;
    public EditText Email, Password, USRname;
    private FirebaseAuth mAuth;
    Context context;
    Bundle bundle = new Bundle();
    private static final String[] paths = {"FPS", "MMO", "RPG", "Other"};
    private static final String[] regionList = {"NA-East", "NA-West", "NA-Mid", "S.America"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.regpage, container, false);
        context = v.getContext();
        Button b = (Button) v.findViewById(R.id.subInfo);
        b.setOnClickListener(mRegListener);

        USRname = (EditText) v.findViewById(R.id.txtName);
        Email = (EditText) v.findViewById(R.id.txtEmail);
        Password = (EditText) v.findViewById(R.id.txtPwd);

        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final Spinner spinnerRegion = (Spinner) v.findViewById(R.id.spinnerRegion);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, regionList);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRegion.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String gameType = spinner.getSelectedItem().toString();

                if (gameType.equals("FPS")) {
                    genre = "FPS";
                }
                if (gameType.equals("MMO")) {
                    genre = "MMO";
                }
                if (gameType.equals("RPG")) {
                    genre = "RPG";
                }
                if (gameType.equals("Other")) {
                    genre = "Other";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(1);
            }

        });
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String regionType = spinnerRegion.getSelectedItem().toString();
                //Should change data base to avoid all these if statements.
                if (regionType.equals("NA-East")) {
                    region = "East";
                }
                if (regionType.equals("NA-West")) {
                    region = "West";
                }
                if (regionType.equals("NA-Mid")) {
                    region = "Mid";
                }
                if (regionType.equals("S.America")) {
                    region = "SAmerica";
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                spinnerRegion.setSelection(1);
            }

        });

        return v;
    }

    private View.OnClickListener mRegListener = new View.OnClickListener() {
        public void onClick(View v) {
            email = Email.getText().toString();
            password = Password.getText().toString();

            name = USRname.getText().toString();

            mAuth = FirebaseAuth.getInstance();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "This email is already registered, please sign in",
                                Toast.LENGTH_SHORT).show();
                        Password.setText(null);
                    } else {


                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        SettingsPage settingsPage = new SettingsPage();
                        settingsPage.setArguments(bundle);
                        bundle.putString("region", region);
                        bundle.putString("genre", genre);
                        bundle.putString("txt", name);
                        bundle.putString("auth", mAuth.getUid());

                        fragmentTransaction.replace(R.id.content_frame, settingsPage);
                        fragmentTransaction.addToBackStack(null);

                        fragmentTransaction.commit();
                    }
                }

            });
        }
    };
}