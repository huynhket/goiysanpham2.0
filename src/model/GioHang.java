package model;

public class GioHang {

    private int maSanPham;
    private int maDL;
    private String tenSanPham;
    private String dungLuong;
    private double gia;
    private int soLuong;

    // ========= GETTER =========
    public int getMaSanPham() { return maSanPham; }
    public int getMaDL() { return maDL; }
    public String getTenSanPham() {
        return tenSanPham;
    }

    public String getDungLuong() {
        return dungLuong;
    }

    public double getGia() {
        return gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getThanhTien() {
        return gia * soLuong;
    }

    // ========= SETTER =========
    public void setMaSanPham(int maSanPham) { this.maSanPham = maSanPham; }
    public void setMaDL(int maDL) { this.maDL = maDL; }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public void setDungLuong(String dungLuong) {
        this.dungLuong = dungLuong;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
