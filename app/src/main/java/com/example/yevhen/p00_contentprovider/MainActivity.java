package com.example.yevhen.p00_contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yevhen.p00_contentprovider.provider.ClientesProvider;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button consultar, insertar, eliminar, llamadas;
    TextView resultado;
    Uri clientesUri = ClientesProvider.CONTENT_URI;
    ContentResolver contentResolver;

    String[] projection = new String[]{
            ClientesProvider.Clientes._ID,
            ClientesProvider.Clientes.COL_NOMBRE,
            ClientesProvider.Clientes.COL_TELEFONO,
            ClientesProvider.Clientes.COL_EMAIL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultado = (TextView) findViewById(R.id.txtResultados);
        consultar = (Button) findViewById(R.id.query);
        insertar = (Button) findViewById(R.id.insert);
        eliminar = (Button) findViewById(R.id.delete);
        llamadas = (Button) findViewById(R.id.calls);

        consultar.setOnClickListener(this);
        insertar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
        llamadas.setOnClickListener(this);

        contentResolver = getApplicationContext().getContentResolver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query:
                queryClient();
                break;
            case R.id.insert:
                insertClient();
                break;
            case R.id.delete:
                deleteClient();
                break;
            case R.id.calls:
                getCalls();
                break;
            default:
                break;
        }
    }

    private void deleteClient() {
        contentResolver.delete(ClientesProvider.CONTENT_URI, ClientesProvider.Clientes.COL_NOMBRE + "= 'ClienteN'", null);
    }

    private void insertClient() {
        ContentValues values = new ContentValues();
        values.put(ClientesProvider.Clientes.COL_NOMBRE, "ClienteN");
        values.put(ClientesProvider.Clientes.COL_TELEFONO, "9999999");
        values.put(ClientesProvider.Clientes.COL_EMAIL, "nuevo@email.com");

        contentResolver.insert(ClientesProvider.CONTENT_URI, values);
    }

    public void queryClient(){
        Cursor cursor = contentResolver.query(
                clientesUri,
                projection,//Columnas a devolver
                null,//Condicion de la query
                null,//Argumentos variables de la query
                null // Orden de los resultados
        );
        if(cursor.moveToFirst())
        {
            String nombre;
            String telefono;
            String email;

            int colNombre = cursor.getColumnIndex(ClientesProvider.Clientes.COL_NOMBRE);
            int colTelefono = cursor.getColumnIndex(ClientesProvider.Clientes.COL_TELEFONO);
            int colEmail = cursor.getColumnIndex(ClientesProvider.Clientes.COL_EMAIL);

            resultado.setText("");

            do{
                nombre = cursor.getString(colNombre);
                telefono = cursor.getString(colTelefono);
                email = cursor.getString(colEmail);

                resultado.append(nombre + " - " + telefono + " - " + email + "/n");
            }while (cursor.moveToNext());
        }
    }

    public void getCalls() {
        String[] projection = new String[]{
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER
        };

        Uri llamadasUri = CallLog.Calls.CONTENT_URI;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = contentResolver.query(
                llamadasUri,
                projection,
                null,
                null,
                null);
        if(cursor.moveToFirst())
        {
            int tipo;
            String tipoLlamada = "";
            String telefono;

            int colTipo = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int colTelefono = cursor.getColumnIndex(CallLog.Calls.NUMBER);

            resultado.setText("");

            do{
                tipo = cursor.getInt(colTipo);
                telefono = cursor.getString(colTelefono);

                if(tipo == CallLog.Calls.INCOMING_TYPE){
                    tipoLlamada = "Entrada";
                }else if(tipo == CallLog.Calls.OUTGOING_TYPE){
                    tipoLlamada = "Salida";
                }else if(tipo == CallLog.Calls.MISSED_TYPE){
                    tipoLlamada = "Perdida";
                }

                resultado.append(tipoLlamada + " - " + telefono + "\n");
            }while(cursor.moveToNext());
        }
    }
}
