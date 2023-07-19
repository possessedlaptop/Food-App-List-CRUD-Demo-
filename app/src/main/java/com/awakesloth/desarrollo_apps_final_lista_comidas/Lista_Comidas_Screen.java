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
import com.google.firebase.auth.FirebaseAuth;
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

        listView = findViewById(R.id.lsvComidas);
        pedirButton = findViewById(R.id.btnFoodRequest);
        borrarSeleccionButton = findViewById(R.id.btnFoodBorrarSeleccion);
        viewOrdersButton = findViewById(R.id.viewOrdersButton);

        // Retrieve the username from the intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Initialize the plate list and adapter
        plateList = generatePlateList();
        plateAdapter = new PlateAdapter(this, plateList);

        listView.setAdapter(plateAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Plate selectedPlate = plateList.get(position);
            Toast.makeText(Lista_Comidas_Screen.this, "Selected plate: " + selectedPlate.getName(), Toast.LENGTH_SHORT).show();
        });

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        pedirButton.setOnClickListener(v -> {
            List<Plate> selectedPlates = plateAdapter.getSelectedPlates();
            double totalPrice = calculateTotalPrice(selectedPlates);
            String orderDetails = generateOrderDetails(selectedPlates, totalPrice);

            // Update the Order constructor to include the username
            Order order = new Order(selectedPlates, totalPrice, username);

            String orderId = ordersRef.push().getKey();
            ordersRef.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Lista_Comidas_Screen.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Lista_Comidas_Screen.this, "Failed to place the order. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        });

        borrarSeleccionButton.setOnClickListener(v -> {
            plateAdapter.clearSelection();
        });

        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lista_Comidas_Screen.this, OrderDetailsListActivity.class);
                startActivity(intent);
            }
        });

    }

    private List<Plate> generatePlateList() {
        List<Plate> plates = new ArrayList<>();

        String[] foodNames = getResources().getStringArray(R.array.food_names);
        int[] foodPrices = getResources().getIntArray(R.array.food_prices);
        String[] foodIngredients = getResources().getStringArray(R.array.food_ingredients);

        int length = Math.min(foodNames.length, Math.min(foodPrices.length, foodIngredients.length));

        for (int i = 0; i < length; i++) {
            String name = foodNames[i];
            int price = foodPrices[i];
            String ingredients = foodIngredients[i];
            plates.add(new Plate(name, ingredients, price));
        }

        return plates;
    }

    private double calculateTotalPrice(List<Plate> selectedPlates) {
        double totalPrice = 0;
        for (Plate plate : selectedPlates) {
            totalPrice += plate.getPrice();
        }
        return totalPrice;
    }

    private String generateOrderDetails(List<Plate> selectedPlates, double totalPrice) {
        StringBuilder builder = new StringBuilder();
        builder.append("Order Details:\n\n");
        for (Plate plate : selectedPlates) {
            builder.append("Plate: ").append(plate.getName()).append("\n");
            builder.append("Price: $").append(plate.getPrice()).append("\n\n");
        }
        builder.append("Total Price: $").append(totalPrice);
        return builder.toString();
    }
}
