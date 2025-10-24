# 🚀 Smart Portfolio - AI-Powered Portfolio Management System

Modern, full-stack portfolio yönetim sistemi. React + Spring Boot + AI entegrasyonu ile güçlü bir platform.

## ✨ Özellikler

### 🎯 Ana Özellikler
- **👤 Kullanıcı Yönetimi** - Kayıt, giriş, profil yönetimi
- **📁 Proje Yönetimi** - Proje ekleme, düzenleme, silme
- **📝 Blog Sistemi** - Blog yazıları oluşturma ve yönetme
- **🛠️ Yetenek Yönetimi** - Teknik yetenekler ve seviyeler
- **🌍 Dil Desteği** - Çoklu dil desteği
- **📊 Dashboard** - İstatistikler ve genel bakış

### 🤖 AI Özellikleri
- **🧠 AI Proje Açıklaması** - Groq API ile otomatik proje açıklamaları
- **⚡ Hızlı Yanıt** - Llama 3.1 modeli ile süper hızlı AI
- **🆓 Ücretsiz** - Aylık 14,400 ücretsiz istek
- **🌍 Türkçe Destek** - Türkçe AI yanıtları

## 🛠️ Teknolojiler

### Backend
- **Spring Boot 3.1.5** - Java framework
- **Spring Security** - Güvenlik ve kimlik doğrulama
- **JWT** - Token tabanlı kimlik doğrulama
- **MySQL** - Veritabanı
- **Redis** - Cache ve session yönetimi
- **MinIO** - Dosya depolama
- **Docker** - Containerization

### Frontend
- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool
- **Tailwind CSS** - Styling
- **React Query** - Data fetching
- **React Router** - Navigation

### AI Integration
- **Groq API** - AI servis sağlayıcısı
- **Llama 3.1 8B** - AI model
- **REST API** - AI endpoint'leri

## 🚀 Kurulum

### Gereksinimler
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.6+

### 1. Repository'yi Klonlayın
```bash
git clone https://github.com/akgulnursima0349/portfolio-app-fl.git
cd portfolio-app-fl
```

### 2. Backend Kurulumu
```bash
cd smart-portfolio-backend

# Docker servislerini başlat
docker-compose up -d

# API key'i ayarla
export GROQ_API_KEY=your-groq-api-key-here

# Backend'i başlat
mvn spring-boot:run
```

### 3. Frontend Kurulumu
```bash
cd smart-portfolio-frontend

# Bağımlılıkları yükle
npm install

# Development server'ı başlat
npm run dev
```

### 4. Erişim
- **Frontend:** http://localhost:5173
- **Backend:** http://localhost:8080
- **API Docs:** http://localhost:8080/swagger-ui.html

## 🔑 API Key Kurulumu

### Groq API Key
1. [Groq Console](https://console.groq.com) hesabı oluşturun
2. API key alın
3. Environment variable olarak ayarlayın:
```bash
export GROQ_API_KEY=your-actual-groq-api-key
```

## 📁 Proje Yapısı

```
portfolio-app-fl/
├── smart-portfolio-backend/          # Spring Boot Backend
│   ├── src/main/java/com/smartportfolio/
│   │   ├── controller/               # REST Controllers
│   │   ├── service/                 # Business Logic
│   │   ├── repository/              # Data Access
│   │   ├── model/                   # Entity Models
│   │   ├── dto/                     # Data Transfer Objects
│   │   ├── security/                # Security Configuration
│   │   └── config/                  # Configuration
│   ├── src/main/resources/
│   │   └── application.yml          # Application Configuration
│   └── docker-compose.yml           # Docker Services
├── smart-portfolio-frontend/         # React Frontend
│   ├── src/
│   │   ├── pages/                   # React Pages
│   │   ├── components/              # Reusable Components
│   │   ├── api/                     # API Services
│   │   ├── context/                 # React Context
│   │   └── layouts/                 # Layout Components
│   └── package.json
└── README.md
```

## 🎯 Kullanım

### Admin Paneli
- **Dashboard** - Genel istatistikler
- **Projeler** - Proje yönetimi
- **Bloglar** - Blog yazıları
- **Yetenekler** - Teknik yetenekler
- **Kullanıcılar** - Kullanıcı yönetimi
- **🧠 AI Tools** - AI proje açıklaması

### AI Özelliği
1. Admin paneline giriş yapın
2. "🧠 AI Tools" sekmesine tıklayın
3. Proje açıklaması için prompt yazın
4. AI otomatik açıklama üretsin

## 🔒 Güvenlik

- **JWT Authentication** - Token tabanlı kimlik doğrulama
- **Role-based Access** - Rol tabanlı erişim kontrolü
- **CORS Configuration** - Cross-origin güvenlik
- **Input Validation** - Giriş doğrulama
- **API Key Security** - Environment variables

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - Kullanıcı kaydı
- `POST /api/auth/login` - Giriş
- `POST /api/auth/refresh` - Token yenileme

### Projects
- `GET /api/projects` - Projeleri listele
- `POST /api/projects` - Proje oluştur
- `PUT /api/projects/{id}` - Proje güncelle
- `DELETE /api/projects/{id}` - Proje sil

### AI
- `POST /api/ai/generate` - AI proje açıklaması

## 🐳 Docker

```bash
# Tüm servisleri başlat
docker-compose up -d

# Servisleri durdur
docker-compose down
```

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.


---

⭐ **Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!**
