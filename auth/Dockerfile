# 使用极简基础镜像（推荐）
FROM debian:bookworm-slim

# 或者使用更小的 distroless（推荐生产环境）
# FROM gcr.io/distroless/base-debian12

LABEL maintainer="little3201@gmail.com"

# 创建工作目录
WORKDIR /app

# 复制 Native 可执行文件
COPY target/auth ./auth   # ← 根据你的实际文件名修改

# 暴露端口（改成你实际使用的端口）
EXPOSE 8761

# 运行 Native 可执行文件
ENTRYPOINT ["./auth"]

# 可选：添加常用参数
# CMD ["--spring.profiles.active=prod"]