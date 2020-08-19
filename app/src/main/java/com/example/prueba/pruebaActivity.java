package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class pruebaActivity extends AppCompatActivity {
    EditText a1,a2,a3;
    Button br1;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        a1=findViewById(R.id.nombre);
        a2=findViewById(R.id.correo);
        a3=findViewById(R.id.contraseña);
        br1=findViewById(R.id.btnregistraru);
        br1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a3.length()>=8){
                    registrar("http://192.168.1.19:8080/emdicel/insertar_usuario.php");
                    Intent intent = new Intent(pruebaActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(pruebaActivity.this, "La contraseña debe tener minimo 8 caracteres", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void registrar(String URL) {
        String nombre = a1.getText().toString().trim();
        String correo = a2.getText().toString().trim();
        String contraseña = a3.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        if (nombre.isEmpty()) {
            a1.setError("complete los campos");
        } else if (correo.isEmpty()) {
            a2.setError("complete los campos");
        } else if (contraseña.isEmpty()) {
            a3.setError("complete los campos");
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("Datos insertados")){
                        Toast.makeText(pruebaActivity.this, "Datos ingresados", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else{
                        Toast.makeText(pruebaActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(pruebaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("nombre", a1.getText().toString());
                    parametros.put("correo", a2.getText().toString());
                    parametros.put("contraseña", a3.getText().toString());
                    return parametros;
                }
            };
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }
}
