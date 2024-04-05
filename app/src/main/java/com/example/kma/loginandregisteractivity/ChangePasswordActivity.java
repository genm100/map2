package com.example.kma.loginandregisteractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kma.dao.DaoTaiKhoan;
import com.example.kma.model.TaikhoanMatKhau;
import com.example.kma.R;

import java.util.ArrayList;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText txtCTk, txtCpass, txtNewPass;
    Button btChangePass, btNhapLai;
    DaoTaiKhoan tkDao;
    Animation animation;
    LinearLayout linearLayout;
    ArrayList<TaikhoanMatKhau> listTk = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        linearLayout=findViewById(R.id.linearLayoutchange);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        linearLayout.setAnimation(animation);
        btChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean xetTk = false, xetMk = true;
                tkDao = new DaoTaiKhoan(ChangePasswordActivity.this);
                String tk = txtCTk.getText().toString();
                String mk = txtCpass.getText().toString();
                String mkk = txtNewPass.getText().toString();
                TaikhoanMatKhau tkmkMoi = new TaikhoanMatKhau(tk,mkk);
                listTk = tkDao.getALl();

                //Check tk, mk có khớp vs tk trong list k
                for (int i = 0; i < listTk.size(); i++) {
                    TaikhoanMatKhau tkx = listTk.get(i);
                    if (tkx.getTenTaiKhoan().matches(tk)&&tkx.getMatKhau().matches(mk)) {
                        xetTk = true;
                        break;
                    }
                }

                if(mk.matches(mkk)){
                    xetMk=false;
                }
                else {
                    xetMk=true;
                }



                if (tk.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mk.isEmpty() || mkk.isEmpty()) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (xetTk == true) {
                            if (xetMk == true) {
                                tkDao.doiMk(tkmkMoi);
                                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();


                                finish();


                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không được trùng với mật khẩu mới!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Tên tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });


        btNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCTk.setText("");
                txtCpass.setText("");
                txtNewPass.setText("");
            }
        });
    }
    private void init(){
        txtCTk = findViewById(R.id.edtCUser);
        txtCpass = findViewById(R.id.edtCPass);
        txtNewPass = findViewById(R.id.edtNewPass);
        btChangePass  =findViewById(R.id.btnChange);
        btNhapLai = findViewById(R.id.btnRelay);
    }
}
