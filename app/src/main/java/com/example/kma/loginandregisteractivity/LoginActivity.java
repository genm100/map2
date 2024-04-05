package com.example.kma.loginandregisteractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kma.InFoAppActivity;
import com.example.kma.ManagerActivity;
import com.example.kma.R;
import com.example.kma.dao.DaoTaiKhoan;
import com.example.kma.model.TaikhoanMatKhau;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private Button btLogin, btndangki;
    EditText edtTaiKhoan, edtMatKhau;
    CheckBox cbLuuThongTin;
    DaoTaiKhoan tkDao;
    ArrayList<TaikhoanMatKhau> listTK = new ArrayList<>();
    LinearLayout linearLayout;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linearLayout = findViewById(R.id.linearLayoutlogin);
        init();
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        linearLayout.startAnimation(animation);
        layThongTin();

        Button btnForgotPassword = findViewById(R.id.btn_qmk);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
            }
        });


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean xetTk = false;
                tkDao = new DaoTaiKhoan(LoginActivity.this);
                String tenTK = edtTaiKhoan.getText().toString();
                String mk = edtMatKhau.getText().toString();
                listTK = tkDao.getALl();
                for (int i = 0; i < listTK.size(); i++) {
                    TaikhoanMatKhau tkx = listTK.get(i);
                    if (tkx.getTenTaiKhoan().matches(tenTK) && tkx.getMatKhau().matches(mk)) {
                        xetTk = true;
                        break;
                    }
                }
                if (xetTk == true) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    luuThongTin();
                    Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                    String username = tenTK;
                    intent.putExtra("USERNAME_KEY",username);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);


                } else {
                    Toast.makeText(LoginActivity.this, "Tên tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();

                }

            }
        });
//        btndangki.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
//            }
//        });

    }


    private void luuThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("sinhvien", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String ten = edtTaiKhoan.getText().toString();
        String pass = edtMatKhau.getText().toString();
        boolean check = cbLuuThongTin.isChecked();
        if (!check) {
            editor.clear();
        } else {
            editor.putString("tennguoidung", ten);
            editor.putString("matkhau", pass);
            editor.putBoolean("checkstatus", check);
        }
        editor.commit();

    }

    private void layThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("sinhvien", MODE_PRIVATE);

        boolean check = sharedPreferences.getBoolean("checkstatus", false);
        if (check) {
            String tenNguoiDung = sharedPreferences.getString("tennguoidung", "");
            String matKhau = sharedPreferences.getString("matkhau", "");
            edtTaiKhoan.setText(tenNguoiDung);
            edtMatKhau.setText(matKhau);
        } else {
            edtTaiKhoan.setText("");
            edtMatKhau.setText("");
        }
        cbLuuThongTin.setChecked(check);
    }

    private void init() {
        edtTaiKhoan = findViewById(R.id.edtUserName);
        edtMatKhau = findViewById(R.id.edtPassword);
        btLogin = findViewById(R.id.btnLogin);
        cbLuuThongTin = findViewById(R.id.cbLuuThongTin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            String tk = data.getStringExtra("taikhoan");
            String mk = data.getStringExtra("matkhau");
            edtTaiKhoan.setText(tk);
            edtMatKhau.setText(mk);
        }
    }
}
