package com.example.brzostek.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.brzostek.common.model.Product;

public class EditActivity extends Activity {

    private EditText name, price, quantity;
    private Button btnDelete, btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = findViewById(R.id.eTxtName);
        price = findViewById(R.id.eTxtPrice);
        quantity = findViewById(R.id.eTxtQuantity);
        btnDelete = findViewById(R.id.removeBtn);
        btnEdit = findViewById(R.id.editBtn);

        Product product = new Product(
                getIntent().getStringExtra(NAME_KEY),
                getIntent().getIntExtra(PRICE_KEY, 1),
                getIntent().getIntExtra(QUANTITY_KEY, 1),
                getIntent().getBooleanExtra(CHECKED_KEY, false)
        );

        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        quantity.setText(String.valueOf(product.getQuantity()));

        DeleteData();
        UpdateData();
    }

    public void DeleteData(){
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EditActivity.this, ProductListActivity.class);
                        startActivity(intent);
                        new DatabaseHelper().deleteProduct(name.getText().toString());
                    }
                }
        );
    }
    public void UpdateData(){
        btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EditActivity.this, ProductListActivity.class);
                        startActivity(intent);
                        new DatabaseHelper().addOrEditProduct(new Product(name.getText().toString(),
                                Integer.parseInt(price.getText().toString()),
                                Integer.parseInt(quantity.getText().toString()), false));
                    }

                }
        );
    }
    public static Intent buildIntent(Context context, Product product) {
        Intent result = new Intent(context, EditActivity.class);
        result.putExtra(NAME_KEY, product.getName());
        result.putExtra(PRICE_KEY, product.getPrice());
        result.putExtra(QUANTITY_KEY, product.getQuantity());
        result.putExtra(CHECKED_KEY, product.isChecked());
        return result;
    }

    private static final String NAME_KEY = "NAME_KEY";
    private static final String PRICE_KEY = "PRICE_KEY";
    private static final String QUANTITY_KEY = "QUANTITY_KEY";
    private static final String CHECKED_KEY = "CHECKED_KEY";

}
