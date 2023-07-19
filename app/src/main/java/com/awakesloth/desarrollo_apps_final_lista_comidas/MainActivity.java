package com.awakesloth.desarrollo_apps_final_lista_comidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private Button forgotPasswordButton;

    private FirebaseAuth firebaseAuth;
    String globalUser; // esto sera mandado desde el login hacia la generacion de ordenes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos las variables de los elementos en el layout
        usernameEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);
        forgotPasswordButton = findViewById(R.id.btnForgot);

        // Inicializa los componentes de Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Establece un OnClickListener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el nombre de usuario y la contraseña ingresados
                String username = usernameEditText.getText().toString();
                globalUser = username;
                String password = passwordEditText.getText().toString();

                // Realiza la operación de inicio de sesión en Firebase
                firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Inicio de sesión exitoso
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        // Obtiene el nombre de usuario de la variable global
                                        String username = globalUser;
                                        // Crea un intent para iniciar la actividad Lista_Comidas_Screen
                                        Intent intent = new Intent(MainActivity.this, Lista_Comidas_Screen.class);
                                        // Pasa el nombre de usuario a la actividad Lista_Comidas_Screen
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // Inicio de sesión fallido
                                    Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Establece un OnClickListener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un intento para iniciar la actividad Register_Screen
                Intent intent = new Intent(MainActivity.this, Register_Screen.class);
                startActivity(intent);
            }
        });

        // Establece un OnClickListener para el botón de contraseña olvidada
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un intento para iniciar la actividad Forgot_Screen
                Intent intent = new Intent(MainActivity.this, Forgot_Screen.class);
                startActivity(intent);
            }
        });
    }
}
