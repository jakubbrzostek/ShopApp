package com.example.brzostek.project1;

import com.example.brzostek.common.model.Identifiable;
import com.example.brzostek.common.model.Product;
import com.example.brzostek.common.model.Shop;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {

    private DatabaseReference getProductsDatabase() {
        return FirebaseDatabase.getInstance().getReference("Products");
    }

    private DatabaseReference getShopsDatabase() {
        return FirebaseDatabase.getInstance().getReference("Shops");
    }

    public void addOrEditProduct(Product product) {
        addOrEditObject(getProductsDatabase(), product);
    }

    public void deleteProduct(String child) {
        deleteObject(getProductsDatabase(), child);
    }

    public void getAllProducts(ValueEventListener listener) {
        getAllObjects(getProductsDatabase(), listener);
    }

    public void addOrEditShop(Shop shop) {
        addOrEditObject(getShopsDatabase(), shop);
    }

    public void deleteShop(String shopId) {
        deleteObject(getShopsDatabase(), shopId);
    }

    public void getAllShops(ValueEventListener listener) {
        getAllObjects(getShopsDatabase(), listener);
    }

    private <O extends Identifiable> void addOrEditObject(DatabaseReference database, O object) {
        Map<String, Object> update = new HashMap<>();
        update.put(object.getId(), object);
        database.updateChildren(update);
    }

    private void deleteObject(DatabaseReference database, String id) {
        database.child(id).removeValue();
    }

    private void getAllObjects(DatabaseReference database, ValueEventListener listener) {
        database.orderByKey().addListenerForSingleValueEvent(listener);
    }
}
