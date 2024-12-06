package com.example.expertmaintenance.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.expertmaintenance.Models.Intervention;
import com.example.expertmaintenance.R;

import java.util.List;

public class InterventionAdapter extends RecyclerView.Adapter<InterventionAdapter.InterventionViewHolder> {

    private List<Intervention> interventionList;
    private Context context;

    public InterventionAdapter(Context context, List<Intervention> interventionList) {
        this.context = context;
        this.interventionList = interventionList;
    }

    @NonNull
    @Override
    public InterventionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.intervention_item, parent, false);
        return new InterventionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InterventionViewHolder holder, int position) {
        Intervention intervention = interventionList.get(position);

        // Bind data to the view holder elements
        holder.itemTitle.setText(intervention.getTitle());
        holder.itemName.setText(intervention.getClientName());
        holder.itemAddress.setText(intervention.getSiteAddress() + ", " + intervention.getSiteCity());
        holder.itemTime.setText(intervention.getStartTime() + " - " + intervention.getEndTime());
        holder.checkBox.setChecked(intervention.isCompleted());

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof Activity) {
                Intent intent = new Intent(context, InterventionDetails.class);
                intent.putExtra("interventionID", intervention.getId());
                intent.putExtra("interventionName", intervention.getTitle());
                intent.putExtra("companyName", intervention.getClientName());
                intent.putExtra("date", intervention.getStartTime());
                intent.putExtra("startTime", intervention.getStartTime());
                intent.putExtra("endTime", intervention.getEndTime());
                Log.d("InterventionAdapter", "Launching InterventionDetailsActivity with ID: " + intervention.getId());

                context.startActivity(intent);
            } else {
                Log.e("InterventionAdapter", "Invalid context: Unable to start InterventionDetailsActivity.");
                Toast.makeText(context, "Error: Unable to launch details.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                updateInterventionStatus(Integer.parseInt(intervention.getId()), 1, holder.checkBox);
            } else {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Décocher ?")
                        .setMessage("Êtes-vous sûr de vouloir décocher cette intervention ?")
                        .setPositiveButton("OUI", (dialog, which) -> {
                            updateInterventionStatus(Integer.parseInt(intervention.getId()), 0, holder.checkBox);
                        })
                        .setNegativeButton("ANNULER", (dialog, which) -> holder.checkBox.setChecked(true))
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void updateInterventionStatus(int interventionId, int terminee, CheckBox checkBox) {
        String url = "http://172.20.10.5/www/expert_maintenance_api/controllers/putTerminer.php?id=" + interventionId + "&terminee=" + terminee;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Statut mis à jour avec succès !", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(context, "Erreur lors de la mise à jour : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    checkBox.setChecked(terminee == 1);
                });

        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return interventionList.size();
    }

    public static class InterventionViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemName, itemAddress, itemTime;
        CheckBox checkBox;

        public InterventionViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemName = itemView.findViewById(R.id.itemName);
            itemAddress = itemView.findViewById(R.id.itemAddress);
            itemTime = itemView.findViewById(R.id.itemTime);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
