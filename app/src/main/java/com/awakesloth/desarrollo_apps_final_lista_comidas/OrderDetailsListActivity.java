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

        // Inicializar el ListView
        orderListView = findViewById(R.id.orderListView);

        // Crear una referencia al nodo "orders" en la base de datos de Firebase Realtime Database
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Inicializar la lista de pedidos (orderList)
        orderList = new ArrayList<>();

        // Crear el ArrayAdapter
        orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderList);

        // Establecer el adaptador en el ListView
        orderListView.setAdapter(orderAdapter);

        // Recuperar los pedidos de la base de datos de Firebase Realtime Database
        retrieveOrders();
    }

    private void retrieveOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpiar la lista de pedidos (orderList) antes de llenarla con nuevos datos
                orderList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener el ID del pedido desde el DataSnapshot
                    String orderId = snapshot.getKey();

                    // Obtener los detalles del pedido (selectedPlates, totalPrice y userInfo)
                    Order order = snapshot.getValue(Order.class);
                    List<Plate> selectedPlates = order.getSelectedPlates();
                    double totalPrice = order.getTotalPrice();
                    String userInfo = order.getUserInfo();

                    // Crear una cadena formateada para mostrar los detalles del pedido
                    StringBuilder builder = new StringBuilder();
                    builder.append("ID del Pedido: ").append(orderId).append("\n");
                    builder.append("Usuario: ").append(userInfo).append("\n\n");
                    builder.append("Elementos del Pedido:\n");
                    for (Plate plate : selectedPlates) {
                        builder.append("Plato: ").append(plate.getName()).append("\n");
                        builder.append("Precio: S/.").append(plate.getPrice()).append("\n\n");
                    }
                    builder.append("Precio Total: S/.").append(totalPrice).append("\n");

                    // Agregar los detalles del pedido formateados a la lista de pedidos (orderList)
                    orderList.add(builder.toString());
                }

                // Notificar al adaptador que los datos han cambiado
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar cualquier error que ocurra al recuperar los pedidos, mira los en el Logcat del AS
                Toast.makeText(OrderDetailsListActivity.this, "Error al recuperar los pedidos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
