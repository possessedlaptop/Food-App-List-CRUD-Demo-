package com.awakesloth.desarrollo_apps_final_lista_comidas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Order;
import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Plate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsListActivity extends AppCompatActivity {

    private ListView orderListView;
    private DatabaseReference ordersRef;
    private ArrayAdapter<String> orderAdapter;
    private List<String> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_list);

        // Initialize the ListView
        orderListView = findViewById(R.id.orderListView);

        // Create a reference to the "orders" node in Firebase Realtime Database
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Initialize the orderList
        orderList = new ArrayList<>();

        // Create the ArrayAdapter
        orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderList);

        // Set the adapter on the ListView
        orderListView.setAdapter(orderAdapter);

        // Retrieve the orders from Firebase Realtime Database
        retrieveOrders();
    }

    private void retrieveOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the orderList before populating it with new data
                orderList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the order ID from the dataSnapshot
                    String orderId = snapshot.getKey();

                    // Get the order details (selectedPlates, totalPrice, and userInfo)
                    Order order = snapshot.getValue(Order.class);
                    List<Plate> selectedPlates = order.getSelectedPlates();
                    double totalPrice = order.getTotalPrice();
                    String userInfo = order.getUserInfo();

                    // Create a formatted string for displaying the order details
                    StringBuilder builder = new StringBuilder();
                    builder.append("Order ID: ").append(orderId).append("\n");
                    builder.append("Username: ").append(userInfo).append("\n\n");
                    builder.append("Order Elements:\n");
                    for (Plate plate : selectedPlates) {
                        builder.append("Plate: ").append(plate.getName()).append("\n");
                        builder.append("Price: $").append(plate.getPrice()).append("\n\n");
                    }
                    builder.append("Total Price: $").append(totalPrice);

                    // Add the formatted order details to the orderList
                    orderList.add(builder.toString());
                }

                // Notify the adapter that the data has changed
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occurred while retrieving orders
                Toast.makeText(OrderDetailsListActivity.this, "Failed to retrieve orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


