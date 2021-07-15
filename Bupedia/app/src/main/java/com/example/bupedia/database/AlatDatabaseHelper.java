package com.example.bupedia.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bupedia.model.AlatModel;

import java.util.ArrayList;

public class AlatDatabaseHelper {
    private DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
    public void getAlat(QueryResponse<ArrayList<AlatModel>> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query("Tbl_alat", null,
                    null, new String[]{},
                    null, null, null);
            ArrayList<AlatModel> datas = new ArrayList<AlatModel>();
            Log.d("get alat ","->>>");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Log.d("get alat ","->>>>>"+  cursor.getString(cursor.getColumnIndex("id")));
                    AlatModel alatModel = new AlatModel(
                            cursor.getString(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("nama_alat")),
                            cursor.getBlob(cursor.getColumnIndex("foto_alat")),
                            cursor.getString(cursor.getColumnIndex("jenis")),
                            cursor.getString(cursor.getColumnIndex("deskripsi_alat"))
                    );
                    datas.add(alatModel);
                } while (cursor.moveToNext());
                response.onSuccess(datas,0 );
            } else {
                response.onFailure("Something wrong");
            }

        } catch (Exception e) {
            Log.d("get alat ","->>>"+e.getMessage());
            e.printStackTrace();
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
        }
    }
}
