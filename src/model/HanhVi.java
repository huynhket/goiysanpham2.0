package model;

import java.time.LocalDateTime;

public class HanhVi {

    private int maHanhVi;
    private int maNguoiDung;
    private int maSanPham;
    private String hanhDong;
    private LocalDateTime thoiGian;

    public int getMaHanhVi() {
        return maHanhVi;
    }

    public void setMaHanhVi(int maHanhVi) {
        this.maHanhVi = maHanhVi;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getHanhDong() {
        return hanhDong;
    }

    public void setHanhDong(String hanhDong) {
        this.hanhDong = hanhDong;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
}
