package com.example.eric.gamepalfinal;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettingsPage extends Fragment {

    View v;

    private EditText nameInput, bioInput;
    private String nameChanged, bio, usrID, gameGenre, userRegion;
    private Button submit, returnButton;
    Context appState;
    private ImageView gamePic1, gamePic2,gamePic3,gamePic4;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Uri uri;
    private int changedImageID, imageNum;
    private String picChanged, region, genre;
    private DrawerLayout mDrawerLayout;
    private static final String[] paths = {"FPS", "MMO", "RPG", "Other"};
    private static final String[] region2 = {"NA-East", "NA-West", "NA-Mid", "S.America"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.settingspage, container, false);
        appState = v.getContext();


        Toolbar settingsToolbar = v.findViewById(R.id.settingsToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(settingsToolbar);
        mDrawerLayout = v.findViewById(R.id.settings_drawer_layout);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.ic_mymenu5);

        nameInput = (EditText) v.findViewById(R.id.nameChange);
        bioInput   = (EditText) v.findViewById(R.id.bio);
        submit = (Button) v.findViewById(R.id.appChanges);
        returnButton = (Button) v.findViewById(R.id.returnButton);
        gamePic1 = (ImageView) v.findViewById(R.id.game1);
        gamePic2 = (ImageView) v.findViewById(R.id.game2);
        gamePic3 = (ImageView) v.findViewById(R.id.game3);
        gamePic4 = (ImageView) v.findViewById(R.id.game4);

        mAuth = FirebaseAuth.getInstance();
        usrID = mAuth.getCurrentUser().getUid();


        //bundle retrieve from grid maybe

        Bundle bundle = getArguments();
        if (bundle!=null) {
            region = bundle.getString("region");
            genre = bundle.getString("genre");
        }

        final Spinner spinner = (Spinner) v.findViewById(R.id.gameGenre);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(appState, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final Spinner spinnerRegion = (Spinner) v.findViewById(R.id.spinnerRegion);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(appState, android.R.layout.simple_spinner_item, region2);
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(region).child(genre).child(usrID);

        if (bundle != null) {
            int imageNumb = bundle.getInt("imageNumber");
            changedImageID = getArguments().getInt("selectedImage");
            gamePic1.setImageResource(changedImageID);
            gamePic2.setImageResource(changedImageID);
            gamePic3.setImageResource(changedImageID);
            gamePic4.setImageResource(changedImageID);
            if (imageNumb == 1) {
                gamePic1.setImageResource(changedImageID);

                Uri uri = Uri.parse("android.resource://com.example.eric.gamepalfinal/mipmap/" + changedImageID);
                try {
                    InputStream stream = appState.getContentResolver().openInputStream(uri);
                    Map usrInfo1 = new HashMap();
                    usrInfo1.put("ProfileImage1", uri.toString());
                    databaseReference.updateChildren(usrInfo1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                int imageId = getResources().getIdentifier("blank", "mipmap", appState.getPackageName()); //this will eventually read db pic
                gamePic1.setImageResource(imageId);  //have to save the image. worked with image  id instead of mipmap location
            }


            if (imageNumb == 2) {
                gamePic2.setImageResource(changedImageID);

                //Toast.makeText(appState, "second image yo", Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse("android.resource://com.example.eric.gamepalfinal/mipmap/" + changedImageID);
                try {
                    InputStream stream = appState.getContentResolver().openInputStream(uri);
                    //Toast.makeText(appState, "uri?" + uri.toString(), Toast.LENGTH_LONG).show();
                    Map usrInfo1 = new HashMap();
                    usrInfo1.put("ProfileImage2", uri.toString());
                    databaseReference.updateChildren(usrInfo1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                int imageId = getResources().getIdentifier("blank", "mipmap", appState.getPackageName()); //this will eventually read db pic
                gamePic2.setImageResource(imageId);  //have to save the image. worked with image  id instead of mipmap location
            }
            if (imageNumb == 3) {
                gamePic3.setImageResource(changedImageID);

                //Toast.makeText(appState, "second image yo", Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse("android.resource://com.example.eric.gamepalfinal/mipmap/" + changedImageID);
                try {
                    InputStream stream = appState.getContentResolver().openInputStream(uri);
                    //Toast.makeText(appState, "uri?" + uri.toString(), Toast.LENGTH_LONG).show();
                    Map usrInfo1 = new HashMap();
                    usrInfo1.put("ProfileImage3", uri.toString());
                    databaseReference.updateChildren(usrInfo1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                int imageId = getResources().getIdentifier("blank", "mipmap", appState.getPackageName()); //this will eventually read db pic
                gamePic3.setImageResource(imageId);  //have to save the image. worked with image  id instead of mipmap location
            }
            if (imageNumb == 4) {
                gamePic4.setImageResource(changedImageID);

                //Toast.makeText(appState, "second image yo", Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse("android.resource://com.example.eric.gamepalfinal/mipmap/" + changedImageID);
                try {
                    InputStream stream = appState.getContentResolver().openInputStream(uri);
                    //Toast.makeText(appState, "uri?" + uri.toString(), Toast.LENGTH_LONG).show();
                    Map usrInfo1 = new HashMap();
                    usrInfo1.put("ProfileImage4", uri.toString());
                    databaseReference.updateChildren(usrInfo1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                int imageId = getResources().getIdentifier("blank", "mipmap", appState.getPackageName()); //this will eventually read db pic
                gamePic4.setImageResource(imageId);  //have to save the image. worked with image  id instead of mipmap location
            }


        }


        gamePic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNum = 1;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle imgNum = new Bundle();
                imgNum.putInt("imageNum", imageNum);
                imgNum.putString("region", region);
                imgNum.putString("genre", genre);

                //GridActivity gridActivity = new GridActivity();
                ChangeGrid changeGrid = new ChangeGrid();
                changeGrid.setArguments(imgNum);
                /*
                gridActivity.setArguments(bundle2);
                bundle2.putString("region", userRegion);
                bundle2.putString("genre", gameGenre);
                */

                fragmentTransaction.replace(R.id.content_frame, changeGrid);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });


        getUserInfo();

        gamePic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNum = 2;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle imgNum = new Bundle();
                imgNum.putInt("imageNum", imageNum);
                imgNum.putString("region", region);
                imgNum.putString("genre", genre);
                ChangeGrid changeGrid = new ChangeGrid();
                changeGrid.setArguments(imgNum);
                fragmentTransaction.replace(R.id.content_frame, changeGrid);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        gamePic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNum = 3;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle imgNum = new Bundle();
                imgNum.putInt("imageNum", imageNum);
                imgNum.putString("region", region);
                imgNum.putString("genre", genre);
                ChangeGrid changeGrid = new ChangeGrid();
                changeGrid.setArguments(imgNum);
                fragmentTransaction.replace(R.id.content_frame, changeGrid);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        gamePic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNum = 4;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle imgNum = new Bundle();
                imgNum.putInt("imageNum", imageNum);
                imgNum.putString("region", region);
                imgNum.putString("genre", genre);
                ChangeGrid changeGrid = new ChangeGrid();
                changeGrid.setArguments(imgNum);
                fragmentTransaction.replace(R.id.content_frame, changeGrid);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle test = new Bundle();

                test.putString("region", region);
                test.putString("genre", genre);
                SettingsPage settingsPage = new SettingsPage();
                settingsPage.setArguments(test);
                fragmentTransaction.replace(R.id.content_frame, settingsPage);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle test = new Bundle();
                test.putString("region", userRegion);
                test.putString("genre", gameGenre);
               HomePage homePage = new HomePage();
               homePage.setArguments(test);

                fragmentTransaction.replace(R.id.content_frame, homePage);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });


        return v;
    }

    private void saveInfo() {
        nameChanged = nameInput.getText().toString();
        final Map usrInfo = new HashMap();
        usrInfo.put("Name", nameChanged);

        bio = bioInput.getText().toString();
        usrInfo.put("Bio", bio);

        databaseReference.updateChildren(usrInfo);


    }
    private void getUserInfo() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& dataSnapshot.getChildrenCount()>0){
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if (map.get("Name")!=null){
                    nameChanged = map.get("Name").toString();
                    nameInput.setText(nameChanged);

                }
                    if (map.get("Bio")!=null){
                        bio = map.get("Bio").toString();
                        bioInput.setText(bio);

                    }
                    if (map.get("ProfileImage1")!=null){
                        picChanged = map.get("ProfileImage1").toString();
                        Glide.with(appState).load(picChanged).into(gamePic1);

                    }
                    if (map.get("ProfileImage2")!=null){
                        picChanged = map.get("ProfileImage2").toString();
                        Glide.with(appState).load(picChanged).into(gamePic2);

                    }
                    if (map.get("ProfileImage3")!=null){
                        picChanged = map.get("ProfileImage3").toString();
                        Glide.with(appState).load(picChanged).into(gamePic3);

                    }
                    if (map.get("ProfileImage4")!=null){
                        picChanged = map.get("ProfileImage4").toString();
                        Glide.with(appState).load(picChanged).into(gamePic4);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}