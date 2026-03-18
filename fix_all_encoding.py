import os
import re
import glob

def escape_vietnamese(text):
    def replace(match):
        char = match.group(0)
        return f"\\u{ord(char):04x}"
    
    # Vietnamese character ranges (approximate)
    # Including characters from Latin-1 Supplement and Latin Extended-A/B and Vietnamese specific ranges
    pattern = re.compile(r'[\u0080-\uFFFF]') 
    return pattern.sub(replace, text)

def fix_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except UnicodeDecodeError:
        with open(filepath, 'r', encoding='cp1252') as f:
            content = f.read()
            
    original_content = content
    
    # Replace Font
    content = content.replace('Segoe UI Emoji', 'Segoe UI')
    
    # Escape Vietnamese strings in double quotes
    def string_replacer(match):
        s = match.group(1)
        return f'"{escape_vietnamese(s)}"'
    
    # This regex finds text inside double quotes, ignoring escaped quotes
    content = re.sub(r'"((?:[^"\\]|\\.)*)"', string_replacer, content)
    
    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

# Fix all java files in src/view
target_dir = r'd:\GoiYSanPhamApp\src\view'
java_files = glob.glob(os.path.join(target_dir, '*.java'))
for filepath in java_files:
    if "DangNhap" in filepath or "TrangChuFrame" in filepath:
        # Avoid double-escaping already fixed files (though the regex won't double escape \ so maybe it's fine, but to be safe)
        continue
    if fix_file(filepath):
        print(f"Fixed {filepath}")
    else:
        print(f"Skipped {filepath}")

