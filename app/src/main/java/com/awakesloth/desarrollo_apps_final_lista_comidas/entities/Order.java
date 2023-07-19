package com.awakesloth.desarrollo_apps_final_lista_comidas.entities;

import java.util.List;

public class Order {
    private List<Plate> selectedPlates;
    private double totalPrice;
    private String userInfo;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(List<Plate> selectedPlates, double totalPrice, String userInfo) {
        this.selectedPlates = selectedPlates;
        this.totalPrice = totalPrice;
        this.userInfo = userInfo;
    }

    public List<Plate> getSelectedPlates() {
        return selectedPlates;
    }

    public void setSelectedPlates(List<Plate> selectedPlates) {
        this.selectedPlates = selectedPlates;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
