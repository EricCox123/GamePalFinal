package com.example.eric.gamepalfinal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class GridActivity extends Fragment {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View v = inflater.inflate(R.layout.grid_layout, container, false);
        context = v.getContext();

        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(context));

        Button home = (Button) v.findViewById(R.id.goHome);
        home.setOnClickListener(mHomeListener);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        }
    private View.OnClickListener mHomeListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Toast.makeText(context, "Clicked",
                    Toast.LENGTH_SHORT).show();
            HomePage homePage = new HomePage();

            fragmentTransaction.replace(R.id.content_frame, homePage);

            Bundle bundle2 = getArguments();
            homePage.setArguments(bundle2);

            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };
}
