package com.khoaga.diemdanhversion1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChonMonHocActivity extends AppCompatActivity {
//https://www.youtube.com/watch?v=wYdqM-u8aaI
    ListView lvMonHoc; // Listview
    ArrayList<String> arrayCourse;
    private boolean success = false; // boolean
    ResultSet resultSet;
    public static TextView UserId;
    private IntentIntegrator qrScan;
    int vitri = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_mon_hoc);

        lvMonHoc = (ListView)findViewById(R.id.listviewMonHoc);
        String idnguoidung = getIntent().getStringExtra("IDNGUOIDUNG");
        //intializing scan object




        String z = "";
        Boolean isSuccess = false; //Biến nhận biết là có truy vấn thành công hay không
        try {
            Connection con = SERVER.Connect(); //khởi tạo kết nối tới server, SERVER chính là class riêng, tìm trong table java
            if (con == null) {
                z = "Không thể kết nối với Server"; //Tiếng Việt :D
            } else {
                String query = "select l.MALOPMH, m.TENMONHOC  from MONHOC m, LOPMONHOC l, DANHSACHLOP d where m.MAMONHOC=l.MAMONHOC and l.MALOPMH=d.MALOPMH and d.IDNGUOIDUNG = "+idnguoidung+" group by m.TENMONHOC, l.MALOPMH";
                try {
                    Statement stmt = con.createStatement(); //blah blah blah
                    resultSet = stmt.executeQuery(query); //thực thi và trả về một cục ResultSet, nó là gì thì Google, tui chịu

                    arrayCourse = new ArrayList<>();
                    int columnCount = resultSet.getMetaData().getColumnCount();

                    while(resultSet.next())
                    {

                        for (int i = 1; i <columnCount ; i++)//i=0 hiện mã lớp môn học
                        {
                            arrayCourse.add(resultSet.getString(i+1));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(ChonMonHocActivity.this, android.R.layout.simple_list_item_1, arrayCourse);
                        lvMonHoc.setAdapter(adapter);
//https://www.youtube.com/watch?v=GWVEPkXBCMI
                        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //vitri = position;

                                qrScan.initiateScan();
                            }
                        });
                        con.close();
                    }

                } catch (SQLException e) {
                    Toast.makeText(ChonMonHocActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            z = "Lỗi !";
        }

        //qrScan = new IntentIntegrator(this);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //mở camera
        int MY_PERMISSIONS_REQUEST_CAMERA=0;
        // Here, this is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        String idnguoidung = getIntent().getStringExtra("IDNGUOIDUNG");
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    String GetQR = result.getContents();

                    String z = "";
                    Boolean isSuccess = false; //Biến nhận biết là có truy vấn thành công hay không
                    try {
                        Connection con = SERVER.Connect(); //khởi tạo kết nối tới server, SERVER chính là class riêng, tìm trong table java
                        if (con == null) {
                            z = "Không thể kết nối với Server"; //Tiếng Việt :D
                        } else {
                            String query = "select * from LICHHOC where MAQR='" + GetQR + "'";
                            //trên đây là câu truy vấn
                            Statement stmt = con.createStatement(); //blah blah blah
                            resultSet = stmt.executeQuery(query); //thực thi và trả về một cục ResultSet, nó là gì thì Google, tui chịu

                            //ResultSet rs = SERVER.executeQuery(query);
                            if(resultSet.next())//nếu trong resultset không null thì sẽ trả về True
                            {
                                con.close();
                                Intent intent = new Intent(ChonMonHocActivity.this, XacThucDiemDanhActivity.class);
                                intent.putExtra("IDNGUOIDUNG", idnguoidung);
                                startActivity(intent);
                            }
                            else
                            {
                                z = "Không có kết nối!";//Lại Tiếng Việt
                                isSuccess = false; //Chạy tới đây là hỏng rồi
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        isSuccess = false;
                        z = "Lỗi !";
                    }
                    //return z;
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
