package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ItemClickListener;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.ChiTietActivity;
import com.example.appbanhang.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;
// Code cũ: public class DienThoaiAdapter extends RecyclerView.Adapter<DienThoaiAdapter.MyViewHolder>
// Code thêm:
public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi>  array;
    //Code được thêm từ vid 13
    public static final int VIEW_TYPE_DATA = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai, parent, false);
//        return new MyViewHolder(view);
//    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent,false);
            return new LoadingViewHolder(view);
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        SanPhamMoi sanPham = array.get(position);
//        holder.tensp.setText(sanPham.getTensp());
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        holder.giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPham.getGiasp()))+"Đ");
//        holder.mota.setText(sanPham.getMota());
//        Glide.with(context).load(sanPham.getHinhanh()).into(holder.hinhanh);
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder; //Ép kiểu
            SanPhamMoi sanPham = array.get(position);
            myViewHolder.tensp.setText(sanPham.getTensp().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPham.getGiasp()))+"Đ");
            myViewHolder.mota.setText(sanPham.getMota());
            //myViewHolder.idsp.setText(sanPham.getId() + "");
            Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.hinhanh);
            //sau khi tạo setItemClickListener
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
//                        Intent intent = new Intent(context, ChiTietActivity.class);
//
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", sanPham);
                        intent.putExtra("key", sanPham.getId()); // Gắn dữ liệu vào Intent (ví dụ: id của sản phẩm)
                        //"key" là khóa được chọn để đại diện cho giá trị ID của sản phẩm
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
    //Nếu vị trí là null thì loading, không thì xem Data
    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        // Tạo constructor
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar); // Gán id

        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp,giasp,mota, idsp;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemdt_ten);
            giasp = itemView.findViewById(R.id.itemdt_gia);
            mota = itemView.findViewById(R.id.itemdt_mota);
            //idsp = itemView.findViewById(R.id.itemdt_idsp);
            hinhanh = itemView.findViewById(R.id.itemdt_image);
            //Tạo một sự kiện cho biến
            itemView.setOnClickListener(this);
            //Sau đó phải implement phương thức View.OnClickListener
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        //Tạo constructor
        @Override
        public void onClick(View view) {

            itemClickListener.onClick(view, getAdapterPosition(),false);
        }
    }
}
