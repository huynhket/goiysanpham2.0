import sys
import joblib
import json
 
# ===== CHECK THAM SỐ =====
if len(sys.argv) < 2:
    print("[]")
    sys.exit(0)
 
user_id = int(sys.argv[1])
 
# Tải model từ file local (vì Java set working dir là AI)
model, matrix = joblib.load("model.pkl")
 
if model is None or matrix is None :
    print("[]")
    sys.exit(0)
 
try:
    k = min(3, len(matrix))
 
    distances, indices = model.kneighbors(
        matrix.loc[user_id].values.reshape(1, -1),
        n_neighbors=k
    )
 
    similar_users = matrix.index[indices.flatten()].tolist()
 
    recommend = []
 
    for u in similar_users:
        items = matrix.loc[u]
        for sp, score in items.items():
            if score > 0:
                recommend.append(int(sp))
 
    recommend = list(set(recommend))
    print(json.dumps(recommend))
except Exception as e:
    print("[]")
