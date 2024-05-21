package com.example.btth4;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtMaLop, edtTenLop, edtSiSo;
    Button btnInsert, btnDelete, btnUpdate, btnQuery;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtMaLop = findViewById(R.id.edtMaLop);
        edtTenLop = findViewById(R.id.edtTenLop);
        edtSiSo = findViewById(R.id.edtSiSo);
        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnQuery = findViewById(R.id.btnQuery);
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);
        mydatabase = openOrCreateDatabase("qlsinhvien.db", MODE_PRIVATE, null);
        try {
            String sql = "CREATE TABLE tbllop(MaLop TEXT PRIMARY KEY, TenLop TEXT, SiSo INTEGER)";
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            Log.e("Error", "Table đã tồn tại");
        }

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MaLop = edtMaLop.getText().toString();
                String TenLop = edtTenLop.getText().toString();
                int SiSo = Integer.parseInt(edtSiSo.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("MaLop", MaLop);
                myvalue.put("TenLop", TenLop);
                myvalue.put("SiSo", SiSo);
                String msg = "";
                if (mydatabase.insert("tbllop", null, myvalue) == -1) {
                    msg = "Fail to Insert Record!";
                } else {
                    msg = "Insert record Sucessfully";
                    // Cập nhật lại ListView
                    updateListView();
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MaLop = edtMaLop.getText().toString();
                int n = mydatabase.delete("tbllop", "MaLop = ?", new String[]{MaLop});
                String msg = "";
                if (n == 0) {
                    msg = "No record to Delete";
                } else {
                    msg = n + " record is deleted";
                    // Cập nhật lại ListView
                    updateListView();
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SiSo = Integer.parseInt(edtSiSo.getText().toString());
                String MaLop = edtMaLop.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("SiSo", SiSo);
                int n = mydatabase.update("tbllop", myvalue, "MaLop = ?", new String[]{MaLop});
                String msg = "";
                if (n == 0) {
                    msg = "No record to Update";
                } else {
                    msg = n + " record is updated";
                    // Cập nhật lại ListView
                    updateListView();
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.clear();
                Cursor c = mydatabase.query("tbllop", null, null, null, null, null, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    String data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2);
                    mylist.add(data);
                    c.moveToNext();
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });
    }

    // Phương thức cập nhật ListView
    private void updateListView() {
        mylist.clear();
        Cursor c = mydatabase.query("tbllop", null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2);
            mylist.add(data);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();
    }
}