@echo off
echo --- BAT DAU SUA LOI DU LIEU ---
cd /d %~dp0
echo Dang kiem tra Python...
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [LOI] Khong tim thay Python! Hay cai dat Python hoac dung lenh 'py' thay the.
    pause
    exit /b
)

echo Dang cai dat thu vien pymongo...
python -m pip install pymongo >nul 2>&1

echo Dang nap du lieu vao MongoDB...
python AI/setup_data.py
 
echo Dang huan luyen mo hinh AI...
cd AI && python train.py && cd ..

echo --- HOAN TAT ---
echo Hay quay lai Eclipse, Refresh (F5) du an va thu dang nhap lai.
pause
