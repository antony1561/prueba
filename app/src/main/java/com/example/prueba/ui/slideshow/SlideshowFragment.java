package com.example.prueba.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba.R;
import com.example.prueba.entidades.producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SlideshowFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private SlideshowViewModel slideshowViewModel;

    EditText cp1;
    TextView t1,t2,t3;
    ImageView imageView;
    Button bc1;
    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        cp1 = (EditText)root.findViewById(R.id.cproducto);
        t1 = (TextView) root.findViewById(R.id.txtprecio);
        t2 = (TextView) root.findViewById(R.id.txtcantidad);
        t3 = (TextView) root.findViewById(R.id.txtdescripcion);
        bc1= (Button)root.findViewById(R.id.btnconsultar);
        imageView=root.findViewById(R.id.imagen);
        requestQueue= Volley.newRequestQueue(getContext());

        bc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargar();
            }
        });
        return root;
    }

    private void cargar() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando ...");
        progreso.show();

         String url ="http://192.168.1.19:8080/emdicel/consultaproductoimagen.php?producto="+cp1.getText().toString();

         jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, this, this);
         requestQueue.add(jsonObjectRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo consultar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(getContext(), "Mensaje"+response, Toast.LENGTH_SHORT).show();

        producto miproducto = new producto();
        JSONArray json= response.optJSONArray("productos");
        JSONObject jsonObject=null;

        try {
            jsonObject= json.getJSONObject(0);
            miproducto.setPrecio(jsonObject.optString("precio"));
            miproducto.setCantidad(jsonObject.optString("cantidad"));
            miproducto.setDescripcion(jsonObject.optString("descripcion"));
            miproducto.setDato(jsonObject.optString("imagen"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        t1.setText("Precio: "+miproducto.getPrecio());
        t2.setText("Cantidad: "+miproducto.getCantidad());
        t3.setText("Descripcion: "+miproducto.getDescripcion());
        if(miproducto.getImagen() !=null){
            imageView.setImageBitmap(miproducto.getImagen());
        }else{
            imageView.setImageResource(R.drawable.camaraimagen);
        }
    }
}
