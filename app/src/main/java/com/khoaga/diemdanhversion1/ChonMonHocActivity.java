package com.khoaga.diemdanhversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChonMonHocActivity extends AppCompatActivity {

    private ArrayList<ClassListMonHoc> itemArrayList;  //List items Array
    private Adapter myAppAdapter; //Array Adapter
    private ListView listView; // Listview
    private boolean success = false; // boolean
    ResultSet resultSet;
    SimpleAdapter ADAhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_mon_hoc);

        listView = (ListView)findViewById(R.id.listviewMonHoc);
        itemArrayList = new ArrayList<ClassListMonHoc>(); // Arraylist Initialization
        String z = "";
        Boolean isSuccess = false; //Biến nhận biết là có truy vấn thành công hay không

        try {
            Connection con = SERVER.Connect(); //khởi tạo kết nối tới server, SERVER chính là class riêng, tìm trong table java
            if (con == null) {
                z = "Không thể kết nối với Server"; //Tiếng Việt :D
            } else {
                String query = "select m.tenmonhoc from MONHOC m, LOPMONHOC l, DANHSACHLOP d where m.MAMONHOC=l.MAMONHOC and l.MALOPMH=d.MALOPMH and d.IDNGUOIDUNG = 1 group by TENMONHOC";
                try {
                    Statement stmt = con.createStatement(); //blah blah blah
                    resultSet = stmt.executeQuery(query); //thực thi và trả về một cục ResultSet, nó là gì thì Google, tui chịu
                    List<Map<String, String>> data = null;
                    data = new ArrayList<Map<String, String>>();
                    while (resultSet.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", resultSet.getString("tenmonhoc"));

                        data.add(datanum);
                    }
                    String[] fromwhere = { "A" };
                    int[] viewswhere = {R.id.textName };
                    ADAhere = new SimpleAdapter(ChonMonHocActivity.this, data, R.layout.activity_dong_mon_hoc, fromwhere, viewswhere);
                    listView.setAdapter(ADAhere);
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
    }



}
