package ru.startandroid.develop.simplenetworkconnector;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;


        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;

        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.android.material.textfield.TextInputEditText;
        import android.app.Activity;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.Button;
        import android.widget.ListAdapter;
        import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Collection;
        import java.util.Iterator;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.ListIterator;


public class SearchFood extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(this);
    private Button Search,reduce, increase, confirm_btn;
    private EditText str;
    private TextView cnt_txt;
    private ListView L1;
    private ListView L2;
    private String[] arr = new String[0];
    private String[] match = new String[0];
    private String[] order = new String[0];
    private int COUNT_FOOD = 0;
    private String selectedItem=new String();
    private FloatingActionButton fab;
    private String[] reply = new String[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton)findViewById(R.id.sendOrder);
        confirm_btn=(Button)findViewById(R.id.confirm);
        dbHelper = new DBHelper(this);
        Search=(Button)findViewById(R.id.search);
        str=(EditText)findViewById(R.id.searchingText);
        L1=(ListView)findViewById(R.id.list1);
        L2=(ListView)findViewById(R.id.list2);
        reduce = (Button)findViewById(R.id.reduce);
        increase = (Button)findViewById(R.id.increase);
        cnt_txt = (TextView)findViewById(R.id.count);

        reduce.setVisibility(View.INVISIBLE);
        increase.setVisibility(View.INVISIBLE);
        cnt_txt.setVisibility(View.INVISIBLE);
        confirm_btn.setVisibility(View.INVISIBLE);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("food", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("ID");
            int nameColIndex = c.getColumnIndex("NAME");
            int priceColIndex = c.getColumnIndex("PRICE");

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

        FloatingActionButton fab = findViewById(R.id.sendOrder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        addListenerOnButton();
        ////
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

    public void addListenerOnButton(){
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = str.getText().toString();
                match = Arrays.copyOf(match, 0);
                for(int i = 0; i < arr.length; i++){
                    if (arr[i].indexOf(s) != -1) {
                        match = Arrays.copyOf(match, match.length + 1);
                        match[match.length - 1]=arr[i];
                        Log.d(">>>",match[match.length - 1]);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchFood.this, android.R.layout.simple_list_item_1, match);
                L1.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                str.setText("");

            }
        });
        L1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // по позиции получаем выбранный элемент
                selectedItem = match[position];
                // установка текста элемента TextView
                Log.d(">>",selectedItem);

                increase.setVisibility(View.VISIBLE);
                reduce.setVisibility(View.VISIBLE);
                cnt_txt.setVisibility(View.VISIBLE);
                confirm_btn.setVisibility(View.VISIBLE);
                cnt_txt.setText(String.valueOf(COUNT_FOOD));
            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(COUNT_FOOD-1==-1){
                    Toast.makeText(SearchFood.this,"Error",Toast.LENGTH_LONG).show();
                }
                else{
                    COUNT_FOOD--;
                    cnt_txt.setText(String.valueOf(COUNT_FOOD));
                }
            }

        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COUNT_FOOD++;
                cnt_txt.setText(String.valueOf(COUNT_FOOD));
            }

        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = Arrays.copyOf(order, order.length+1);
                order[order.length-1]=selectedItem +" x "+COUNT_FOOD;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchFood.this, android.R.layout.simple_list_item_1, order);
                L2.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                reduce.setVisibility(View.INVISIBLE);
                increase.setVisibility(View.INVISIBLE);
                cnt_txt.setVisibility(View.INVISIBLE);
                confirm_btn.setVisibility(View.INVISIBLE);

                COUNT_FOOD=0;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    for(int i = 0; i < order.length ; i++){
                        ContentValues cv = new ContentValues();
                        cv.put("name", order[i]);
                        db.insert("orders",null, cv);
                    }
                    Toast.makeText(SearchFood.this,"Заказ отправлен на кухню",Toast.LENGTH_SHORT).show();
                    order = Arrays.copyOf(order, 0);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchFood.this, android.R.layout.simple_list_item_1, order);
                    L2.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                catch(Exception e){
                    Toast.makeText(SearchFood.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}