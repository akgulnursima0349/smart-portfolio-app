# Smart Portfolio Backend

Spring Boot tabanlı çok katmanlı backend projesi.

## Teknolojiler

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **Spring Data Redis** (Token Blacklisting)
- **MinIO** (Object Storage)
- **MySQL**
- **Redis**
- **Maven**
- **Lombok**
- **ModelMapper**
- **Swagger/OpenAPI** (API Dokümantasyonu)

## Proje Yapısı

```
com.smartportfolio
├── controller      # REST API endpoint'leri
├── service        # İş mantığı katmanı
├── repository     # Veritabanı erişim katmanı
├── dto            # Data Transfer Objects
├── model          # Entity sınıfları
├── exception      # Özel exception sınıfları ve global handler
├── config         # Konfigürasyon sınıfları
└── security       # JWT ve Security konfigürasyonları
```

## Özellikler

### Kimlik Doğrulama ve Yetkilendirme
- JWT tabanlı kimlik doğrulama sistemi
- Rol ve izin tabanlı yetkilendirme (RBAC)
- Kullanıcı kaydı ve girişi
- Token tabanlı oturum yönetimi
- **Token Refresh** - Access token yenileme mekanizması
- **Logout** - Redis ile token blacklisting
- Refresh token Redis'te güvenli saklama

### Kullanıcı Yönetimi
- Kullanıcı CRUD işlemleri
- Rol atama
- Kullanıcı durumu yönetimi (aktif/pasif)

### Portföy Modülleri
- **Projects** - Proje yönetimi (CRUD, arama, öne çıkan)
- **Blogs** - Blog yönetimi (CRUD, arama, analytics)
- **Skills** - Yetenek yönetimi (seviye bazlı filtreleme)
- **Languages** - Dil yönetimi (varsayılan dil)

### Dosya Yönetimi
- **MinIO Entegrasyonu** - Resim dosyaları için object storage
- **File Upload** - Resim yükleme (JPEG, PNG, GIF, WebP)
- **File Management** - Dosya silme, URL oluşturma
- **File Validation** - Dosya tipi ve boyut kontrolü

### Güvenlik
- Şifre şifreleme (BCrypt)
- JWT token validasyonu
- Token blacklisting (çıkış yapılan token'lar)
- Method seviyesinde yetkilendirme (@PreAuthorize)
- Global exception handling
- Özel exception mesajları

## Kurulum

### Gereksinimler
- Java 17 veya üzeri
- Docker & Docker Compose
- Maven 3.6 veya üzeri

### Hızlı Başlangıç

```bash
# 1. Projeyi klonla
git clone <repository-url>
cd smart-portfolio-platform-backend

# 2. Servisleri başlat (MySQL, Redis, MinIO)
docker-compose up -d

# 3. Uygulamayı çalıştır
mvn clean install
mvn spring-boot:run
```

**Servisler:**
- **MinIO**: http://localhost:9000 (Console: http://localhost:9001)
- **Redis**: localhost:6379
- **MySQL**: localhost:3307
- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html

### Veritabanı

Veritabanı otomatik olarak oluşturulur. `application.yml`'de `createDatabaseIfNotExist=true` parametresi ile MySQL otomatik olarak `smart_portfolio_db` veritabanını oluşturur.

## API Dokümantasyonu

### Swagger UI
API'leri test etmek ve keşfetmek için Swagger UI kullanabilirsiniz:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

### Swagger Özellikleri
- ✅ **Interactive API Testing** - Doğrudan tarayıcıdan API testi
- ✅ **JWT Authentication** - Token ile kimlik doğrulama
- ✅ **Request/Response Examples** - Örnek veriler
- ✅ **Parameter Validation** - Otomatik doğrulama
- ✅ **Error Responses** - Hata yanıtları
- ✅ **File Upload Support** - Dosya yükleme desteği

### Swagger Kullanımı
1. **Swagger UI'ya gidin**: http://localhost:8080/api/swagger-ui.html
2. **Authorize butonuna tıklayın**
3. **Token'ı girin**: `Bearer your_jwt_token`
4. **API'leri test edin**

## API Endpoints

### Authentication

#### Kayıt Ol
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "kullanici_adi",
  "email": "email@example.com",
  "password": "Guvenli123!",
  "firstName": "Ad",
  "lastName": "Soyad",
  "phoneNumber": "+905551234567"
}
```

#### Giriş Yap
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "kullanici_adi",
  "password": "Guvenli123!"
}
```

#### Mevcut Kullanıcı Bilgisi
```http
GET /api/auth/me
Authorization: Bearer {token}
```

#### Çıkış Yap (Logout)
```http
POST /api/auth/logout
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "refreshToken": "refresh_token_buraya"
}
```

#### Token Yenileme (Refresh)
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "refresh_token_buraya"
}
```

### Portföy Modülleri

#### Projects (`/api/projects`)
```http
GET    /projects              # Tüm projeler
GET    /projects/paged        # Sayfalı projeler
GET    /projects/featured     # Öne çıkan projeler
GET    /projects/search       # Proje arama
GET    /projects/{id}         # Proje detayı
POST   /projects              # Proje oluştur (ADMIN)
PUT    /projects/{id}         # Proje güncelle (ADMIN)
DELETE /projects/{id}         # Proje sil (ADMIN)
```

#### Blogs (`/api/blogs`)
```http
GET    /blogs                 # Tüm yayınlanmış bloglar
GET    /blogs/paged           # Sayfalı bloglar
GET    /blogs/search          # Blog arama
GET    /blogs/top             # En çok okunan bloglar
GET    /blogs/{id}            # Blog detayı (görüntülenme +1)
POST   /blogs                 # Blog oluştur (ADMIN)
PUT    /blogs/{id}            # Blog güncelle (ADMIN)
DELETE /blogs/{id}            # Blog sil (ADMIN)
```

#### Skills (`/api/skills`)
```http
GET    /skills                # Tüm yetenekler
GET    /skills/level/{level}  # Seviye bazlı yetenekler
GET    /skills/search         # Yetenek arama
GET    /skills/{id}           # Yetenek detayı
POST   /skills                # Yetenek oluştur (ADMIN)
PUT    /skills/{id}           # Yetenek güncelle (ADMIN)
DELETE /skills/{id}           # Yetenek sil (ADMIN)
```

#### Languages (`/api/languages`)
```http
GET    /languages             # Tüm diller
GET    /languages/search      # Dil arama
GET    /languages/{id}        # Dil detayı
GET    /languages/code/{code}  # Kod bazlı dil
GET    /languages/default     # Varsayılan dil
POST   /languages             # Dil oluştur (ADMIN)
PUT    /languages/{id}        # Dil güncelle (ADMIN)
DELETE /languages/{id}        # Dil sil (ADMIN)
```

### Dosya Yönetimi

#### File Upload (`/api/files`)
```http
POST   /files/upload          # Dosya yükle (ADMIN)
GET    /files/{fileName}      # Dosya bilgisi
GET    /files/{fileName}/url  # Dosya URL'si
GET    /files/{fileName}/exists # Dosya varlığı
DELETE /files/{fileName}      # Dosya sil (ADMIN)
```

#### Dosya Yükleme Örneği
```bash
POST http://localhost:8080/api/files/upload
Authorization: Bearer {admin_token}
Content-Type: multipart/form-data

file: [resim_dosyası]
```

**Response:**
```json
{
  "fileName": "20250122_143022_a1b2c3d4_image.jpg",
  "fileUrl": "http://localhost:9000/portfolio-images/20250122_143022_a1b2c3d4_image.jpg",
  "originalName": "project-image.jpg",
  "contentType": "image/jpeg",
  "fileSize": 245760,
  "uploadedAt": "2025-01-22T14:30:22"
}
```

## Veritabanı Modeli

### User (Kullanıcı)
- id, username, email, password, firstName, lastName
- phoneNumber, isActive, isEmailVerified, lastLogin
- roles (many-to-many), createdAt, updatedAt

### Project (Proje)
- id, title, description, imageUrl, githubUrl, demoUrl
- isActive, isFeatured, createdAt, updatedAt

### Blog (Blog)
- id, title, content, summary, imageUrl
- isPublished, viewCount, createdAt, updatedAt

### Skill (Yetenek)
- id, name, level (enum), iconUrl
- isActive, sortOrder, createdAt, updatedAt

### Language (Dil)
- id, code, name, isActive, isDefault
- createdAt, updatedAt

## Güvenlik Notları

1. `application.yml` dosyasındaki JWT secret key'i değiştirin
2. Üretim ortamında güçlü şifreler kullanın
3. HTTPS kullanın
4. Rate limiting ekleyin
5. CORS ayarlarını yapılandırın
6. MinIO access key ve secret key'i değiştirin

## Şifre Kuralları

Kayıt sırasında şifre en az:
- 8 karakter
- 1 büyük harf
- 1 küçük harf
- 1 rakam
- 1 özel karakter (@$!%*?&)

içermelidir.

## Dosya Yükleme Kuralları

- **Desteklenen formatlar**: JPEG, PNG, GIF, WebP
- **Maksimum boyut**: 5MB
- **Bucket**: portfolio-images
- **Dosya adlandırma**: timestamp_uuid_image.extension

## Geliştirme

### Test
```bash
mvn test
```

### Package
```bash
mvn package
```

### Docker ile Tam Stack
```bash
# Tüm servisleri başlat
docker-compose up -d

# Uygulamayı çalıştır
mvn spring-boot:run
```

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.