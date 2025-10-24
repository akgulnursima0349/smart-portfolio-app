# Smart Portfolio Frontend

Bu proje, Smart Portfolio platformunun React frontend kısmıdır. Backend Spring Boot + MySQL + JWT ile çalışır.

## 🚀 Özellikler

- **Modern React Stack**: React 18 + TypeScript + Vite
- **Styling**: TailwindCSS ile modern ve responsive tasarım
- **State Management**: React Query + Context API
- **Authentication**: JWT tabanlı kimlik doğrulama
- **Internationalization**: i18next ile çok dillilik (TR/EN)
- **Form Management**: Formik + Yup ile form doğrulama
- **UI Components**: Lucide React ikonları
- **Notifications**: React Hot Toast ile bildirimler

## 📁 Proje Yapısı

```
src/
├── api/              # Backend API servisleri
│   ├── auth.service.ts
│   ├── projects.service.ts
│   ├── blogs.service.ts
│   ├── skills.service.ts
│   ├── users.service.ts
│   ├── languages.service.ts
│   ├── files.service.ts
│   └── axios.ts
├── components/       # UI bileşenleri
│   ├── Button.tsx
│   ├── Card.tsx
│   ├── Input.tsx
│   ├── Textarea.tsx
│   ├── Select.tsx
│   ├── Modal.tsx
│   ├── Navbar.tsx
│   ├── ProtectedRoute.tsx
│   ├── LoadingSpinner.tsx
│   └── ConfirmDialog.tsx
├── context/          # React Context
│   └── AuthContext.tsx
├── hooks/            # Custom hooks
│   ├── useAuth.ts
│   ├── useFetch.ts
│   └── useCRUD.ts
├── i18n/             # Çok dillilik
│   ├── config.ts
│   └── locales/
│       ├── en.json
│       └── tr.json
├── layouts/           # Layout bileşenleri
│   └── AdminLayout.tsx
├── pages/            # Sayfalar
│   ├── Login.tsx
│   ├── Register.tsx
│   ├── Dashboard.tsx
│   ├── ProjectsPage.tsx
│   ├── BlogsPage.tsx
│   ├── SkillsPage.tsx
│   ├── UsersPage.tsx
│   ├── Portfolio.tsx
│   └── NotFound.tsx
├── utils/             # Yardımcı fonksiyonlar
│   ├── formatDate.ts
│   └── tokenHelpers.ts
├── App.tsx
└── main.tsx
```

## 🛠️ Kurulum

1. **Bağımlılıkları yükleyin:**
   ```bash
   npm install
   ```

2. **Geliştirme sunucusunu başlatın:**
   ```bash
   npm run dev
   ```

3. **Projeyi build edin:**
   ```bash
   npm run build
   ```

## 🔧 Backend API Entegrasyonu

Frontend, aşağıdaki backend endpoint'leri ile çalışır:

- **Base URL**: `http://localhost:8080/api`
- **Authentication**: JWT token tabanlı
- **File Upload**: Multipart form data ile dosya yükleme
- **CRUD Operations**: Tüm entity'ler için tam CRUD desteği

### API Endpoints

- **Auth**: `/auth/register`, `/auth/login`, `/auth/me`, `/auth/logout`, `/auth/refresh`
- **Users**: `/users`, `/users/{id}`, `/users/{id}/roles`
- **Projects**: `/projects`, `/projects/{id}`, `/projects/featured`, `/projects/active`
- **Blogs**: `/blogs`, `/blogs/{id}`, `/blogs/search`, `/blogs/{id}/views`
- **Skills**: `/skills`, `/skills/{id}`, `/skills/level/{level}`
- **Languages**: `/languages`, `/languages/{id}`, `/languages/code/{code}`
- **Files**: `/files/upload`, `/files/{fileName}`, `/files/{fileName}/url`

## 🌍 Çok Dillilik

Proje Türkçe ve İngilizce dil desteği sunar:
- Dil değiştirme navbar'da bulunur
- Tüm metinler i18next ile yönetilir
- Dil tercihi localStorage'da saklanır

## 🔐 Authentication

- JWT token tabanlı kimlik doğrulama
- Otomatik token yenileme
- Protected routes ile güvenli sayfa erişimi
- Logout işlemi ile token temizleme

## 📱 Responsive Design

- Mobile-first yaklaşım
- TailwindCSS ile responsive tasarım
- Modern ve kullanıcı dostu arayüz

## 🚀 Deployment

1. **Build:**
   ```bash
   npm run build
   ```

2. **Preview:**
   ```bash
   npm run preview
   ```

## 📝 Scripts

- `npm run dev` - Geliştirme sunucusu
- `npm run build` - Production build
- `npm run preview` - Build preview
- `npm run lint` - ESLint kontrolü
- `npm run typecheck` - TypeScript kontrolü

## 🔗 Backend Gereksinimleri

Bu frontend, aşağıdaki backend API'leri ile çalışır:
- Spring Boot backend
- MySQL veritabanı
- JWT authentication
- File upload endpoint'i
- CORS yapılandırması

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.