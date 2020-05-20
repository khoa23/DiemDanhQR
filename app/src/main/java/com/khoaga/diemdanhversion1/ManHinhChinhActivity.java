package com.khoaga.diemdanhversion1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ManHinhChinhActivity extends AppCompatActivity {
    //View Objects
    private Button buttonScan;
    //private TextView textViewName, textViewAddress;
    public static TextView UserId;
    //qr code scanner object
    private IntentIntegrator qrScan;
    TextView tvIDNguoiDung;
    ResultSet resultSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chinh);

        //View objects
        buttonScan = (Button) findViewById(R.id.btnDiemDanh);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String idnguoidung = getIntent().getStringExtra("IDNGUOIDUNG");
                Intent intent = new Intent(ManHinhChinhActivity.this, ChonMonHocActivity.class);
                //intent.putExtra("IDNGUOIDUNG", idnguoidung);
                startActivity(intent);
            }
        });

    }
}