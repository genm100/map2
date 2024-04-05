package com.example.kma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kma.R;
import com.example.kma.dao.SinhVienDao;
import com.example.kma.model.SinhVien;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QRScannerActivity extends AppCompatActivity {
    Button scanbtn;
    Button dsdiemdanhbtn;
    String simpleFileName = "diemDanh.txt";
    public static TextView scantext;
    public static TextView dstext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        dsdiemdanhbtn=(Button) findViewById(R.id.dsdiemdanhbtn);
        dstext=(TextView) findViewById(R.id.dstext);
        scantext=(TextView)findViewById(R.id.scantext);
        scanbtn=(Button) findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),qrresult.class));

            }
        });
        dsdiemdanhbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readData();

            }
        });
    }

    private void readData() {
        try {
            // Open stream to read file.
            FileInputStream in = this.openFileInput(simpleFileName);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s).append("\n");
            }
            Toast.makeText(QRScannerActivity.this,"Hiển thị danh sách thành công!",Toast.LENGTH_SHORT).show();
            this.dstext.setText(sb.toString());

        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
