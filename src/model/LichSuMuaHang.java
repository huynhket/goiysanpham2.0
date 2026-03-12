package model;

public class LichSuMuaHang {

    private int maDonHang;
    private String tenSanPham;
    private int soLuong;
    private double gia;
    private String ngayMua;
    private String trangThai; // ⭐ Thêm trạng thái

    public LichSuMuaHang(int maDonHang, String tenSanPham, int soLuong, double gia, String ngayMua, String trangThai) {
        this.maDonHang = maDonHang;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.gia = gia;
        this.ngayMua = ngayMua;
        this.trangThai = trangThai;
    }

    public int getMaDonHang() { return maDonHang; }
    public String getTenSanPham() { return tenSanPham; }
    public int getSoLuong() { return soLuong; }
    public double getGia() { return gia; }
    public String getNgayMua() { return ngayMua; }
    public String getTrangThai() { return trangThai; }
}