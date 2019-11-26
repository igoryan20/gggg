package ru.startandroid.develop.simplenetworkconnector;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;

public class AdminFood extends AppCompatActivity {

    private Button btnAdd,delButton;
    private EditText nameText;
    private EditText priceText;
    private DBHelper dbHelper = new DBHelper(this);
    private ListView listView;
    private String[] arr = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food);

        ///
        btnAdd=(Button)findViewById(R.id.addBtn);
        nameText=(EditText)findViewById(R.id.addName);
        priceText=(EditText)findViewById(R.id.addPrice);
        listView=(ListView)findViewById(R.id.list);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("food", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке

            int nameColIndex = c.getColumnIndex("NAME");
            int priceColIndex = c.getColumnIndex("PRICE");

            do {
                // получаем значения по номерам столбцов
                String str__ = "ИМЯ: " + c.getString(nameColIndex)+ ";ЦЕНА: " + c.getDouble(priceColIndex);
                arr = Arrays.copyOf(arr, arr.length + 1);
                arr[arr.length - 1] = str__;
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminFood.this, android.R.layout.simple_list_item_1, arr);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ///

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String name_java = nameText.getText().toString();
                    double price_java = Double.parseDouble(priceText.getText().toString());
                    ContentValues cv = new ContentValues();
                    cv.put("NAME", name_java);
                    cv.put("PRICE", price_java);
                    if (price_java <= 0) {
                        throw new RuntimeException();
                    }
                    // вставляем запись

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.insert("food", null, cv);

                    String str__ = "ИМЯ: " + name_java+ "; ЦЕНА: " + price_java;
                    arr = Arrays.copyOf(arr, arr.length + 1);
                    arr[arr.length - 1] = str__;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminFood.this, android.R.layout.simple_list_item_1, arr);

                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    nameText.setText("");
                    priceText.setText("");
                }
                catch(RuntimeException e){
                    Toast.makeText(AdminFood.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
