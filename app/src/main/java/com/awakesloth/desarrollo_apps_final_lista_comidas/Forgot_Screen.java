package com.awakesloth.desarrollo_apps_final_lista_comidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Forgot_Screen extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText securityQuestionEditText;
    private EditText resultEditText;
    private Button retrievePasswordButton;
    private Button backButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_screen);

        // Inicializamos las variables de los elementos en el layout
        usernameEditText = findViewById(R.id.etForUsername);
        securityQuestionEditText = findViewById(R.id.etForPregunta);
        resultEditText = findViewById(R.id.etForResultado);
        retrievePasswordButton = findViewById(R.id.btnForEnviar);
        backButton = findViewById(R.id.btnForRegresar);

        // Inicializar los componentes de Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Establecer un OnClickListener para el botón de recuperación de contraseña
        retrievePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de usuario y la pregunta de seguridad ingresados
                String username = usernameEditText.getText().toString();
                String securityQuestion = securityQuestionEditText.getText().toString();

                // Obtener la contraseña del usuario en función del nombre de usuario y la pregunta de seguridad proporcionados
                // Se busca por cada elemento hasta encontrar el del mismo username
                Query query = usersRef.orderByChild("username").equalTo(username);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Usuario user = snapshot.getValue(Usuario.class);
                            if (user != null && user.getSqAnswer().equals(securityQuestion)) {
                                // Recuperación de contraseña exitosa
                                resultEditText.setText(user.getPassword());
                                return;
                            }
                        }
                        Toast.makeText(Forgot_Screen.this, "Usuario o Respuesta inválidas", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Forgot_Screen.this, "Error, no se encuentra el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Establecer un OnClickListener para el botón de retroceso
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad y volver a la pantalla anterior
            }
        });
    }
}
