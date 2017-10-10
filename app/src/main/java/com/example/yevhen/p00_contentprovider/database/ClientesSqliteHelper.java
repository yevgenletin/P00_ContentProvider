package com.example.yevhen.p00_contentprovider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yevhen on 09/10/2017.
 */

public class ClientesSqliteHelper extends SQLiteOpenHelper {
    //Sentencia SQL para crear la tabla Clientes
    String sqlCreate = "CREATE TABLE Clientes " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nombre TEXT, " +
            " telefono TEXT, " +
            " email TEXT )";
    public ClientesSqliteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version){
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creacion de la tabla
        db.execSQL(sqlCreate);

        //Insertamos 15 clientes de ejemplo
        for (int i=1; i<=15; i++){
            //Generamos los datos
            String nombre = "Cliente" + i;
            String telefono = "900-123-00" + i;
            String email = "email" + i + "@mail.com";

            //Insertamos los datos en la tabla clientes
            db.execSQL("INSERT INTO Clientes(nombre, telefono, email) " +
                    "VALUES ('"+nombre+"', '"+telefono+"', '"+email+"')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    //Se elimina la tabla version anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Clientes");

        //Se Crea la nueva version de la tabla
        db.execSQL(sqlCreate);
    }
}
