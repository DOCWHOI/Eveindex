#!/bin/bash

echo "启动风险监控系统后端服务..."
echo

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装JDK 17或更高版本"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven环境，请先安装Maven"
    exit 1
fi

echo "正在编译项目..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "编译失败，请检查代码"
    exit 1
fi

echo "正在启动应用..."
mvn spring-boot:run -q 