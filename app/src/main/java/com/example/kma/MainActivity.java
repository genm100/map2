package com.example.kma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.kma.loginandregisteractivity.LoginActivity;
import com.example.kma.R;
import com.example.kma.adapter.SinhVienAdapter;
import com.example.kma.dao.SinhVienDao;
import com.example.kma.model.SinhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText edtSearch;
    public static ArrayList<SinhVien> ds = new ArrayList<>();
    ArrayList<SinhVien> timKiem = new ArrayList<>();

    SinhVienAdapter sinhVienAdapter;
    FloatingActionButton fbadd;

    SinhVienDao sinhVienDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtSearch = findViewById(R.id.edtsearch);
        listView = findViewById(R.id.listSV);
        fbadd = findViewById(R.id.fbadd);

        sinhVienDao = new SinhVienDao(MainActivity.this);
        if (DanhSachLopActivity.xetList == true) {
            ds = DanhSachLopActivity.svlistDuocLoc;
        } else {
            ds = sinhVienDao.getALL();
        }
        timKiem = sinhVienDao.getALL();
        sinhVienAdapter = new SinhVienAdapter(MainActivity.this, ds);
        listView.setAdapter(sinhVienAdapter);

        fbadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemSinhVienActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
            }
        });
        listView.setTextFilterEnabled(true);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    sinhVienAdapter.resetData();
                } else {
                    sinhVienAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
