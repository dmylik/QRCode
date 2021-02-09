package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter adapter;
    private List<String> listData;
    private List<User> listTemp;

    TextView txtInv;

    ArrayList<Product> products = new ArrayList<Product>();
    BoxAdapter boxAdapter;

    // База данных FireBase
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        init();
        getDataFromDB();
        boxAdapter = new BoxAdapter(this, products);
        setOnClickItem();
    }

    private void init() {
        txtInv = findViewById(R.id.txtInv);
        listView = findViewById(R.id.listView);

        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        //Обработка нажатия Круглой Кнопки и переход к Сканеру
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReadActivity.this, ScannedBarcodeActivity.class));
            }
        });
    }

    // Полученние данных и базы FireBase, занесение их в список
    private void getDataFromDB() {
        ValueEventListener vListnener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() > 0) listData.clear();
                if(listTemp.size() > 0) listTemp.clear();
                if(products.size() > 0) products.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    assert user != null;
                    txtInv.setText(user.AMOUNT);
                    String[] osnDel = user.OSNAME.split("\n");
//                     Если устройство не было ранее найдено
                    if (user.POISK.equals("0"))
                        products.add(new Product(osnDel[2], "Инв: " + user.INVNUM + ", " + user.SAMOUNT + " из " + user.AMOUNT, R.drawable.x));
//                         Если устройство было найдено
                    else
                        products.add(new Product(osnDel[2],"Инв: " + user.INVNUM + ", " + user.SAMOUNT + " из " + user.AMOUNT, R.drawable.v));


                    listData.add(user.OSNAME);
                    listTemp.add(user);
                }


                ListView lvMain = (ListView) findViewById(R.id.listView);
                lvMain.setAdapter(boxAdapter);

//                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDataBase.addValueEventListener(vListnener);
    }

    //Обработка нажатя на определенную строку в Списке
    private void setOnClickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = listTemp.get(position);
                Intent i = new Intent(ReadActivity.this, ShowActivity.class);

                i.putExtra("INVNUM", user.INVNUM);
                i.putExtra("TABNUM", user.TABNUM);
                i.putExtra("OSNAME", user.OSNAME);
                i.putExtra("AMOUNT", user.AMOUNT);
                i.putExtra("SAMOUNT", user.SAMOUNT);
                i.putExtra("DATE", user.DATE  );
                i.putExtra("POISK", user.POISK  );

                startActivity(i);
            }
        });
    }
}
