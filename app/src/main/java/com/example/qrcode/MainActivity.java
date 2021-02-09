package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnTakePicture, btnScanBarcode, btnJSON;
    private static DatabaseReference mDataBase;
    private static String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        init();

    }

    private void init() {
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnJSON = findViewById(R.id.btnJSON);
//        btnWriteFile = findViewById(R.id.btnWriteFile);

        btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
        btnJSON.setOnClickListener(this);

//        btnWriteFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Переход к списку
            case R.id.btnTakePicture:
                startActivity(new Intent(MainActivity.this, ReadActivity.class));
                break;
            //Переход к Скунеру
            case R.id.btnScanBarcode:
                startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
                break;
                //Инструкция
            case R.id.btnJSON:
                startActivity(new Intent(MainActivity.this, Instruction.class));
                break;
        }

    }

//    static void importFromJSON() throws IOException {
//        // Доступ к встоенной памяти устройства
//        File sdCard = Environment.getExternalStorageDirectory();
//        File directory = new File(sdCard.getAbsolutePath() + "/MyDB/1");
//        directory.mkdirs();
//
//        // Открытие файла и отправление его в поток
//        File dbFile = new File(directory, "FileQR.txt");
//
//        // Разбор потока и побитовое копирование в
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(
//                        new FileInputStream(dbFile), "UTF8"));
//
//        String str;
//        //Занесенние данных в базу данных
//        while ((str = in.readLine()) != null) {
////            String line = in.readLine();
//            String[] s = str.split("\t");
//            User newUser = new User(s[1],s[2],s[3],s[5],"0",s[4],"0");
//            mDataBase.push().setValue(newUser);
//        }
//
//        // Закрыте базы данных
//        in.close();
//    }
}


