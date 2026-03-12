import pandas as pd
from pymongo import MongoClient
import joblib
from sklearn.neighbors import NearestNeighbors
import os
import sys
 
# Đảm bảo in được tiếng Việt trên console Windows
if sys.stdout.encoding != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except AttributeError:
        import io
        sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
 
print("=== TRAIN START (MongoDB) ===")
 
# ================= CONNECT DB =================
try:
    client = MongoClient("mongodb://localhost:27017/")
    db = client["GOI_Y_SAN_PHAM"]
    collection = db["behaviors"]
 
    # Lấy dữ liệu từ MongoDB
    data = list(collection.find({}, {"_id": 0, "ma_nguoi_dung": 1, "ma_san_pham": 1, "hanh_dong": 1}))
    
    if not data:
        print("NO DATA in MongoDB -> skip training")
        joblib.dump((None, None), "model.pkl") 
        exit()
 
    df = pd.DataFrame(data)
 
    # ================= MAP SCORE =================
    score_map = {
        "view": 1,
        "buy": 3
    }
 
    df["score"] = df["hanh_dong"].map(score_map).fillna(0)
    df = df[df["score"] > 0]
 
    # ================= BUILD MATRIX =================
    matrix = df.pivot_table(
        index="ma_nguoi_dung",
        columns="ma_san_pham",
        values="score",
        aggfunc="sum",
        fill_value=0
    )
 
    if matrix.empty:
        print("Matrix empty -> skip")
        joblib.dump((None, None), "model.pkl")
        exit()
 
    # ================= TRAIN =================
    model = NearestNeighbors(metric="cosine")
    model.fit(matrix)
 
    # Lưu model
    joblib.dump((model, matrix), "model.pkl")
 
    print(f"Train xong: {matrix.shape}")
    print("=== TRAIN DONE ===")
 
except Exception as e:
    print(f"Lỗi training: {e}")
