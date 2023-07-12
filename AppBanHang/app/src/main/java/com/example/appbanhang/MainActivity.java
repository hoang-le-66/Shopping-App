package com.example.appbanhang;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.activity.DangNhapActivity;
import com.example.appbanhang.activity.DienThoaiActivity;
import com.example.appbanhang.activity.GioHangActivity;
import com.example.appbanhang.activity.LaptopActivity;
import com.example.appbanhang.activity.SearchActivity;
import com.example.appbanhang.activity.XemDonActivity;
import com.example.appbanhang.adapter.LoaiSpAdapter;
import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.LoaiSp;
import com.example.appbanhang.model.SanPhamMoi;
//import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolBarManHinhChinh;
    ViewFlipper viewFlipper;
    RecyclerView recycleViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        Actionbar();
        ActionViewFlipper();
        if (isConnected(this)){
            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }
        else {
            Toast.makeText(getApplicationContext(),"khong co internet",Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                switch (i){
                    //Trang chủ là số 0
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        //Truyền loại 1
                        startActivity(dienthoai);
                        break;
                    case 2:
                       // Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        //Nãy quên truyền vào số 2
                        Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("loai",2);
                        //Truyền loại 2
                        startActivity(laptop);
                        break;
                    case 4:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 5:
                        //xoa key user
                        Paper.book().delete("user");
                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;

                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    sanPhamMoiModel -> {
                        if (sanPhamMoiModel.isSuccess()){
                            mangSpMoi = sanPhamMoiModel.getResult();
                            spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                            recycleViewManHinhChinh.setAdapter(spAdapter);
                        }
                    },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi duoc"+throwable.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    //    private void getLoaiSanPham() {
//        compositeDisposable.add(apiBanHang.getLoaiSp()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        loaiSpModel -> {
//                            if(loaiSpModel.isSuccess()){
//                                Toast.makeText(getApplicationContext(), loaiSpModel.getResult().get(0).getTensanpham(),Toast.LENGTH_LONG).show();
//                            }
//                        }
//                ));
//
//    }
private void getLoaiSanPham() {
    compositeDisposable.add(apiBanHang.getLoaiSp()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    loaiSpModel -> {
                        if (loaiSpModel.isSuccess()) {
                            //Toast.makeText(getApplicationContext(), loaiSpModel.getResult().get(0).getTensanpham(), Toast.LENGTH_LONG).show();
                            mangloaisp = loaiSpModel.getResult();
                            //Thêm mục đăng xuất :))) lú
                            //mangloaisp.add(new LoaiSp("Đăng xuất",""));
                            loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                            listViewManHinhChinh.setAdapter(loaiSpAdapter);

                        } else {
                            // Xử lý lỗi khi không thành công
                            Toast.makeText(getApplicationContext(),
                                    "Không thể lấy danh sách loại sản phẩm", Toast.LENGTH_LONG).show();
                        }
                    },
                    throwable -> {
                        // Xử lý lỗi khi có ngoại lệ xảy ra
                        if (throwable instanceof SocketTimeoutException) {
                            Toast.makeText(getApplicationContext(),
                                    "Lỗi kết nối timeout", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Lỗi mạng: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            ));
}


    private void ActionViewFlipper() {
        List <String> mangquangcao = new ArrayList<>();
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i=0; i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }
    private void Actionbar() {
        setSupportActionBar(toolBarManHinhChinh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarManHinhChinh.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);//Thiết lập navbar
        toolBarManHinhChinh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        imgsearch = findViewById(R.id.imgsearch);
        toolBarManHinhChinh = (Toolbar) findViewById(R.id.toolBarManHinhChinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recycleViewManHinhChinh = (RecyclerView) findViewById(R.id.recycleViewManHinhChinh);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recycleViewManHinhChinh.setLayoutManager(layoutManager);
        recycleViewManHinhChinh.setHasFixedSize(true);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        listViewManHinhChinh = (ListView) findViewById(R.id.listViewManHinhChinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //Bài 21
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        //khoi tao list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        //Bài 21
        if(Utils.manggiohang==null){
            Utils.manggiohang = new ArrayList<>();
        }else {
            int totalItem = 0;
            for (int i=0;i<Utils.manggiohang.size();i++)
            {
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });
        //khoi tao adapter
//        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
//        listViewManHinhChinh.setAdapter(loaiSpAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i=0;i<Utils.manggiohang.size();i++)
        {
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    //Kiem tra internet
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // Can them quyen
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}