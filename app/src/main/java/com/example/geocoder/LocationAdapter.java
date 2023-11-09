package com.example.geocoder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    Context context;
    ArrayList<Location> locations;
    public LocationAdapter(Context context, ArrayList<Location> locations) {
        this.context = context;
        this.locations = locations;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Each individual item_view in the RecyclerView is inflated to return in wht ViewHolder
        return new LocationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        // save the data that will be associated with each position for each item_view
        Location location = locations.get(position);
        holder.tvAddress.setText(location.getAddress());
        holder.tvLatitude.setText("LAT: " + location.getLatitude());
        holder.tvLongitude.setText("LON: " + location.getLongitude());
    }
    @Override
    public int getItemCount() {
        return locations.size();
    }
    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        TextView tvLatitude;
        TextView tvLongitude;
        RelativeLayout layout;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            // get the references for each view in the item_view
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvLatitude = itemView.findViewById(R.id.tvLatitude);
            tvLongitude = itemView.findViewById(R.id.tvLongitude);
            layout = itemView.findViewById(R.id.item_location);
            ImageView ivTrashCan = itemView.findViewById(R.id.ivTrashCan);


            ivTrashCan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete location from the main activity functionality is executed
                    // when the trash can icon is clicked
                    int pos = getAdapterPosition();
                    Location location = locations.get(pos);
                    DBHelper dbHelper = new DBHelper(context);
                    dbHelper.deleteLocation(location.getId());
                    locations.remove(pos);
                    notifyItemRemoved(pos);
                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // proceed to Edit Activity by providing the associated id of the item_view
                    // that was selected. The id associated with the location represented by
                    // that item_view will be passed with the Intent.
                    int pos = getAdapterPosition();
                    Location location = locations.get(pos);
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("ID", location.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
