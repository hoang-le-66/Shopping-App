package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.model.UserModel;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass, repass,mobile,username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }
    //Hàm này cắt chuỗi ở hai đầu
    private void dangKi() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_user = username.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email",Toast.LENGTH_LONG).show();
            //phải có show thì mới toast được
        }else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass",Toast.LENGTH_LONG).show();
            //phải có show thì mới toast được
        }else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập repass",Toast.LENGTH_LONG).show();
            //phải có show thì mới toast được
        }else if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại",Toast.LENGTH_LONG).show();
            //phải có show thì mới toast được
        }else if(TextUtils.isEmpty(str_user)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập username",Toast.LENGTH_LONG).show();
            //phải có show thì mới toast được
        }else {
            // Check mật khẩu trùng nhau
            if (str_pass.equals(str_repass)){
                //post data
                compositeDisposable.add(apiBanHang.dangki(str_email,str_pass,str_repass,str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                if(userModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), "Thành công",Toast.LENGTH_LONG).show();
                                    Utils.user_current.setEmail(str_email);
                                    Utils.user_current.setPass(str_pass);
                                    // đăng ký xong chuyển về màn hình đăng nhập
                                    Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                    startActivity(intent);
                                    finish();
                                    }else {
                                    Toast.makeText(getApplicationContext(), userModel.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_LONG).show();
                                }

                        )
                );
            }else {
                Toast.makeText(getApplicationContext(), "Mật khẩu chưa khớp",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btndangki);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}