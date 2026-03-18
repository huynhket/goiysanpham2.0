import os
import traceback

def convert_to_escaped(filename):
    try:
        with open(filename, 'r', encoding='utf-8-sig') as f:
            content = f.read()
    except Exception as e:
        print(f"Failed to read {filename}: {e}")
        return
        
    res = []
    for c in content:
        if ord(c) > 127:
            res.append(f"\\u{ord(c):04x}")
        else:
            res.append(c)
            
    try:
        with open(filename, 'w', encoding='utf-8') as f:
            f.write(''.join(res))
        print(f"Updated {filename}")
    except Exception as e:
        print(f"Failed to write {filename}: {e}")

for root, _, files in os.walk('d:/GoiYSanPhamApp/src'):
    for file in files:
        if file.endswith('.java'):
            convert_to_escaped(os.path.join(root, file))
print("Done")
