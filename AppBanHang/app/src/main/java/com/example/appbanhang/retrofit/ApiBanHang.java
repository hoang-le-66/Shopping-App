package com.example.appbanhang.retrofit;

import com.example.appbanhang.model.DonHangModel;
import com.example.appbanhang.model.LoaiSpModel;
import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    //Hiển thị
    @GET("getloaisp.php")
   Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    //Có sự kiện
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
        @Field("page") int page,
        @Field("loai") int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> creatOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") String soluong,
            @Field("chitiet") String chitiet
    );

   @POST("timkiem.php")
   @FormUrlEncoded
   Observable<SanPhamMoiModel> search(
           @Field("search") String search
   );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") String id
    );
}
