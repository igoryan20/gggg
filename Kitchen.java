package ru.startandroid.develop.simplenetworkconnector;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

public class Kitchen extends AppCompatActivity {

    private ListView listView;
    private DBHelper dbHelper;
    private String[] arr= new String[0];
    private String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        dbHelper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.list);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orders", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int readyColIndex = c.getColumnIndex("ID");
            int nameColIndex = c.getColumnIndex("NAME");


            do {
                // получаем значения по номерам столбцов
                String str__ = c.getString(nameColIndex);
                arr = Arrays.copyOf(arr, arr.length + 1);
                arr[arr.length - 1] = str__;
                Log.d(">>>", arr[arr.length - 1]);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Kitchen.this, android.R.layout.simple_list_item_1, arr);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        addListenerOnButton();

    }

    public void addListenerOnButton() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
            {
                // по позиции получаем выбранный элемент
                selectedItem = arr[position];
                // установка текста элемента TextView
                Log.d(">>",selectedItem);
                AlertDialog.Builder ad = new AlertDialog.Builder(Kitchen.this);
                ad.setTitle("Подтверждение");
                ad.setPositiveButton( "Подтвердить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("orders","name = " + selectedItem, null);
                        for(int j = position; j < arr.length-1; j++){
                            arr[j] = arr[j+1];
                        }
                        arr=Arrays.copyOf(arr, arr.length-1);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Kitchen.this, android.R.layout.simple_list_item_1, arr);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
                ad.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });

            }
        });
    }



}
