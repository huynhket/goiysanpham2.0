import os
import glob

folder = r"d:\GoiYSanPhamApp\src\view"
for file_path in glob.glob(os.path.join(folder, "*.java")):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    if "Segoe UI Emoji" in content:
        content = content.replace("Segoe UI Emoji", "Segoe UI")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Fixed {file_path}")
