package com.example.travelist.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelist.Arayuz.ItemClickListener;
import com.example.travelist.R;

public class TurlerViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView txtTurAdi;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public TurlerViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTurAdi = itemView.findViewById(R.id.tur_adi);
        imageView = itemView.findViewById(R.id.tur_resmi);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Eylem Seçin");
        contextMenu.add(0, 0, getAdapterPosition(), "Güncelle");
        contextMenu.add(0, 1, getAdapterPosition(), "Sil");
    }
}
