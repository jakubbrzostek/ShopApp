package com.example.brzostek.project1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brzostek.common.model.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    private Context context;
    private List<Shop> shops = new ArrayList<>();

    public ShopsAdapter(Context context) {
        this.context = context;
    }

    public void updateShops(List<Shop> newShops) {
        shops.clear();
        shops.addAll(newShops);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_shop, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Shop shop = shops.get(i);
        viewHolder.txtShopName.setText(shop.getName());
        viewHolder.txtShopRadius.setText(shop.getRadius() + "m");
        viewHolder.txtShopLatLong.setText(String.format(Locale.getDefault(), "Lat: %.3f, Long: %.3f", shop.getLatitude(), shop.getLongitude()));
        viewHolder.txtShopDescription.setText(shop.getDescription());
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtShopName, txtShopRadius, txtShopLatLong, txtShopDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShopName = itemView.findViewById(R.id.txtShopName);
            txtShopRadius = itemView.findViewById(R.id.txtShopRadius);
            txtShopLatLong = itemView.findViewById(R.id.txtShopLatLong);
            txtShopDescription = itemView.findViewById(R.id.txtShopDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = MapsActivity.buildIntent(context, shops, getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}
