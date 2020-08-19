package com.example.prueba.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba.R;
import com.example.prueba.adapters.productosadapter;
import com.example.prueba.entidades.producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private HomeViewModel homeViewModel;

    RecyclerView recyclerproductos;
    ArrayList<producto> listproducto;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    JsonObjectRequest jsonObjectRequest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listproducto = new ArrayList<>();

        recyclerproductos = (RecyclerView) root.findViewById(R.id.rlista);
        recyclerproductos.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerproductos.setHasFixedSize(true);

        requestQueue = Volley.newRequestQueue(getContext());

        cargarlistawev();
        return root;
    }

    private void cargarlistawev() {
         progressDialog = new ProgressDialog(getContext());
         progressDialog.setMessage("Cargando ...");
         progressDialog.show();
         String url = "http://192.168.1.19:8080/emdicel/consultalista.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(), "No se pudo conectar"+error.toString(), Toast.LENGTH_SHORT).show();
        System.out.println();
        Log.i("ERROR",error.toString());
        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        producto productos = null;

        JSONArray json = response.optJSONArray("productos");
        try {

              for(int i=0;i<json.length();i++){
                  productos=new producto();
              JSONObject jsonObject=null;
              jsonObject=json.getJSONObject(i);

               productos.setId(jsonObject.optInt("codigo"));
               productos.setProducto(jsonObject.optString("producto"));
               productos.setPrecio(jsonObject.optString("precio"));
               productos.setCantidad(jsonObject.optString("cantidad"));
               productos.setDescripcion(jsonObject.optString("descripcion"));
               productos.setDato(jsonObject.optString("imagen"));
               listproducto.add(productos);
                }
              progressDialog.hide();
              productosadapter adapter= new productosadapter(listproducto);
              recyclerproductos.setAdapter(adapter);
              } catch (JSONException e){
              e.printStackTrace();
            Toast.makeText(getContext(), "No se pudo conectar al servidor"+" "+response, Toast.LENGTH_SHORT).show();
            progressDialog.hide();
             }
        }
    }