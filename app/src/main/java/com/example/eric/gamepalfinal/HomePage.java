package com.example.eric.gamepalfinal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment{

    private cards cards[];

    private arrayAdapter arrayAdapter;
    private int i;
    Context appState;
    private ArrayList<String> gamesLiked;
    Button logout, settings, matches;
    FirebaseAuth mAuth;
    String genre, region, currentUid;

    Toolbar homeToolbar;
    private DrawerLayout mDrawerLayout;

    List<cards> rowItems;
    private DatabaseReference userDB, chatDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_homepage, container, false);

        Toolbar homeToolbar = v.findViewById(R.id.homeToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(homeToolbar);
        mDrawerLayout = v.findViewById(R.id.home_drawer_layout);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.ic_mymenu5);

        appState = v.getContext();

        mAuth = FirebaseAuth.getInstance();
        userDB = FirebaseDatabase.getInstance().getReference().child("Users");
        chatDB = FirebaseDatabase.getInstance().getReference();
        if(mAuth!=null) {
            currentUid = mAuth.getCurrentUser().getUid();
        }else{
            currentUid = "X3kqabjcDhMJUbkhpF7aiuGlyji1";
            Toast.makeText(appState, "null", Toast.LENGTH_SHORT).show();
        }
        logout = (Button) v.findViewById(R.id.logout);
        logout.setOnClickListener(mLogoutListener);

        settings = (Button) v.findViewById(R.id.settings);
        settings.setOnClickListener(mSettingsListener);

        matches = (Button) v.findViewById(R.id.matches);
        matches.setOnClickListener(mMatchesListener);

        rowItems = new ArrayList<cards>();


        Bundle bundle = getArguments();
        if (bundle != null) {
            region = getArguments().getString("region");
            genre = getArguments().getString("genre");

            checkUsers(region, genre);
        }


        arrayAdapter = new arrayAdapter(appState, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) v.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards object = (cards) dataObject;
                String userID = object.getUsrID();
                userDB.child(region).child(genre).child(userID).child("No").child(currentUid).setValue(true);
                Toast.makeText(appState, "Dislike!",Toast.LENGTH_SHORT);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(appState, "Like!",Toast.LENGTH_SHORT);
                cards object = (cards) dataObject;
                String userID = object.getUsrID();
                userDB.child(region).child(genre).child(userID).child("Yes").child(currentUid).setValue(true);
                isAMatch(userID);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

            }
        });

        return v;
    }


    private void isAMatch(final String userID) {
        DatabaseReference checkMatchDB = userDB.child(region).child(genre).child(currentUid).child("Yes").child(userID);
        checkMatchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(appState, "New Match!",Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    userDB.child(region).child(genre).child(dataSnapshot.getKey()).child("Matches").child(currentUid).child("ChatID").setValue(key);
                    userDB.child(region).child(genre).child(currentUid).child("Matches").child(dataSnapshot.getKey()).child("ChatID").setValue(key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void checkUsers(String region, String genre){

       final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(region).child(genre);

       ref.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && !dataSnapshot.getKey().equals(user.getUid())){

                    String img1, img2, img3, img4, bio;
                    if (dataSnapshot.child("ProfileImage1").getValue()==null) {
                        img1 = "android.resource://com.example.eric.gamepalfinal/mipmap/blank";
                    }
                    else{
                        img1 = dataSnapshot.child("ProfileImage1").getValue().toString();
                    }
                    if (dataSnapshot.child("ProfileImage2").getValue()==null) {
                        img2 = "android.resource://com.example.eric.gamepalfinal/mipmap/blank";
                    }
                    else{
                        img2 = dataSnapshot.child("ProfileImage2").getValue().toString();
                    }
                    if (dataSnapshot.child("ProfileImage3").getValue()==null) {
                        img3 = "android.resource://com.example.eric.gamepalfinal/mipmap/blank";
                    }
                    else{
                        img3 = dataSnapshot.child("ProfileImage3").getValue().toString();
                    }
                    if (dataSnapshot.child("ProfileImage4").getValue()==null) {
                        img4 = "android.resource://com.example.eric.gamepalfinal/mipmap/blank";
                    }
                    else{
                        img4 = dataSnapshot.child("ProfileImage4").getValue().toString();
                    }
                    if (dataSnapshot.child("Bio").getValue()==null) {
                        bio = "Empty";
                    }
                    else{
                        bio = dataSnapshot.child("Bio").getValue().toString();
                    }

                    cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("Name").getValue().toString(), bio,
                            img1,img2,img3,img4);

                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       });
   }

    private View.OnClickListener mLogoutListener = new View.OnClickListener() {
        public void onClick(View v) {

            mAuth.signOut();
            currentUid = null;
            mAuth = null;
            userDB = null;
            for (Fragment fragment:getFragmentManager().getFragments()) {
               if (fragment!=null) {
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            LogRegPage logRegPage = new LogRegPage();

            fragmentTransaction.replace(R.id.content_frame, logRegPage);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener mSettingsListener = new View.OnClickListener() {
        public void onClick(View v) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SettingsPage settingsPage = new SettingsPage();
            Bundle bundle = new Bundle();
            bundle.putString("region", region);
            bundle.putString("genre", genre);
            settingsPage.setArguments(bundle);

            fragmentTransaction.replace(R.id.content_frame, settingsPage);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };
    private View.OnClickListener mMatchesListener = new View.OnClickListener() {
        public void onClick(View v) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            MatchesPage matchesPage = new MatchesPage();

            Bundle bundle = new Bundle();
            bundle.putString("region", region);
            bundle.putString("genre", genre);
            matchesPage.setArguments(bundle);

            fragmentTransaction.replace(R.id.content_frame, matchesPage);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };
}