package com.example.kma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kma.loginandregisteractivity.LoginActivity;
import com.example.kma.R;
import com.example.kma.adapter.LopAdapter;
import com.example.kma.dao.LopDao;
import com.example.kma.dao.SinhVienDao;
import com.example.kma.model.Lop;
import com.example.kma.model.SinhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DanhSachLopActivity extends AppCompatActivity {
    FloatingActionButton fbadd;

    TextView tvanhien;
    EditText edtSearch;

    ArrayList<Lop> dsLop = new ArrayList<>();
    ArrayList<Lop> timKiem = new ArrayList<>();

    ArrayList<SinhVien> svlist;
    static ArrayList<SinhVien> svlistDuocLoc;
    public static boolean xetList = true;

    ListView listView;
    LopAdapter lopAdapter;

    LopDao lopDao;
    SinhVienDao sinhVienDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_lop);
        listView = findViewById(R.id.listviewLop);
        fbadd = findViewById(R.id.fbThemLop);
        tvanhien = findViewById(R.id.tvAnHien);

        edtSearch = findViewById(R.id.edtserchLop);

        lopDao = new LopDao(DanhSachLopActivity.this);

        dsLop = lopDao.getAll();
        timKiem = lopDao.getAll();

        fbadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DanhSachLopActivity.this, ThemLopActivity.class));
            }
        });

        lopAdapter = new LopAdapter(DanhSachLopActivity.this, R.layout.dong_lop, dsLop);
        listView.setAdapter(lopAdapter);

        if (dsLop.size() == 0) {
            listView.setVisibility(View.INVISIBLE);
            tvanhien.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvanhien.setVisibility(View.INVISIBLE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String maLop = dsLop.get(position).toString();
                sinhVienDao = new SinhVienDao(DanhSachLopActivity.this);
                svlist = sinhVienDao.getALL();
                int dem = 0;
                svlistDuocLoc = new ArrayList<>();
                for (int i = 0; i < svlist.size(); i++) {

                    SinhVien sv = svlist.get(i);
                    if (maLop.matches(sv.getMaLop())) {
                        svlistDuocLoc.add(svlist.get(i));
                        dem++;
                    }
                }
                if (dem > 0) {
                    Intent i = new Intent(DanhSachLopActivity.this, MainActivity.class);
                    xetList = true;
                    startActivity(i);
                } else {
                    Toast.makeText(DanhSachLopActivity.this, "Không có sinh viên nào thuộc mã lớp " + dsLop.get(position), Toast.LENGTH_LONG).show();
                }
            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Search or Filter

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    lopAdapter.resetData();

                } else {
                    lopAdapter.getFilter().filter(s);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
