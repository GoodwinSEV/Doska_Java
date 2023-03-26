package com.yakse.doska_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView nav_view;
    private FirebaseAuth mAuth;
    private TextView userEmail;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    protected void onStart() {
        super.onStart();
        getUserData();
        // updateUI(currentUser);
    }

    private void getUserData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null){
            userEmail.setText(currentUser.getEmail());
        }
        else
        {
            userEmail.setText(R.string.signInOrSignUp);
        }

    }

    private void init() {
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        userEmail = nav_view.getHeaderView(0).findViewById(R.id.tvEmail);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Doska");

        myRef.setValue("Hello, World!");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_my_ads:
                Toast.makeText(this, "Pressed my ads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_cars_ads:
                Toast.makeText(this, "Pressed cars ads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_pc_ads:
                Toast.makeText(this, "Pressed pc ads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_smartphone_ads:
                Toast.makeText(this, "Pressed smartphone ads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_dm_ads:
                Toast.makeText(this, "Pressed dm ads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_up:
                signUpDialog(R.string.sign_up, R.string.sign_up_button, 0);

                Toast.makeText(this, "Pressed Зарегистрироваться", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_in:
                signUpDialog(R.string.sign_in, R.string.sign_in_button, 1);
                Toast.makeText(this, "Pressed Войти", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_out:
                signOut();
                Toast.makeText(this, "Pressed Выйти", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private void signUpDialog(int title, int buttonTitle, int index) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sign_up_layout, null);
        dialogBuilder.setView(dialogView);
        TextView titleTextView = dialogView.findViewById(R.id.tvAlertTitle);
        titleTextView.setText(title);
        Button b = dialogView.findViewById(R.id.btnSignUp);
        EditText edEmail = dialogView.findViewById(R.id.edEmail);
        EditText edPassword = dialogView.findViewById(R.id.edPassword);

        b.setText(buttonTitle);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0) {
                    signUp(edEmail.getText().toString(), edPassword.getText().toString());
                }
                else
                {
                    signIn(edEmail.getText().toString(), edPassword.getText().toString());
                }
                dialog.dismiss();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();

    }

    private void signUp(String email, String password) {
        if (!email.equals("") && (!password.equals(""))) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                getUserData();
                                //FirebaseUser user = mAuth.getCurrentUser();
                                /*if (user != null)
                                    Toast.makeText(getApplicationContext(), "SignUp done ... user email: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                Log.d("MyLog", "createUserWithEmail:success" + user.getEmail());*/
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("MyLog", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authenticated failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Email  или Password пустой!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password) {
        if (!email.equals("") && (!password.equals(""))) {
            {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getUserData();
                                    // Sign in success, update UI with the signed-in user's information
                                   /* Log.d("MyLog", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(getApplicationContext(), "SignIn done ... user email: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    }*/
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("MyLog", "signInWithEmail:failure", task.getException());
                                }
                            }
                        });
            }

        }
        else {
            Toast.makeText(this, "Email  или Password пустой!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(){
        mAuth.signOut();
        getUserData();
    }


}

