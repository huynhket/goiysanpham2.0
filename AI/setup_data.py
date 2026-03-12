from pymongo import MongoClient
from datetime import datetime
import sys

# Đảm bảo in được tiếng Việt trên console Windows
if sys.stdout.encoding != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except AttributeError:
        # For Python < 3.7
        import io
        sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def setup_data():
    client = MongoClient("mongodb://localhost:27017/")
    db = client["GOI_Y_SAN_PHAM"]

    print("--- Dọn dẹp data cũ ---")
    db["users"].drop()
    db["products"].drop()
    db["product_variants"].drop()
    db["behaviors"].drop()
    db["orders"].drop()
    db["carts"].drop()

    print("--- Thêm Users ---")
    db["users"].insert_many([
        {"ma_nguoi_dung": 1, "ten_dang_nhap": "user1", "mat_khau": "123", "ho_ten": "Nguyễn Văn A", "vai_tro": "USER"},
        {"ma_nguoi_dung": 2, "ten_dang_nhap": "admin", "mat_khau": "123", "ho_ten": "Quản Trị Viên", "vai_tro": "ADMIN"}
    ])

    print("--- Thêm Sản Phẩm ---")
    products = [
        {"ma_san_pham": 1, "ten_san_pham": "iPhone 15 Pro", "gia": 25000000.0, "so_luong": 50, "ma_danh_muc": 1, "mo_ta": "Siêu phẩm Apple 2023", "hinh_anh": "images/iphone15.png"},
        {"ma_san_pham": 2, "ten_san_pham": "Samsung Galaxy S23", "gia": 18000000.0, "so_luong": 30, "ma_danh_muc": 1, "mo_ta": "Flagship Samsung", "hinh_anh": "images/s23.png"},
        {"ma_san_pham": 3, "ten_san_pham": "MacBook Air M2", "gia": 28000000.0, "so_luong": 20, "ma_danh_muc": 2, "mo_ta": "Mỏng nhẹ mạnh mẽ", "hinh_anh": "images/macbook.png"}
    ]
    db["products"].insert_many(products)

    print("--- Thêm Biến Thể (Dung Lượng) ---")
    variants = [
        {"ma_dl": 1, "ma_san_pham": 1, "dung_luong": "128GB", "phu_phi": 0.0, "so_luong": 20},
        {"ma_dl": 2, "ma_san_pham": 1, "dung_luong": "256GB", "phu_phi": 3000000.0, "so_luong": 15},
        {"ma_dl": 3, "ma_san_pham": 2, "dung_luong": "128GB", "phu_phi": 0.0, "so_luong": 10},
        {"ma_dl": 4, "ma_san_pham": 3, "dung_luong": "8GB/256GB", "phu_phi": 0.0, "so_luong": 10},
    ]
    db["product_variants"].insert_many(variants)

    print("--- Thêm Hành Vi Mẫu (để test AI) ---")
    db["behaviors"].insert_many([
        {"ma_nguoi_dung": 1, "ma_san_pham": 1, "hanh_dong": "view", "thoi_gian": datetime.now()},
        {"ma_nguoi_dung": 1, "ma_san_pham": 3, "hanh_dong": "buy", "thoi_gian": datetime.now()}
    ])

    print("--- HOÀN TẤT ---")
    print("Database: GOI_Y_SAN_PHAM đã sẵn sàng!")

if __name__ == "__main__":
    setup_data()
