package com.example.brzostek.project1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.brzostek.common.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListActivity extends Activity {

    private RecyclerView recyclerView;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("Options", 0);
        setContentView(R.layout.activity_product_list);
        recyclerView = findViewById(R.id.product_list);

        LinearLayoutManager rlm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rlm);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        rlm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getItems();
    }

    private void getItems() {
        new DatabaseHelper().getAllProducts(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Product> products = new ArrayList<>();
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()){
                    products.add(productSnapshot.getValue(Product.class));
                }
                int color = TextColor.values()[settings.getInt("spinnerSelection", 0)].toAndroidColor();
                ProductsAdapter rva = new
                        ProductsAdapter(products, ProductListActivity.this, color);
                recyclerView.setAdapter(rva);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void NavigateToAddActivityOnClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
}
