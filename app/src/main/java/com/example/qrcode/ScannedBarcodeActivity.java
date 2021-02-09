package com.example.qrcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScannedBarcodeActivity extends AppCompatActivity {
    private static final int SHORT_DELAY = 2000; // 2 seconds

    SurfaceView surfaceView;
    TextView txtBarcodeValue, textModal;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    private String textQR;

    String textINV;

    //База данных FireBase
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";

    public boolean poisk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readDataBase(getTextQR());
            }
        });

    }


    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Сканер QR-кода запущен", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                setTextQR(intentData);
                            }
                            else {
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                setTextQR(intentData);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

//     Проверка значений QR-кода со начениями базы данных
    public void readDataBase(String textQr){
        String str = textQr;
        String delimeter = "; ";

        // Определение даты
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

//        String data1 = format.format(new Date());
        // Определение даты и времени
        final DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        final String date = df.format(Calendar.getInstance().getTime());

        // Разделитель
        // Разделение Сроки полученной м
        String[] subStr = str.split(delimeter);
        final String invNumQr = subStr[1].substring(5,11);


        ValueEventListener vListnener = new ValueEventListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    String data = user.TABNUM;
                    //Проверка значений базы данных и данных QR-кода
                    if(invNumQr.equals(data)){
                        String childName = ds.getKey();
                        setTextINV(invNumQr);

                        String samount = SAmount(user.SAMOUNT, user.AMOUNT);

                        //Обновленние данных в случае совпадения
                        mDataBase.child(childName).child("POISK").setValue("1");
                        mDataBase.child(childName).child("SAMOUNT").setValue(samount);
                        mDataBase.child(childName).child("DATE").setValue(date);

                        txtBarcodeValue.setText("");
                        setPoisk(true);
                        //Модальное окно
                        if(isPoisk())
                            Toast.makeText(getApplicationContext(), "Данный элемент найден", SHORT_DELAY).show();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mDataBase.addValueEventListener(vListnener);
    }

    // Проверка множества одинаковых кодов
    public String SAmount(String s, String a){
        String rez;

        int ai = Integer.parseInt(a);
        int si = Integer.parseInt(s);

        if(ai==1)
            si=1;
        else if(ai>1 && ai==si)
            si=ai;
        else if(ai>1 && ai>si)
            si++;

        return String.valueOf(si);
    }

    public String getTextQR() {
        return textQR;
    }

    public void setTextQR(String textQR) {
        this.textQR = textQR;
    }

    public String getTextINV() {
        return textINV;
    }

    public void setTextINV(String textINV) {
        this.textINV = textINV;
    }

    public boolean isPoisk() {
        return poisk;
    }

    public void setPoisk(boolean poisk) {
        this.poisk = poisk;
    }
}
