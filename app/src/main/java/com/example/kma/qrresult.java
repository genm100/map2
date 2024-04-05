package com.example.kma;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Build;
import android.widget.Toast;

import com.example.kma.dao.SinhVienDao;
import com.example.kma.model.SinhVien;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrresult extends AppCompatActivity implements ZXingScannerView.ResultHandler, ActivityCompat.OnRequestPermissionsResultCallback {

    private LocationManager locationManager;
    private Location currentLocation;
    SinhVienDao svDao;
    ArrayList<SinhVien> listSV = new ArrayList<>();
    ZXingScannerView scannerView;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        // Yêu cầu quyền truy cập vào camera và vị trí
        requestCameraPermission();
        requestLocationPermission();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, new MyLocationListener());
        }
    }

    // Phương thức yêu cầu quyền truy cập vào camera
    private void requestCameraPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // Quyền được cấp, khởi động camera để quét mã QR
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // Hiển thị thông báo hoặc thực hiện xử lý phù hợp khi người dùng từ chối quyền truy cập vào camera
                        Toast.makeText(qrresult.this, "Permission Denied: Camera Access", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    // Phương thức yêu cầu quyền truy cập vào vị trí
    private static final double TARGET_LATITUDE = 10.0440341;
    private static final double TARGET_LONGITUDE = 105.7703402;

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu quyền từ người dùng
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu quyền đã được cấp, thực hiện các hành động liên quan đến vị trí ở đây
            getCurrentLocation();
        }
    }

    private String getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (currentLocation != null) {
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                // Kiểm tra nếu vị trí hiện tại không khớp với vị trí mong muốn
                if (latitude == TARGET_LATITUDE && longitude == TARGET_LONGITUDE) {
                    // Trả về vị trí hiện tại nếu khớp
                    return "Latitude:1 " + latitude + ", Longitude:2 " + longitude;
                } else {
                    // Trả về vị trí mặc định nếu không khớp
                    return "Trường ĐH KT-CN Cần Thơ";
                }
            }
        }
        // Trả về vị trí mặc định nếu không có quyền hoặc không có vị trí
        return "Trường ĐH KT-CN Cần Thơ";
    }



    @Override
    public void handleResult(Result rawResult) {
        boolean isSvExist = false;
        svDao = new SinhVienDao(qrresult.this);
        listSV = svDao.getALL();
        String scancode = rawResult.getText();

        // Lấy số ID của thiết bị
        String deviceId = Build.DEVICE;

        for (int i = 0; i < listSV.size(); i++) {
            SinhVien svx = listSV.get(i);
            if (svx.getMaSv().equals(scancode)) {
                isSvExist = true;
                // Lấy thông tin vị trí hiện tại
                String currentLocation = getCurrentLocation();
                // Hiển thị thông tin sinh viên, số ID của thiết bị và vị trí
                QRScannerActivity.scantext.setText("Họ và tên: " + svx.getTenSv() + "\nMã SV: " + svx.getMaSv() + "\nLớp: " + svx.getMaLop() + "\nĐã điểm danh lúc: " + getDateTime() + "\nVị trí hiện tại: " + currentLocation + "\nID thiết bị: " + deviceId);
                // Lưu thông tin vào file
                String data = "\n----------------------------------------------------------------------------------------" +
                        "\nHọ tên: " + svx.getTenSv() + " | Mã SV: " + svx.getMaSv() + " | Lớp: " + svx.getMaLop() + "\nThời gian: " + getDateTime() + "\nVị trí hiện tại: " + currentLocation + "\nID thiết bị: " + deviceId;
                saveData(data);
                Toast.makeText(qrresult.this, "Điểm danh thành công!", Toast.LENGTH_SHORT).show();
            }
        }

        if (!isSvExist) {
            QRScannerActivity.scantext.setText("Chưa có dữ liệu!");
            Toast.makeText(qrresult.this, "Điểm danh thất bại!", Toast.LENGTH_SHORT).show();
        }

        onBackPressed();
        getCurrentLocation();
    }






    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Cập nhật vị trí mới
            currentLocation = location;
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Xử lý khi trạng thái vị trí thay đổi
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Xử lý khi người dùng bật cung cấp vị trí
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Xử lý khi người dùng tắt cung cấp vị trí
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tạm dừng camera khi Activity bị pause
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Khởi động lại camera khi Activity được resume
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    String simpleFileName = "diemDanh.txt";
    private void saveData(String data) {
        try {
            // Open Stream to write file.
            FileOutputStream out = this.openFileOutput(simpleFileName, MODE_APPEND);
            // Ghi dữ liệu.
            out.write(data.getBytes());
            out.close();
//            Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
