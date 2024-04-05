package com.example.kma.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kma.dao.LopDao;
import com.example.kma.dao.SinhVienDao;
import com.example.kma.model.Lop;
import com.example.kma.model.SinhVien;
import com.example.kma.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinhVienAdapter extends BaseAdapter implements Filterable {
    Context context;

    ArrayList<SinhVien> ds;
    ArrayList<SinhVien> dsSortSinhVien;
    private Filter svFilter;

    LopDao lopDao;
    SinhVienDao sinhVienDao;
    ArrayList<Lop> dsmaLop = new ArrayList<>();
    Animation animation;
    public static final int REQUEST_CODE_GALLERY = 777;

    public SinhVienAdapter(Context context, ArrayList<SinhVien> ds) {
        this.context = context;
        this.ds = ds;
        sinhVienDao = new SinhVienDao(context);
        this.dsSortSinhVien = ds;
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return ds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataSet(ArrayList<SinhVien> dsl) {
        this.ds = dsl;
        notifyDataSetChanged();
    }

    public void resetData() {
        this.ds = dsSortSinhVien;
    }

    @Override
    public Filter getFilter() {
        if (svFilter == null) {
            svFilter = new CustomFilter();
        }
        return svFilter;
    }

    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = dsSortSinhVien;
                results.count = dsSortSinhVien.size();
            } else {
                ArrayList<SinhVien> dssv = new ArrayList<SinhVien>();
                for (SinhVien sv : ds) {
                    if (sv.getTenSv().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        dssv.add(sv);
                    }
                }
                results.values = dssv;
                results.count = ds.size();
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                ds = (ArrayList<SinhVien>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    private class ViewHolder extends AppCompatActivity {
        TextView txtTensv, txtMasv, txtemail, txtMalop;
        CircleImageView imageView_Avatalist;
        ImageView ivDelete, ivEdit;
        LinearLayout linearLayout;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.dong_sinhvien, null);
            holder = new ViewHolder();
            holder.txtMasv = convertView.findViewById(R.id.tvmasv);
            holder.txtTensv = convertView.findViewById(R.id.tvtensv);
            holder.txtemail = convertView.findViewById(R.id.tvemail);
            holder.txtMalop = convertView.findViewById(R.id.tvmalop);
            holder.linearLayout = convertView.findViewById(R.id.linearLayoutSuaLop);
            holder.ivDelete = convertView.findViewById(R.id.imageViewDelete);
            holder.imageView_Avatalist = convertView.findViewById(R.id.imageViewHinh);
            holder.ivEdit = convertView.findViewById(R.id.imageViewedit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SinhVien s = ds.get(position);
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lopDao = new LopDao(context);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View view = inflater.inflate(R.layout.sua_sinhvien, null);
                final EditText etMasv = view.findViewById(R.id.edMaSvedit);
                final EditText etTensv = view.findViewById(R.id.edTenSvedit);
                final EditText etemail = view.findViewById(R.id.edemail);
                final EditText ethinh = view.findViewById(R.id.edhinh);
                final CircleImageView imgAvata = view.findViewById(R.id.imageView_avata_edit);
                final Spinner spMalop = view.findViewById(R.id.spEdmalop);
                Button btnReview = view.findViewById(R.id.btnReviewSuaSV);
                final Button btnSua = view.findViewById(R.id.btnEdSua);
                final Button btnLai = view.findViewById(R.id.btnEdNhapLai);
                final Button btnHuy = view.findViewById(R.id.btnHuyeditSV);


                dsmaLop = lopDao.getAll();
                final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, dsmaLop);
                spMalop.setAdapter(adapter);
                //Đổ dữ liệu
                etMasv.setText(s.getMaSv());
                etTensv.setText(s.getTenSv());
                etemail.setText(s.getEmail());
                ethinh.setText(s.getHinh());
                int id_hinh = ((Activity) context).getResources().getIdentifier(s.getHinh(), "drawable", ((Activity) context).getPackageName());
                imgAvata.setImageResource(id_hinh);


                int giatri = -1;
                for (int i = 0; i < dsmaLop.size(); i++) {
                    if (dsmaLop.get(i).toString().equalsIgnoreCase(s.getMaLop())) {
                        giatri = i;
                        break;
                    }
                }
                spMalop.setSelection(giatri);

                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                //Sửa dữ liệu
                btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ethinh.getText().toString().equalsIgnoreCase("")){
                            imgAvata.setImageResource(R.drawable.avatamacdinh);
                        }else if(ethinh.getText().toString()!=""){
                            int id_hinh = ((Activity)context).getResources().getIdentifier(ethinh.getText().toString(), "drawable", ((Activity) context).getPackageName());
                            imgAvata.setImageResource(id_hinh);
                        }
                    }
                });
                btnSua.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            String maSv = etMasv.getText().toString();
                            String tenSv = etTensv.getText().toString();
                            String email = etemail.getText().toString();
                            Lop lop = (Lop) spMalop.getSelectedItem();
                            String maLop = lop.getMaLop();
                            String ViTri = "Some value";
                            String hinh = ethinh.getText().toString();
                            if (maSv.equals("")) {
                                Toast.makeText(context, "Bạn cần thêm mã sinh viên", Toast.LENGTH_LONG).show();
                            } else if (tenSv.equals("")) {
                                Toast.makeText(context, "Bạn cần thêm tên sinh viên", Toast.LENGTH_LONG).show();
                            } else if (email.equals("")) {
                                Toast.makeText(context, "Bạn cần thêm email sinh viên", Toast.LENGTH_LONG).show();
                            } else if (hinh.equals("")) {
                                ethinh.setText("avatamacdinh");
                            } else {
                                SinhVien sinhVien = new SinhVien(maSv, tenSv, email, hinh, maLop, ViTri);
                                //Update vào sql
                                if (sinhVienDao.update(sinhVien)) {
                                    Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_LONG).show();
                                    ds.clear();
                                    ds.addAll(sinhVienDao.getALL());
                                    notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_LONG).show();

                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                btnLai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etMasv.setText(s.getMaSv());
                        etTensv.setText(s.getTenSv());
                        etemail.setText(s.getEmail());
                        int giatri = -1;
                        for (int i = 0; i < dsmaLop.size(); i++) {
                            if (dsmaLop.get(i).toString().equalsIgnoreCase(s.getMaSv())) {
                                giatri = i;
                                break;
                            }
                        }
                        spMalop.setSelection(giatri);

                    }
                });

                alertDialog.show();
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn xóa sinh viên " + s.getTenSv() + " không ?");
                final SinhVien s = ds.get(position);
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sinhVienDao.delete(s)) {
                            Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            ds.clear();
                            ds.addAll(sinhVienDao.getALL());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog myAlert = builder.create();
                myAlert.show();
            }
        });

        holder.txtMasv.setText(s.getMaSv());
        holder.txtTensv.setText(s.getTenSv());
        holder.txtemail.setText(s.getEmail());
        holder.txtMalop.setText(s.getMaLop());
        int id_hinh = ((Activity) context).getResources().getIdentifier(ds.get(position).getHinh(), "drawable", ((Activity) context).getPackageName());
        if (ds.get(position).getHinh().length() + "" == null) {
            holder.imageView_Avatalist.setImageResource(R.drawable.avatamacdinh);
        } else {
            holder.imageView_Avatalist.setImageResource(id_hinh);

        }

        return convertView;

    }


}

