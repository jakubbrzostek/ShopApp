package com.example.brzostek.project1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.brzostek.common.model.Product;

public class AddActivity extends Activity {

    private EditText editName, editPrice, editQuantity;
    private Button btnAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editName = findViewById(R.id.eTxtName);
        editPrice = findViewById(R.id.eTxtPrice);
        editQuantity = findViewById(R.id.eTxtQuantity);
        btnAddData = findViewById(R.id.addToListBtn);
        AddData();
    }

    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatabaseHelper().addOrEditProduct(getCurrentProduct());
                        broadcastIntent();
                        finish();
                    }
                }
        );
    }

    public void broadcastIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.bbrzy.broadcastreceiver", "com.example.bbrzy.broadcastreceiver.MyBroadcastReceiver"));
        intent.setAction("com.example.MY_CUSTOM_INTENT");
        intent.putExtra(Product.KEY, getCurrentProduct());
        sendBroadcast(intent, "com.example.my_permissions.MY_PERMISSION");
    }

    private Product getCurrentProduct() {
        return new Product(
                editName.getText().toString(),
                Integer.parseInt(editPrice.getText().toString()),
                Integer.parseInt(editQuantity.getText().toString()),
                false
        );
    }
}
