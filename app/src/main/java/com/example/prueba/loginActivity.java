package com.example.prueba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    EditText u1,u2;
    Button ba1,ba2;
    RequestQueue requestQueue;
    String correo,contraseña;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        u1 = findViewById(R.id.correo2);
        u2 = findViewById(R.id.contraseña2);
        ba1= findViewById(R.id.btnregistrarse2);
        ba1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),pruebaActivity.class);
                startActivity(i);
            }
        });
        ba2= findViewById(R.id.btningresar);
        recuperarpreferencias();

        ba2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarusuario("http://192.168.1.19:8080/emdicel/validar_usuario.php");

            }
        });

    }


    private void validarusuario(String URL){
        final String correo = u1.getText().toString().trim();
        final String contraseña = u2.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        if (correo.isEmpty()) {
            u1.setError("complete los campos");
        } else if (contraseña.isEmpty()) {
            u2.setError("complete los campos");
        } else{
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 if (!response.isEmpty()){
                     guardarpreferencias();
                     Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                     intent.putExtra("correo",correo);
                     startActivity(intent);
                     finish();
                 }else{
                     Toast.makeText(loginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                 }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<String, String>();
                parametros.put("correo", correo);
                parametros.put("contraseña",contraseña);
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    }
    private void guardarpreferencias() {
        SharedPreferences preferences = getSharedPreferences("preferenciaslogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo", correo);
        editor.putString("contraseña", contraseña);
        editor.putBoolean("sesion", true);
        editor.commit();
    }
    private void recuperarpreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferenciaslogin", Context.MODE_PRIVATE);
        u1.setText(preferences.getString("correo",""));
        u2.setText(preferences.getString("contraseña",""));
    }
}
