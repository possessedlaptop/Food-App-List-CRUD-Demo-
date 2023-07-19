package com.awakesloth.desarrollo_apps_final_lista_comidas.entities;

public class Plate {
    private String name;
    private String ingredients;
    private int price;
    private boolean selected;

    public Plate() {
        // Default constructor required for Firebase
    }

    public Plate(String name, String ingredients, int price) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.selected = false; // Initialize selection state to false
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
