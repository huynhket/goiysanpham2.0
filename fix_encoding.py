import os
import re

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
    
    # Replace Font
    content = content.replace('Segoe UI Emoji', 'Segoe UI')
    
    # Escape Vietnamese strings in double quotes
    def string_replacer(match):
        s = match.group(1)
        return f'"{escape_vietnamese(s)}"'
    
    # This regex finds text inside double quotes, ignoring escaped quotes
    content = re.sub(r'"((?:[^"\\]|\\.)*)"', string_replacer, content)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

# Fix DangNhap.java as a test
target_file = r'd:\GoiYSanPhamApp\src\view\DangNhap.java'
if os.path.exists(target_file):
    fix_file(target_file)
    print(f"Fixed {target_file}")
else:
    print(f"File not found: {target_file}")
