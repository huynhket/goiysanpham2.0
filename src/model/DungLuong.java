package model;

public class DungLuong {

    private int maDL;
    private int maSanPham;
    private String dungLuong;   // ⭐ đúng tên DB
    private double phuPhi;
    private int soLuong;
    
    // ⭐ MỚI
    private String hinhAnh;     // Ảnh riêng của biến thể này

    public int getMaDL() { return maDL; }
    public void setMaDL(int maDL) { this.maDL = maDL; }

    public int getMaSanPham() { return maSanPham; }
    public void setMaSanPham(int maSanPham) { this.maSanPham = maSanPham; }

    public String getDungLuong() { return dungLuong; }
    public void setDungLuong(String dungLuong) { this.dungLuong = dungLuong; }

    public double getPhuPhi() { return phuPhi; }
    public void setPhuPhi(double phuPhi) { this.phuPhi = phuPhi; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    @Override
    public String toString() {
        return dungLuong; // combobox hiển thị
    }
}
