from pymongo import MongoClient

client = MongoClient("mongodb://localhost:27017/")
db = client["GOI_Y_SAN_PHAM"]
products = db["products"]

# Print all products
all_products = list(products.find({}, {"ma_san_pham": 1, "ten_san_pham": 1, "hinh_anh": 1}))
print(f"Total products: {len(all_products)}")
for p in all_products:
    print(f"  ID={p.get('ma_san_pham')}, Name={p.get('ten_san_pham')}, Image={p.get('hinh_anh')}")

# Fix paths: /src/images/xxx -> images/xxx
updated = 0
for p in all_products:
    img = p.get("hinh_anh", "")
    original = img
    if img.startswith("/src/images/"):
        new_img = "images/" + img.split("/src/images/")[1]
        products.update_one({"_id": p["_id"]}, {"$set": {"hinh_anh": new_img}})
        updated += 1
        print(f"  Fixed: {original} -> {new_img}")
    elif img.startswith("src/images/"):
        new_img = "images/" + img.split("src/images/")[1]
        products.update_one({"_id": p["_id"]}, {"$set": {"hinh_anh": new_img}})
        updated += 1
        print(f"  Fixed: {original} -> {new_img}")
    elif img.startswith("images/"):
        print(f"  OK: {img}")
    else:
        print(f"  Unknown path format: {img}")

print(f"\nDone. Updated {updated} product image paths.")
