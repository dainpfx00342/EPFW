#Sử dụng JDK 21
FROM openjdk:11
# Thư mục làm việc
WORKDIR /app

# Copy file jar vào thư mục làm việc
COPY . .
#Cap quyen cho file jar
RUN  chmod +x ./mvnw
# Build ứng dụng, bỏ qua test để tăng tốc build
RUN ./mvnw package -DskipTests

# Chạy ứng dụng
CMD ["java", "-jar", "/target/*.jar"]