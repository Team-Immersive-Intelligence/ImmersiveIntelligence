@echo off
echo.
set apppath="optipng.exe"
for /R "src\main\resources\assets\immersiveintelligence\textures" %%f in (*.png) do %apppath% %%f -zc9 -zm9 -zs3 -f0-5 -nc -o7 -strip "all"