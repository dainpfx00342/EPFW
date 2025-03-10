#Sử dụng JDK 21
FROM openjdk:21-jdk
# Thư mục làm việc
WORKDIR /app

# Copy file jar vào thư mục làm việc
COPY target/EPFW-0.0.1-SNAPSHOT.jar app.jar

# Chạy ứng dụng
CMD ["java", "-jar", "app.jar"]