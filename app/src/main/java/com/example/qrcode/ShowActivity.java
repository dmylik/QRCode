package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {
    TextView txtINVNUM, txtTABNUM, txtOSNAME, txtAMOUNT, txtName, txtDATE;
    ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        init();
        getIntentMain();
    }

    private void init() {
        txtINVNUM = findViewById(R.id.txtINVNUM);
        txtTABNUM = findViewById(R.id.txtTABNUM);
        txtOSNAME = findViewById(R.id.txtOSNAME);
        txtAMOUNT = findViewById(R.id.txtAMOUNT);
        txtDATE = findViewById(R.id.txtDATE);
        txtName = findViewById(R.id.txtName);
        imageView = findViewById(R.id.imagePoisk);

    }

    //Вывод значений на отдельное окно для полученния всех данных
    private  void getIntentMain(){
        Intent i = getIntent();
        if(i != null){
            String inv =  i.getStringExtra("INVNUM");
            String tab =  i.getStringExtra("TABNUM");
            String osn = i.getStringExtra("OSNAME");
            String am = i.getStringExtra("AMOUNT");
            String sm = i.getStringExtra("SAMOUNT");
            String dt = i.getStringExtra("DATE");
            String ps = i.getStringExtra("POISK");

            // Разделитель
            assert osn != null;
            String[] osnDel = osn.split("\n");

            txtName.setText(osnDel[2]);
            txtINVNUM.setText(inv);
            txtTABNUM.setText(tab);
            txtOSNAME.setText(osnDel[0] + ", " +osnDel[1]);
            txtAMOUNT.setText(sm + "/" + am);
            txtDATE.setText(dt);
            if(ps.equals("1"))
                imageView.setImageResource(R.drawable.vb);
            else
                imageView.setImageResource(R.drawable.xb);


        }

    }





}
