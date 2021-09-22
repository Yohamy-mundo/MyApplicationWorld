package com.itca.myapplicationworld;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.itca.myapplicationworld.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion.db" ,null,1);

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private EditText et_codigo, et_descripcion, et_precio;
    private Button btnAlta, btnConsulta1, btnConsulta2, btnEliminar, btnActualizar, btnNuevo, btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/

        et_codigo = findViewById(R.id.et_codigo);
        et_descripcion = findViewById(R.id.et_descripcion);
        et_precio = findViewById(R.id.et_precio);

        btnAlta = findViewById(R.id.btnAlta);
        btnConsulta1 = findViewById(R.id.btnConsulta1);
        btnConsulta2 = findViewById(R.id.btnConsulta2);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnNuevo = findViewById(R.id.btnNuevo);
        btnSalir = findViewById(R.id.btnSalir);

        btnAlta.setOnClickListener(this);
        btnConsulta1.setOnClickListener(this);
        btnConsulta2.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnNuevo.setOnClickListener(this);
        btnSalir.setOnClickListener(this);


        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        SQLiteDatabase bd = admin.getWritableDatabase();
        AdminSQLiteOpenHelper admin =new AdminSQLiteOpenHelper(this,"administracion.db" ,null,1) ;
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("codigo", codigo);
        registro.put("descripcion", descripcion);
        registro.put("precio", precio);
        bd.insert("articulos", null, registro);

        switch (view.getId()) {
            case R.id.btnAlta:
                //Toast.makeText(this, "has hecho click en el botn Alta", Toast.LENGTH_SHORT).show();

                if(codigo.isEmpty()){
                    et_codigo.setError("campo obligatorio.");
                }else if(descripcion.isEmpty()){
                    et_descripcion.setError("campo obligatorio");
                }else if(precio.isEmpty()){
                    et_precio.setError("Campo obligatorio");
                }else{

                    bd.close();
                    et_codigo.setText(null);
                    et_descripcion.setText(null);
                    et_precio.setText(null);
                    Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnConsulta1:
                //Toast.makeText(this, "has hecho click en el botn Consulta1", Toast.LENGTH_SHORT).show();
                codigo = et_codigo.getText().toString();
                if(codigo.isEmpty()){
                    et_codigo.setError("campo obligatorio");
                }else{
                    Cursor fila =bd.rawQuery("select descripcion, precio from articulos where codigo="+codigo, null);
                    if(fila.moveToFirst()){
                        et_descripcion.setText(fila.getString(0));
                        et_precio.setText(fila.getString(1));
                    }else{
                        Toast.makeText(this, "No existe articulo con dicho codigo", Toast.LENGTH_SHORT).show();
                    }
                    bd.close();
                }
                break;
            case R.id.btnConsulta2:
                //Toast.makeText(this, "has hecho click en el botn Consulta2", Toast.LENGTH_SHORT).show();
                descripcion = et_descripcion.getText().toString();
                if(descripcion.isEmpty()){
                    et_descripcion.setError("campo obligatorio");
                }else{
                    Cursor fila =bd.rawQuery("select codigo, precio from articulos where descripcion ='" + descripcion +"'", null);
                    if(fila.moveToFirst()){
                        et_codigo.setText(fila.getString(0));
                        et_precio.setText(fila.getString(1));
                    }else{
                        Toast.makeText(this, "No existe articulo con dicha descripcion", Toast.LENGTH_SHORT).show();
                    }
                    bd.close();
                }
                break;
            case R.id.btnEliminar:
                //Toast.makeText(this, "has hecho click en el botn Eliminar", Toast.LENGTH_SHORT).show();
                codigo = et_codigo.getText().toString();
                int cant = bd.delete("articulos", "codigo" + codigo, null);
                bd.close();
                et_codigo.setText("");
                et_descripcion.setText("");
                et_precio.setText("");

                if(cant == 1){
                    Toast.makeText(this, "Has eliminado el registro", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "No existe el articulo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnActualizar:
                //Toast.makeText(this, "has hecho click en el botn Actualizar", Toast.LENGTH_SHORT).show();
                codigo = et_codigo.getText().toString();
                descripcion = et_descripcion.getText().toString();
                precio = et_precio.getText().toString();

                registro.put("codigo",codigo);
                registro.put("descripcion",descripcion);
                registro.put("precio",precio);

                cant = bd.update("articulos", registro, "codigo=" + codigo, null);
                bd.close();

                if(cant == 1){
                    Toast.makeText(this, "Has actualizado el articulo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "No existe el articulo", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnNuevo:
                /*Toast.makeText(this, "has hecho click en el botn Nuevo", Toast.LENGTH_SHORT).show();
                et_codigo = (EditText) findViewById(R.id.et_codigo);
               et_descripcion = (EditText) findViewById(R.id.et_descripcion);
                et_precio = (EditText) findViewById(R.id.et_precio);
                final String clear = new String("Ingrese Datos");*/

                et_codigo.setText("");
                et_descripcion.setText("");
                et_precio.setText("");

                break;
            case R.id.btnSalir:
                //Toast.makeText(this, "has hecho click en el botn Salir", Toast.LENGTH_SHORT).show();
                // System.exit(0);
                finish();

                break;
        }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
    }
}