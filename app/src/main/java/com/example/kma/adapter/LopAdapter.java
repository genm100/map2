package com.example.kma.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kma.dao.LopDao;
import com.example.kma.model.Lop;
import com.example.kma.R;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class LopAdapter extends BaseAdapter implements Filterable {
    Activity context;
    int layout;
    ArrayList<Lop> dslop;
    ArrayList<Lop> dsSortLop;
    private Filter lopFilter;
    LopDao lopDao;



    public LopAdapter(Activity context, int layout, ArrayList<Lop> dslop) {
        this.context = context;
        this.layout = layout;
        this.dslop = dslop;
        this.dsSortLop = dslop;
    }


    @Override
    public int getCount() {
        return dslop.size();

    }

    @Override
    public Object getItem(int position) {
        return dslop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataSet(ArrayList<Lop> dsl) {
        this.dslop = dsl;
        notifyDataSetChanged();
    }

    public void resetData() {
        this.dslop = dsSortLop;
    }

    @Override
    public Filter getFilter() {
        if (lopFilter == null) {
            lopFilter = new CustomFilter();
        }
        return lopFilter;
    }

    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = dsSortLop;
                results.count = dsSortLop.size();
            } else {
                ArrayList<Lop> dslopmoi = new ArrayList<Lop>();
                for (Lop lop : dslop) {
                    if (lop.getTenLop().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        dslopmoi.add(lop);
                    }
                }
                results.values = dslopmoi;
                results.count = dslop.size();
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                dslop = (ArrayList<Lop>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    private class ViewHolder {
        TextView txtMalop, txtTenLop;
        CircleImageView  imageViewhinh;
        ImageView ivDelete, ivEdit;
        LinearLayout linearLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.linearLayout=convertView.findViewById(R.id.linearLayoutSuaLop);
            holder.txtMalop = convertView.findViewById(R.id.txtMaLophoc);
            holder.txtTenLop = convertView.findViewById(R.id.txtTenLophoc);
            holder.ivDelete = convertView.findViewById(R.id.imageViewdeletelop);
            holder.imageViewhinh = convertView.findViewById(R.id.imageView);
            holder.ivEdit = convertView.findViewById(R.id.imageeditlop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Lop lop = dslop.get(position);


        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lopDao = new LopDao(context);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View view = inflater.inflate(R.layout.edit_lop, null);
                final EditText edtmaLop = view.findViewById(R.id.edteditMaLop);
                final EditText edtTenLop = view.findViewById(R.id.edteditTenLop);
                Button btnSua = view.findViewById(R.id.btnCapNhat);
                Button btnLai = view.findViewById(R.id.btnEdNhapLai);


                //Đổ dữ liệu
                edtmaLop.setText(lop.getMaLop());
                edtTenLop.setText(lop.getTenLop());


                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String maLop = edtmaLop.getText().toString();
                            String tenLop = edtTenLop.getText().toString();

                            Lop lop1 = new Lop(maLop, tenLop);

                            //Update vào sql
                            if (lopDao.update(lop1)) {
                                Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                dslop.clear();
                                dslop.addAll(lopDao.getAll());
                                notifyDataSetChanged();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn xóa lớp "+ lop.getMaLop()+" không ? \nCảnh báo nếu xóa lớp "+ lop.getMaLop()+  " thì hệ thống sẽ xóa toàn bộ sinh viên trong lớp  "+ lop.getMaLop());
              //  final Lop lop = dslop.get(position);
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lopDao = new LopDao(context);
                        if (lopDao.delete(lop.getMaLop())) {
                            Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            dslop.clear();
                            dslop.addAll(lopDao.getAll());
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


        holder.txtMalop.setText(lop.getMaLop());
        holder.txtTenLop.setText(lop.getTenLop());
        holder.imageViewhinh.setImageResource(R.drawable.ctuet);
        return convertView;
    }
}
