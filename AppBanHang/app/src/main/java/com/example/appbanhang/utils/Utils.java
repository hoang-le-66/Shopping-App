package com.example.appbanhang.utils;

import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.1.56:84/banhang/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();

    public  static User user_current = new User() ;
    //public  static User user_current;
}
