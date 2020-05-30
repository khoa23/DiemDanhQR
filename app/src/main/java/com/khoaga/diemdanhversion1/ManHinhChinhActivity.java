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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManHinhChinhActivity extends AppCompatActivity implements View.OnClickListener{
    //View Objects
    private Button buttonScan;
    //private TextView textViewName, textViewAddress;
    public static TextView UserId;
    //qr code scanner object
    private IntentIntegrator qrScan;
    TextView tvIDNguoiDung;
    ResultSet resultSet;

    //lấy ngày giờ
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
    String currentDateandTime = sdf.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chinh);

        //View objects
        buttonScan = (Button) findViewById(R.id.btnDiemDanh);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);

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
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String idnguoidung = getIntent().getStringExtra("IDNGUOIDUNG");
        int id = Integer.parseInt(idnguoidung);
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
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    UserId = findViewById(R.id.textView4);

                    String GetQR = result.getContents();

                    String[] parts = GetQR.split("-",3); //cắt chuối mã QR, ngăn bởi -, lấy 2 đoạn đầu, tăng đoạn nhớ chỉnh limit

                    String part0 = parts[0];//đoạn 0 BUOITHU
                    String part1= parts[1];//đoạn 1 IDLOPMH
                    int idlopmh = Integer.parseInt(part1);
                    String part2 = parts[2];//đoạn 2
                    //String part3 = parts[3];//đoạn 3

                    //hiện mã qr để quét ra text
                    UserId.setText(part0+"\n"+idlopmh+"   "+part2);
                    UserId = findViewById(R.id.textView4);

                    String z = "";
                    Boolean isSuccess = false; //Biến nhận biết là có truy vấn thành công hay không
                    try {
                        Connection con = SERVER.Connect(); //khởi tạo kết nối tới server, SERVER chính là class riêng, tìm trong table java
                        if (con == null) {
                            z = "Không thể kết nối với Server"; //Tiếng Việt :D
                        } else {
                            String query = "select * from BUOIHOC where MAQR='" + GetQR + "'";
                            //trên đây là câu truy vấn
                            Statement stmt = con.createStatement(); //blah blah blah
                            resultSet = stmt.executeQuery(query); //thực thi và trả về một cục ResultSet, nó là gì thì Google, tui chịu

                            //ResultSet rs = SERVER.executeQuery(query);
                            if(resultSet.next())//nếu trong resultset không null thì sẽ trả về True
                            {
                                //thêm vào bảng CTDIEMDANH
                                String diemdanh = "INSERT INTO CTDIEMDANH(BUOITHU, IDNGUOIDUNG, IDLOPMH, NGAYGIO) VALUES ("+part0+", "+id+", "+idlopmh+",(SELECT GETDATE() AS CurrentTime));";
                                //trên đây là câu truy vấn
                                Statement dd = con.createStatement(); //blah blah blah
                                resultSet = dd.executeQuery(diemdanh); //thực thi và trả về một cục ResultSet, nó là gì thì Google, tui chịu

                                con.close();//đóng kết nối
                                //Intent intent = new Intent(ManHinhChinhActivity.this, XacThucDiemDanhActivity.class);
                                //intent.putExtra("IDNGUOIDUNG", idnguoidung);
                                //startActivity(intent);

                                Toast.makeText(this, "Điểm đanh thành công", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                z = "Không có mã qr trong cơ sở dữ liệu!";//Lại Tiếng Việt
                                isSuccess = false; //Chạy tới đây là hỏng rồi
                                con.close();//đóng kết nối
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
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}