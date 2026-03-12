package model;

import java.util.List;

public class SanPham {

    private int maSanPham;
    private String tenSanPham;
    private double gia;
    private int soLuong;      // tồn kho
    private int maDanhMuc;    // danh mục

    // ⭐ MỚI
    private String moTa;      // mô tả
    private String hinhAnh;   // đường dẫn ảnh chính
    private List<String> danhSachHinhAnh; // danh sách các ảnh phụ

    // ================= GET SET =================

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    // ================= ⭐ MỚI =================

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public List<String> getDanhSachHinhAnh() {
        return danhSachHinhAnh;
    }

    public void setDanhSachHinhAnh(List<String> danhSachHinhAnh) {
        this.danhSachHinhAnh = danhSachHinhAnh;
    }

    // ================= HIỂN THỊ =================
    @Override
    public String toString() {
        return tenSanPham +
                " | Giá: " + gia + " đ" +
                " | SL: " + soLuong;
    }
 // ================= CONSTRUCTOR =================

 // rỗng (bắt buộc cho JavaBean)
 public SanPham() {
 }

 // dùng cho DAO AI (4 field)
 public SanPham(int maSanPham, String tenSanPham, double gia, String hinhAnh) {
     this.maSanPham = maSanPham;
     this.tenSanPham = tenSanPham;
     this.gia = gia;
     this.hinhAnh = hinhAnh;
 }

 // full (nếu cần dùng sau này)
 public SanPham(int maSanPham, String tenSanPham, double gia,
                int soLuong, int maDanhMuc, String moTa, String hinhAnh, List<String> danhSachHinhAnh) {
     this.maSanPham = maSanPham;
     this.tenSanPham = tenSanPham;
     this.gia = gia;
     this.soLuong = soLuong;
     this.maDanhMuc = maDanhMuc;
     this.moTa = moTa;
     this.hinhAnh = hinhAnh;
     this.danhSachHinhAnh = danhSachHinhAnh;
 }

}

