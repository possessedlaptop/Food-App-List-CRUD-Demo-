package com.awakesloth.desarrollo_apps_final_lista_comidas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.awakesloth.desarrollo_apps_final_lista_comidas.adapters.PlateAdapter;
import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Order;
import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Plate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class Lista_Comidas_Screen extends AppCompatActivity {

    private ListView listView;
    private Button pedirButton;
    private Button borrarSeleccionButton;
    private Button viewOrdersButton;
    private List<Plate> plateList;
    private PlateAdapter plateAdapter;
    private DatabaseReference ordersRef;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comidas_screen);

        // Inicializamos las variables de los elementos en el layout
        listView = findViewById(R.id.lsvComidas);
        pedirButton = findViewById(R.id.btnFoodRequest);
        borrarSeleccionButton = findViewById(R.id.btnFoodBorrarSeleccion);
        viewOrdersButton = findViewById(R.id.viewOrdersButton);

        // Obtén el nombre de usuario desde el intent, este lo pasaremos al ingresar una orden
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Inicializar la lista de platos y el adaptador
        plateList = generatePlateList();
        plateAdapter = new PlateAdapter(this, plateList);

        // Establecer el adaptador en el ListView
        listView.setAdapter(plateAdapter);

        // Establecer el Listener de clics en los elementos del ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Plate selectedPlate = plateList.get(position);
            Toast.makeText(Lista_Comidas_Screen.this, "Plato seleccionado: " + selectedPlate.getName(), Toast.LENGTH_SHORT).show();
        });

        // Crear una referencia al nodo "orders" en la base de datos de Firebase Realtime Database, este puede debe ser creado manualmente si no funciona
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Establecer el Listener de clics en el botón "Pedir"
        pedirButton.setOnClickListener(v -> {
            // Obtener los platos seleccionados
            List<Plate> selectedPlates = plateAdapter.getSelectedPlates();
            // Calcular el precio total
            double totalPrice = calculateTotalPrice(selectedPlates);
            // Generar los detalles del pedido
            String orderDetails = generateOrderDetails(selectedPlates, totalPrice);

            // Actualiza el constructor de Order para incluir el nombre de usuario
            Order order = new Order(selectedPlates, totalPrice, username);

            // Generar un ID único para el pedido - Obligatorio
            String orderId = ordersRef.push().getKey();
            // Guardar el pedido en la base de datos de Firebase Realtime Database
            ordersRef.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Lista_Comidas_Screen.this, "¡Pedido realizado con éxito!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Lista_Comidas_Screen.this, "Error al realizar el pedido. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
                    });
        });

        // Establecer el Listener de clics en el botón "Borrar selección"
        borrarSeleccionButton.setOnClickListener(v -> {
            // Limpiar la selección en el adaptador
            plateAdapter.clearSelection();
        });

        // Establecer el Listener de clics en el botón "Ver pedidos"
        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intento para iniciar la actividad OrderDetailsListActivity
                Intent intent = new Intent(Lista_Comidas_Screen.this, OrderDetailsListActivity.class);
                startActivity(intent);
            }
        });

    }

    // Generar una lista de platos
    private List<Plate> generatePlateList() {
        List<Plate> plates = new ArrayList<>();

        // Obtén los nombres, precios e ingredientes de los platos desde los recursos
        String[] foodNames = getResources().getStringArray(R.array.food_names);
        int[] foodPrices = getResources().getIntArray(R.array.food_prices);
        String[] foodIngredients = getResources().getStringArray(R.array.food_ingredients);

        // Asegúrate de que todos los arrays tengan la misma longitud
        int length = Math.min(foodNames.length, Math.min(foodPrices.length, foodIngredients.length));

        // Loopea sobre los arrays y crea objetos Plate
        for (int i = 0; i < length; i++) {
            String name = foodNames[i];
            int price = foodPrices[i];
            String ingredients = foodIngredients[i];
            plates.add(new Plate(name, ingredients, price));
        }

        return plates;
    }

    // Calcular el precio total de los platos seleccionados
    private double calculateTotalPrice(List<Plate> selectedPlates) {
        double totalPrice = 0;
        for (Plate plate : selectedPlates) {
            totalPrice += plate.getPrice();
        }
        return totalPrice;
    }

    // Generar los detalles del pedido
    private String generateOrderDetails(List<Plate> selectedPlates, double totalPrice) {
        StringBuilder builder = new StringBuilder();
        builder.append("Detalles del Pedido:\n\n");
        for (Plate plate : selectedPlates) {
            builder.append("Plato: ").append(plate.getName()).append("\n");
            builder.append("Precio: S/.").append(plate.getPrice()).append("\n\n");
        }
        builder.append("Precio Total: S/.").append(totalPrice);
        return builder.toString();
    }
}
