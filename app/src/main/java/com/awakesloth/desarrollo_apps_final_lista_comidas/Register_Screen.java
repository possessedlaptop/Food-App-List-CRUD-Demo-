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

        // Inicializamos las variables de los elementos en el layout
        usernameEditText = findViewById(R.id.etRegUsername);
        passwordEditText = findViewById(R.id.etRegPass);
        securityQuestionEditText = findViewById(R.id.etRegPregunta);
        registerButton = findViewById(R.id.btnRegRegistrar);
        backButton = findViewById(R.id.btnRegRegresar);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Listener para comprobar la existencia del nodo "users" en la base de datos de Firebase Realtime Database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // El nodo "users" no existe, por lo que se crea
                    usersRef.setValue(true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Creación del nodo exitosa
                                        // Proceder con la lógica de registro de usuario
                                    } else {
                                        // Fallo en la creación del nodo
                                        Toast.makeText(Register_Screen.this, "Error al crear el nodo 'users'", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // El nodo "users" ya existe
                    // Proceder con la lógica de registro de usuario
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar cualquier error durante la comprobación o creación del nodo
                Toast.makeText(Register_Screen.this, "Se produjo un error durante la comprobación del nodo 'users'", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de usuario, contraseña y pregunta de seguridad ingresados
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String securityQuestion = securityQuestionEditText.getText().toString();

                if (password.length() >= 6) {
                    // La contraseña cumple con el requisito de longitud mínima
                    // Proceder con la lógica de registro
                    firebaseAuth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(Register_Screen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registro exitoso
                                        String userId = firebaseAuth.getCurrentUser().getUid();
                                        Usuario newUser = new Usuario(userId, username, password, securityQuestion);
                                        usersRef.child(userId).setValue(newUser);
                                        Toast.makeText(Register_Screen.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        // Proceder a la pantalla de inicio de sesión o realizar cualquier acción deseada
                                    } else {
                                        // Registro fallido
                                        Log.d("Registro", "Error: " + task.getException());
                                        Toast.makeText(Register_Screen.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Listener para el botón de retroceso
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad y volver a la pantalla anterior
            }
        });
    }
}
