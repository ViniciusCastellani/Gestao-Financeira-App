package com.example.gestaofinanceiraapp.Meta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaofinanceiraapp.R;

import java.util.ArrayList;
import java.util.List;

public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder> {

    private List<Meta> metaList;
    private Context context;

    public MetaAdapter(List<Meta> metaList, Context context) {
        this.metaList = metaList != null ? metaList : new ArrayList<>(); // Initialize with empty list if null
        this.context = context;
    }

    @NonNull
    @Override
    public MetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_meta, parent, false);
        return new MetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MetaViewHolder holder, int position) {
        if (metaList != null && position < metaList.size()) {
            Meta meta = metaList.get(position);
            holder.tvMetaTitle.setText(String.format("%s", meta.getTituloMeta()));
            holder.tvMetaDescription.setText(String.format("%s", meta.getDescricaoMeta()));
            holder.tvMetaValue.setText(String.format("R$ %.2f", meta.getValorMeta()));
            holder.tvMetaDueDate.setText(String.format("%s",
                    meta.getDataPrazo()));
        }
    }

    @Override
    public int getItemCount() {
        return metaList != null ? metaList.size() : 0;
    }

    public static class MetaViewHolder extends RecyclerView.ViewHolder {
        TextView tvMetaTitle;
        TextView tvMetaDescription;
        TextView tvMetaValue;
        TextView tvMetaDueDate;

        public MetaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMetaTitle = itemView.findViewById(R.id.tvMetaTitle);
            tvMetaDescription = itemView.findViewById(R.id.tvMetaDescription);
            tvMetaValue = itemView.findViewById(R.id.tvMetaValue);
            tvMetaDueDate = itemView.findViewById(R.id.tvMetaDueDate);
        }
    }
}
