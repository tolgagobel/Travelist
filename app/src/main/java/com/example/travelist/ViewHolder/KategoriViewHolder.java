package com.example.travelist.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelist.Arayuz.ItemClickListener;
import com.example.travelist.R;

public class KategoriViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener


{

    public TextView txtKategoriAdi;
    public ImageView imageView;

    private ItemClickListener itemClickListener;


    public KategoriViewHolder(@NonNull View itemView) {
        super(itemView);
        txtKategoriAdi=itemView.findViewById(R.id.kategori_adi);
        imageView=itemView.findViewById(R.id.kategori_resmi);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);


    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Eylem Seçin");
        contextMenu.add(0,0,getAdapterPosition(),"Güncelle");
        contextMenu.add(0,1,getAdapterPosition(),"Sil");
    }
}
