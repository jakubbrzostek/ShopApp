package com.example.brzostek.project1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class OptionsActivity extends Activity {

    private Spinner spinner;
    private EditText editText;
    private SharedPreferences settings;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("Options", 0);
        setContentView(R.layout.activity_options);
        spinner = findViewById(R.id.colorSpinner);
        editText = findViewById(R.id.sizeEdit);
        saveBtn = findViewById(R.id.saveBtn);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TextColor.values());
        spinner.setAdapter(spinnerAdapter);

        int currentPosition = settings.getInt("spinnerSelection", 0);
        spinner.setSelection(currentPosition);

        float newSize = settings.getFloat("buttonSize", -1);
        if (newSize != -1) {
            onSizeChanged(newSize);
        }
        editText.setText(String.valueOf(saveBtn.getTextSize()));
    }

    public void SaveOnClick(View view) {
        SharedPreferences.Editor editor = settings.edit();
        TextColor selectedPosition = (TextColor) spinner.getSelectedItem();
        editor.putInt("spinnerSelection", selectedPosition.ordinal());

        float newSize = Float.parseFloat(editText.getText().toString());
        editor.putFloat("buttonSize", newSize);
        editor.apply();
        onSizeChanged(newSize);

    }

    private void onSizeChanged(float newSize) {
        saveBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
    }
}
