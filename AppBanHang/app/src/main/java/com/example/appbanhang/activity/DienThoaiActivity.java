package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
//Nhớ import đúng thư viện

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.DienThoaiAdapter;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page=1;
    int loai;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getByteExtra("loai", (byte) 1);
        AnhXa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    //Chú ý truyền nhầm lớp LinearLayoutManager thay vì tham số linearLayoutManager
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() ==sanPhamMoiList.size()-1){
                        isLoading = true;
                        loadMore();

                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                sanPhamMoiList.add(null);
                adapterDt.notifyItemInserted(sanPhamMoiList.size()-1);
            }
        });
        //Thực hiện tải dữ liệu trang tiếp theo và cập nhật giao diện
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Remover null
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                adapterDt.notifyItemRemoved(sanPhamMoiList.size());
                page = page+1;
                getData(page);
                adapterDt.notifyDataSetChanged();
                isLoading = false;

            }
        },2000);
    }
    //Hàm này sẽ được dùng đi dùng lại
    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            //Thành công là đang còn dữ liệu
                            if(sanPhamMoiModel.isSuccess()){
                                if(adapterDt == null){
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    adapterDt = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                                    recyclerView.setAdapter(adapterDt);
                                }else {
                                    int vitri = sanPhamMoiList.size()-1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for (int i=0; i<soluongadd; i++){
                                        sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    adapterDt.notifyItemRangeInserted(vitri,soluongadd);
                                }
//                                sanPhamMoiList = sanPhamMoiModel.getResult();
//                                adapterDt = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
//                                recyclerView.setAdapter(adapterDt);
                            }else {
                                //Đây là khi hết dữ liệu
                                Toast.makeText(getApplicationContext(), "Đã hiển thị hết sản phẩm, đợi cập nhật"
                                        ,Toast.LENGTH_LONG).show();
                                isLoading=true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi server",Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Bắt sự kiện cho nút Back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recycleview_dt);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();

    }
    // Không hiểu lắm nhưng hàm này lúc nào cũng có
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}