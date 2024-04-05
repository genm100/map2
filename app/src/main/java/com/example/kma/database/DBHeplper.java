package com.example.kma.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHeplper extends SQLiteOpenHelper {
    public DBHeplper(@Nullable Context context) {
        super(context, "CTUET.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //tạo table lớp
        String sql = " CREATE TABLE LOP(maLop TEXT PRIMARY KEY, tenLop TEXT)";
        db.execSQL(sql);
        sql = " INSERT INTO LOP VALUES ('L03','CNTP')";
        db.execSQL(sql);
        sql = " INSERT INTO LOP VALUES ('L02','KTPM')";
        db.execSQL(sql);
        sql = " INSERT INTO LOP VALUES ('L05','CNTT')";
        db.execSQL(sql);
        //tao table calendar event
        sql = "CREATE TABLE EventCalendar(Date TEXT, Event TEXT)";
        db.execSQL(sql);
        //tạo table sinh viên
        sql = " CREATE TABLE SINHVIEN(maSv TEXT PRIMARY KEY, tenSV TEXT ," + " email TEXT ,hinh TEXT, maLop TEXT REFERENCES LOP(maLop))";
        db.execSQL(sql);
        sql = " INSERT INTO SINHVIEN VALUES ('2000403','Nguyễn Hoàng Trương','nhtruong2000403@student.ctuet.edu.vn','avatamacdinh','L05')";
        db.execSQL(sql);
        sql = " INSERT INTO SINHVIEN VALUES ('2000606','Dương Thị Tiểu Yến','dttyen2000606@student.ctuet.edu.vn','avatamacdinh','L05')";
        db.execSQL(sql);
        sql = " INSERT INTO SINHVIEN VALUES ('2000589','Nguyễn Ngọc Triều','nntrieu2000589@student.ctuet.edu.vn','avatamacdinh','L03')";
        db.execSQL(sql);
        sql = " INSERT INTO SINHVIEN VALUES ('2000222','Trần Quang Trường','tqtruong2000222@student.ctuet.edu.vn','avatamacdinh','L02')";
        db.execSQL(sql);
        sql = " INSERT INTO SINHVIEN VALUES ('2000064','Lê Tấn Lộc','ltloc2000064@student.ctuet.edu.vn','avatamacdinh','L02')";
        db.execSQL(sql);
        //tạo table taikhoan
        sql = "CREATE TABLE taiKhoan(tenTaiKhoan text primary key, matKhau text)";
        db.execSQL(sql);
        //tai khoan mac dinh admin
        sql = "INSERT INTO taiKhoan VALUES('admin','admin')";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
