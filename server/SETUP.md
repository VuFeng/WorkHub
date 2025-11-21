# Setup Hướng Dẫn

## Cấu hình Database

### Bước 1: Tạo file application.properties

Copy file template và điền thông tin database của bạn:

```bash
cd server/src/main/resources
cp application.properties.example application.properties
```

### Bước 2: Cập nhật thông tin database

Mở file `application.properties` và thay đổi:

```properties
spring.datasource.password=YOUR_PASSWORD_HERE
```

Thành password MySQL thực tế của bạn.

### Bước 3: Chạy ứng dụng

```bash
cd server
mvn spring-boot:run
```

## ⚠️ Lưu ý Bảo mật

- **KHÔNG BAO GIỜ** commit file `application.properties` lên Git
- File này đã được thêm vào `.gitignore`
- Chỉ commit file `application.properties.example` (template)
- Mỗi developer cần tự tạo file `application.properties` của riêng mình

## Kiểm tra trước khi commit

Trước khi commit, kiểm tra:

```bash
git status
```

Đảm bảo `application.properties` **KHÔNG** xuất hiện trong danh sách files to commit.
