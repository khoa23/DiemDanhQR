package com.khoaga.diemdanhversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class XacThucDiemDanhActivity extends AppCompatActivity {

    private Button buttonXacThuc;
    ResultSet resultSet;
    EditText editTextmaxacthuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_thuc_diem_danh);

        buttonXacThuc = (Button)findViewById(R.id.btnXacThuc);

        buttonXacThuc.setOnClickListener(xacthuc);
    }

    private View.OnClickListener xacthuc = new View.OnClickListener() {
        public void onClick(View v) {
            // so sanh ma qr

            //truy vấn mã xác thực trong database
            //chưa có cột mã xác thực nên lấy tạm mã qr
            String z = "";
            Boolean isSuccess = false; //Biến nhận biết là có truy vấn thành công hay không
            editTextmaxacthuc = (EditText)findViewById(R.id.editTextMaXacThuc);
            try {
                Connection con = SERVER.Connect(); //khởi tạo kết nối tới server, SERVER chính là class riêng, tìm trong table java
                if (con == null) {
                    z = "Không thể kết nối với Server"; //Tiếng Việt :D
                } else {
                    String query = "select * from LICHHOC where MAXACNHAN='" + editTextmaxacthuc.getText() + "'";
                    //trên đây là câu truy vấn
                    Statement stmt = con.createStatement(); //blah blah blah
                    resultSet = stmt.executeQuery(query); //thực thi và trả về một cục ResultSet, nó là gì thì Google, tui chịu

                    //ResultSet rs = SERVER.executeQuery(query);
                    if(resultSet.next())//nếu trong resultset không null thì sẽ trả về True
                    {
                        con.close();
                        //lưu tên và thông tin vào database nếu mã đúng

                        // chuyen man hinh neu ĐÚNG
                        Intent intent = new Intent(XacThucDiemDanhActivity.this, ManHinhChinhActivity.class);
                        startActivity(intent);
                        Toast.makeText(XacThucDiemDanhActivity.this, "Điểm danh thành công!", Toast.LENGTH_LONG).show();
                    }
                    else
                        if(resultSet.next() == false)
                        {
                            Toast.makeText(XacThucDiemDanhActivity.this, "Sai mã!", Toast.LENGTH_LONG).show();
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


        }
    };
}
