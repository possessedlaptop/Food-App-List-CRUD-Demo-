package com.awakesloth.desarrollo_apps_final_lista_comidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register_Screen extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText securityQuestionEditText;
    private Button registerButton;
    private Button backButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // Get references to the UI elements
        usernameEditText = findViewById(R.id.etRegUsername);
        passwordEditText = findViewById(R.id.etRegPass);
        securityQuestionEditText = findViewById(R.id.etRegPregunta);
        registerButton = findViewById(R.id.btnRegRegistrar);
        backButton = findViewById(R.id.btnRegRegresar);


        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // The "users" node doesn't exist, so create it
                    usersRef.setValue(true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Node creation successful
                                        // Proceed with user registration logic
                                    } else {
                                        // Node creation failed
                                        Toast.makeText(Register_Screen.this, "Failed to create 'users' node", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // The "users" node already exists
                    // Proceed with user registration logic
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error during node check or creation
                Toast.makeText(Register_Screen.this, "Error occurred during 'users' node check", Toast.LENGTH_SHORT).show();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered username, password, and security question
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String securityQuestion = securityQuestionEditText.getText().toString();

                if (password.length() >= 6) {
                    // Password meets the minimum length requirement
                    // Proceed with registration logic
                firebaseAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(Register_Screen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    Usuario newUser = new Usuario(userId, username, password, securityQuestion);
                                    usersRef.child(userId).setValue(newUser);
                                    Toast.makeText(Register_Screen.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    // Proceed to the login screen or perform any desired action
                                } else {
                                    // Registration failed
                                    Log.d("Registration", "Error: " + task.getException());
                                    Toast.makeText(Register_Screen.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and return to the previous screen
            }
        });
    }
}
