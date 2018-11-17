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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegPage extends Fragment {

    public String email, password, name;
    public EditText Email, Password, USRname;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    Context context;
    Bundle bundle = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.regpage, container, false);
        context = v.getContext();
        Button b = (Button) v.findViewById(R.id.btnReg);
        b.setOnClickListener(mRegListener);

        USRname = (EditText) v.findViewById(R.id.txtName);
        Email = (EditText) v.findViewById(R.id.txtEmail);
        Password = (EditText) v.findViewById(R.id.txtPwd);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                   // Toast.makeText(context, "null user", Toast.LENGTH_LONG).show();

                }
            }
        };

        return v;
    }

    private View.OnClickListener mRegListener = new View.OnClickListener() {
        public void onClick(View v) {
            email = Email.getText().toString();
            password = Password.getText().toString();

            name = USRname.getText().toString();

            mAuth.createUserWithEmailAndPassword(email,password);

            /*.addOnCompleteListener(RegPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    }
                }
            });
*/
            //worked tho???

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            NewUserPage newUserPage = new NewUserPage();
            newUserPage.setArguments(bundle);
            bundle.putString("txt", name);
            bundle.putString("auth", mAuth.getUid());
            fragmentTransaction.replace(R.id.content_frame, newUserPage);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
