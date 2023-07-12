package com.example.appbanhang.model;

public class LoaiSp {
    int id;
    String tensanpham;
    String hinhanh;

    public LoaiSp(String tensanpham, String hinhanh) {
        this.tensanpham = tensanpham;
        this.hinhanh = hinhanh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public static class Item {
        int idsp;
        String tensp;

        public int getIdsp() {
            return idsp;
        }

        public void setIdsp(int idsp) {
            this.idsp = idsp;
        }

        public String getTensp() {
            return tensp;
        }

        public void setTensp(String tensp) {
            this.tensp = tensp;
        }
    }
}
