package com.example.brzostek.project1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NavigateToListOnClick(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void NavigateToShopsOnClick(View view) {
        Intent intent = new Intent(this, ShopsActivity.class);
        startActivity(intent);
    }

    public void NavigateToOptionsOnClick(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}