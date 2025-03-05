#Sử dụng JDK 21
FROM openjdk:21-jdk-alpine
# Thư mục làm việc
WORKDIR /app

# Copy file jar vào thư mục làm việc
COPY target/EPFW-0.0.1-SNAPSHOT.jar app.jar
#Cap quyen cho file jar
RUN  chmod +x ./mvnw
# Build ứng dụng, bỏ qua test để tăng tốc build
RUN ./mvnw package -DskipTests

# Chạy ứng dụng
CMD ["java", "-jar", "/target/*.jar"]