/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.text.SimpleDateFormat;

/**
 *
 * @author Admin
 */
public class SanPhamModel {
    int maSp;
    String tenSP;
    int dongia;
    int soLuong;

    public SanPhamModel() {
    }

    public SanPhamModel(int maSp, String tenSP, int dongia, int soLuong) {
        this.maSp = maSp;
        this.tenSP = tenSP;
        this.dongia = dongia;
        this.soLuong = soLuong;
    }

    public int getMaSp() {
        return maSp;
    }

    public void setMaSp(int maSp) {
        this.maSp = maSp;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getDongia() {
        return dongia;
    }

    public void setDongia(int dongia) {
        this.dongia = dongia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public String[] toArray(){
        String[] stringArray = {String.format("%03d", maSp), tenSP, String.valueOf(soLuong), String.valueOf(dongia)};
        return stringArray;
    }
    
}
