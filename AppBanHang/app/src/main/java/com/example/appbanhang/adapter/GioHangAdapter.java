package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ImageClickListener;
import com.example.appbanhang.R;
import com.example.appbanhang.model.EnventBus.TinhTongEvent;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }
    // Bắt sự kiện cho giao diện
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        //Ép số lượng về chuỗi
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+"");
        //Lấy hình ảnh
        Glide.with(context).load(gioHang.getHinhanh()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        //--> ở đây giasp được khai bao kiểu long nên không thể ép kiểu Double được
        //holder.item_giohang_gia.setText("Giá: "+decimalFormat.format(Double.parseDouble(gioHang.getGiasp()))+"Đ");
        holder.item_giohang_gia.setText(decimalFormat.format((gioHang.getGiasp())));
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.mangmuahang.add(gioHang);
                    // Mỗi lần thực hiện thì tính lại giá
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }else {
                    for (int i= 0; i< Utils.manggiohang.size();i++){
                        if(Utils.mangmuahang.get(i).getIdsp()== gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });

        holder.setListener(new ImageClickListener() {
            //Bài 21: chức năng cộng trừ
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if(giatri == 1){
                    if(gioHangList.get(pos).getSoluong()>1){
                        int soluongmoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                        //Bài 22: xóa sản phẩm trong giỏ nếu đang có số lượng 1
                    } else if (gioHangList.get(pos).getSoluong()==1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }

                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoluong()<11){
                        int soluongmoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
//                holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
//                long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
//                holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
//                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image,imgtru,imgcong;
        TextView  item_giohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_giasp2;
        ImageClickListener listener;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);
            //Bài 21: event click
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
            //Check box giỏ hàng
            checkBox = itemView.findViewById(R.id.item_giohang_check);
        }

        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view==imgtru){
                listener.onImageClick(view,getAdapterPosition(),1);
            } else if (view==imgcong) {
                listener.onImageClick(view,getAdapterPosition(),2);
            }
        }
    }
}
