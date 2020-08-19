package com.example.prueba.ui.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba.R;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
   // private static final int COD_SELECCIONA =20 ;

    private GalleryViewModel galleryViewModel;
    EditText r1,r2,r3,r4,r5,r6;
    Spinner sp;
    Button br1,br2;
    ProgressDialog progreso;
    ImageView photor;
    RequestQueue request;
    JsonObjectRequest jsonObject;
    StringRequest stringRequest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ///////////////////////////////////////////////////////////////////////////////////////
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        String [] categoriasList =  new String[] {
                "Celulares", "Tablet", "Accesorios"
        };
        sp = root.findViewById(R.id.categorias);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, categoriasList);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
        ////////////// para que me salga un mensaje que e sellecionado del combo box//////////////
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Seleccionado:" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////
        r1 = (EditText)root.findViewById(R.id.producto);
        r2 = (EditText)root.findViewById(R.id.precio);
        r3 = (EditText)root.findViewById(R.id.cantidad);
        r4 = (EditText)root.findViewById(R.id.descripcion);
        r5 = (EditText)root.findViewById(R.id.codigo);
        r6 = (EditText)root.findViewById(R.id.descuento);
        br1= (Button)root.findViewById(R.id.btnregistrar);
        br2=(Button)root.findViewById(R.id.btnbuscarimagen);
        photor=(ImageView) root.findViewById(R.id.imagengaleria);

        request = Volley.newRequestQueue(getContext());

        br1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarweb();
            }
        });

        br2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostraropciones();
            }
        });
        return root;
    }

    private void mostraropciones() {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // switch(requestCode){
         //   case COD_SELECCIONA:
           //     Uri miPath= data .getData();
             //   photor.setImageURI(miPath);
               // break;
          //  case COD_FOTO:
            //    MediaScannerConnection.scanFile(getContext(),new String[]{path},null,(path,uri));
       // }
           if(resultCode == RESULT_OK){
               Uri path=data.getData();
               photor.setImageURI(path);

              // bitmap = BitmapFactory
           }
    }

    private void cargarweb() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando ...");
        progreso.show();
       // String url ="http://192.168.1.19:8080/emdicel/dbconexionconimagen.php?";

       // stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
         //   @Override
           // public void onResponse(String response) {

         //   }
      //  }, new Response.ErrorListener(){
       //     @Override
       // public void onErrorResponse(VolleyError error){

        //    }
       // }
       // ){
        //    @Override
         //   protected Map<String, String> getParams() throws AuthFailureError {
          //      String producto = r1.getText().toString();
           //     String precio = r2.getText().toString();
           //     String cantidad = r3.getText().toString();
            //    String descripcion = r4.getText().toString();
             //   String codigo = r5.getText().toString();
             //   String descuento = r6.getText().toString();
             //   //String imagen = photor.
            //    return super.getParams();
          //  }
      //  };
        String url= "http://192.168.1.19:8080/emdicel/dbconexion.php?" + "codigo="+r5.getText().toString()+
                                                                       "&producto="+r1.getText().toString()+
                                                                        "&precio="+r2.getText().toString()+
                                                                     "&cantidad="+r3.getText().toString()+
                                                                      "&descuento="+r6.getText().toString()+
                                                                     "&categoria="+ sp.getTextAlignment()+
                                                                   "&descripcion="+r4.getText().toString();

        url = url.replace(" ", "%20");
        jsonObject = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObject);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        progreso.hide();
        Toast.makeText(getContext(), "No se pudo registrar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        progreso.hide();
        r1.setText("");
        r2.setText("");
        r3.setText("");
        r4.setText("");
        r5.setText("");
        r6.setText("");

    }
}
