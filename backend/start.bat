@echo off
echo 启动风险监控系统后端服务...
echo.

REM 检查Java环境
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Java环境，请先安装JDK 17或更高版本
    pause
    exit /b 1
)

REM 检查Maven环境
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Maven环境，请先安装Maven
    pause
    exit /b 1
)

echo 正在编译项目...
call mvn clean compile -q
if errorlevel 1 (
    echo 编译失败，请检查代码
    pause
    exit /b 1
)

echo 正在启动应用...
call mvn spring-boot:run -q

pause 