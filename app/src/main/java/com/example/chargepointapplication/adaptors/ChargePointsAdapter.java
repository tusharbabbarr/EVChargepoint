package com.example.chargepointapplication.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepointapplication.DatabaseHelper;
import com.example.chargepointapplication.MapsActivity;
import com.example.chargepointapplication.Models.ChargePoint;
import com.example.chargepointapplication.R;

import java.util.List;

public class ChargePointsAdapter extends RecyclerView.Adapter<ChargePointsAdapter.ChargePointViewHolder> {

    private List<ChargePoint> chargePointsList;
    private Context context;

    public ChargePointsAdapter(List<ChargePoint> chargePointsList, Context context) {
        this.chargePointsList = chargePointsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChargePointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_charge_point, parent, false);
        return new ChargePointViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChargePointViewHolder holder, int position) {
        ChargePoint chargePoint = chargePointsList.get(position);
        holder.referenceId.setText(chargePoint.getReferenceID());
        holder.town.setText(chargePoint.getTown());
        holder.chargeDeviceStatusTextView.setText(chargePoint.getChargeDeviceStatus());
        holder.postcode.setText(chargePoint.getPostcode());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("chargePoint", chargePoint);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Charge Point")
                    .setMessage("Are you sure you want to delete this charge point?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new DatabaseHelper(context).deleteChargePoint(chargePoint.getReferenceID());
                        chargePointsList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Charge Point Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return chargePointsList.size();
    }

    public void updateList(List<ChargePoint> newList) {
        chargePointsList = newList;
        notifyDataSetChanged();
    }

    public static class ChargePointViewHolder extends RecyclerView.ViewHolder {

        TextView referenceId, town, chargeDeviceStatusTextView, postcode;

        public ChargePointViewHolder(@NonNull View itemView) {
            super(itemView);
            referenceId = itemView.findViewById(R.id.referenceIdTextView);
            town = itemView.findViewById(R.id.townTextView);
            chargeDeviceStatusTextView = itemView.findViewById(R.id.chargeDeviceStatusTextView);
            postcode = itemView.findViewById(R.id.postcodeTextView);
        }
    }
}
