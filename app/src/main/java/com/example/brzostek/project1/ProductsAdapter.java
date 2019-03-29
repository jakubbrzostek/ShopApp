package com.example.brzostek.project1;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brzostek.common.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Product> productsList;
    private Context context;
    private int color;

    public ProductsAdapter(List<Product> productsList, Context context, int color) {
        this.productsList = productsList;
        this.context = context;
        this.color = color;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Product pr = productsList.get(position);
        viewHolder.name.setTextColor(color);
        viewHolder.name.setText(pr.getName());
        viewHolder.price.setText("" + pr.getPrice() + "zł");
        viewHolder.quantity.setText("" + pr.getQuantity() + "szt.");
        if (pr.isChecked()) {
            viewHolder.bought.setChecked(true);
        } else {
            viewHolder.bought.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, price, quantity;
        public CheckBox bought;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            quantity = view.findViewById(R.id.quantity);
            bought = view.findViewById(R.id.bought);

            view.setOnClickListener(this);

            bought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(ProductsAdapter.this.context,
                                name.getText() + " kupiona!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProductsAdapter.this.context,
                                name.getText() + " do kupienia!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(ProductsAdapter.this.context,
                    "Zaznaczony produkt to " + name.getText(),
                    Toast.LENGTH_LONG).show();
            Intent intent = EditActivity.buildIntent(context, productsList.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
