package com.example.prueba.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.R;
import com.example.prueba.entidades.producto;

import java.lang.reflect.Array;
import java.util.List;

public class productosadapter extends RecyclerView.Adapter<productosadapter.productoHolder>{

List<producto> productoList;
public productosadapter(List<producto> productoList)

    {
    this.productoList = productoList;
}


    @NonNull
    @Override
    public productoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.listaproducto,parent,false);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new productoHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull productoHolder holder, int position) {
        holder.txtlid.setText(productoList.get(position).getId().toString());
        holder.txtlproducto.setText(productoList.get(position).getProducto().toString());
        holder.txtlprecio.setText(productoList.get(position).getPrecio().toString());
        holder.txtlcantidad.setText(productoList.get(position).getCantidad().toString());
        holder.txtldescripcion.setText(productoList.get(position).getDescripcion().toString());

        if(productoList.get(position).getImagen()!=null){
            holder.txtlimagen.setImageBitmap(productoList.get(position).getImagen());
        }else{
            holder.txtlimagen.setImageResource(R.drawable.camaraimagen);
        }
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class productoHolder extends RecyclerView.ViewHolder {
    TextView txtlid,txtlproducto,txtlprecio,txtlcantidad,txtldescripcion;
    ImageView txtlimagen;
    public productoHolder(View itemView){
          super(itemView);
          txtlid=itemView.findViewById(R.id.txtlid);
          txtlproducto=itemView.findViewById(R.id.txtlproducto);
          txtlprecio=itemView.findViewById(R.id.txtlprecio);
          txtlcantidad=itemView.findViewById(R.id.txtlcantidad);
          txtldescripcion=itemView.findViewById(R.id.txtldescripcion);
          txtlimagen=(ImageView) itemView.findViewById(R.id.txtlimagen);

    }
    }
}
