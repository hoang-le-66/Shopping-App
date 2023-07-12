package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.MainActivity;
import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        initControl();
        countItem();
    }

//
    //Bài 35 Check box
    private void countItem() {
        totalItem=0;
        for (int i=0;i<Utils.mangmuahang.size();i++)
        {
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolBar);
        txttongtien = findViewById(R.id.txttongtien);
        txtsodt = findViewById(R.id.txtsodienthoai);
        txtemail = findViewById(R.id.txtemail);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);
    }


    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Đặt sự kiện cho nút back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsodt.setText(Utils.user_current.getMobile());

        // Bài 29: Do dữ liệu truyền vào là kiểu long nên phải có "", double thì không cần
        txttongtien.setText(getIntent().getLongExtra("tongtien",0)+"");

        //Kiểm tra xem khách hàng có nhập địa chỉ không
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }else {
                    //Post data giỏ hàng, chuyển giỏ hàng thành chuỗi json
                    //Ép kiểu String.valueOf(tongtien)
                    String str_email =Utils.user_current.getEmail();
                    String str_sdt =Utils.user_current.getMobile();
                    int id=Utils.user_current.getId();
                    //Log.d("test",new Gson().toJson(Utils.manggiohang));
                    //Bài 35
                    //Kiem tra
                    Log.d("test",new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.creatOder(str_email,str_sdt
                           // ,String.valueOf(tongtien),id,str_diachi, String.valueOf(totalItem),new Gson().toJson(Utils.manggiohang))
                            //Day moi la thuc hien
                            ,String.valueOf(tongtien),id,str_diachi, String.valueOf(totalItem),new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_SHORT).show();
                                        // Mua xong thì xóa
                                        Utils.mangmuahang.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}