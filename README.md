**💳 SmartPay Platform - Backend**
SmartPay, modern bir mikroservis mimarisi üzerine kurulu, yüksek performanslı ve ölçeklenebilir bir ödeme altyapısı platformudur. Bu proje, platformun tüm backend servislerini içermektedir.

Sistem, docker-compose kullanılarak tek bir komutla ayağa kaldırılmak üzere tasarlanmıştır ve servisler arası iletişimi sağlamak için RabbitMQ üzerinden olay-tabanlı (event-driven) bir yaklaşım benimser.

**🚀 Mimari ve Servisler**
Proje, her biri belirli bir iş mantığından sorumlu olan aşağıdaki modüllerden (mikroservislerden) oluşur:

**1. GATEWAY (Ağ Geçidi)**
api-gateway (Spring Cloud Gateway)

Tüm dış istekler için tek giriş noktasıdır.

Gelen istekleri ilgili mikroservise yönlendirir.

JWT tabanlı kimlik doğrulama (JwtAuthenticationFilter) ve Redis destekli hız limiti (Rate Limiting) gibi ara katman görevlerini üstlenir.

**2. AUTH (Kimlik Doğrulama)**
auth-service (Spring Boot)

Kullanıcı (Merchant) kaydı (/register) ve girişi (/login) işlemlerini yönetir.

Başarılı giriş sonrası accessToken ve refreshToken üretir.

Verileri PostgreSQL veritabanında saklar.

**3. PAYMENT (Ödeme)**
payment-service (Spring Boot)

Ödeme işlemlerini (/api/payment/pay) alır ve işler.

Her ödeme isteğini, fraud-detection-service'e göndererek risk analizinden geçirir (FraudDetectionClient).

İşlem sonucunu (başarılı/başarısız) payment.exchange adlı RabbitMQ Fanout Exchange'ine yayınlar (PaymentService).

**4. FRAUD (Dolandırıcılık Tespiti)**
fraud-detection-service (Python / Flask)

payment-service'ten gelen işlemleri alır.

Önceden eğitilmiş bir Scikit-learn makine öğrenimi modeli (fraud_detector.py) ve kural bazlı kontroller (örn: yüksek tutar) kullanarak bir risk skoru hesaplar.

İşlemin onaylanması (APPROVE) veya engellenmesi (BLOCK) için tavsiyede bulunur.

**5. ANALYTICS (Analiz)**
analytics-service (Spring Boot)

RabbitMQ'daki payment.exchange'i dinler (PaymentEventListener).

Gelen tüm başarılı ve engellenen işlemleri kendi PostgreSQL veritabanına kaydeder (AnalyticsService).

Frontend paneli için raporlama ve dashboard verilerini sağlar (/api/analytics/report).

**6. NOTIFICATION (Bildirim)**
notification-service (Spring Boot)

RabbitMQ'daki payment.exchange'i dinler (PaymentEventListener).

Başarılı veya başarısız olan her işlem için (simüle edilmiş) bir e-posta bildirimi gönderir (EmailService).

**7. COMMON (Ortak Kütüphane)**
common-lib (Java Kütüphanesi)

Tüm servisler tarafından paylaşılan ortak kodları içerir.

JwtProvider, ortak DTO'lar (BaseResponse) ve özel istisna (exception) sınıfları (SmartPayException) burada tanımlanmıştır.

**🛠️ Kullanılan Teknolojiler**
Backend: Java 21, Spring Boot 3.3.3, Spring Cloud Gateway, Spring Security (JWT)

Veritabanı: PostgreSQL (İlişkisel Veri), Redis (Hız Limitleme/Cache)

Mesajlaşma: RabbitMQ (Olay-tabanlı mimari için)

AI/ML: Python 3.11, Flask, Scikit-learn (Dolandırıcılık tespiti için)

Konteynerizasyon: Docker & Docker Compose

Derleme: Maven

**🏁 Başlarken**
Gereksinimler
Docker ve Docker Compose

Java 21 JDK

Apache Maven

Çalıştırma
Proje, docker-compose ile tek komutta çalışacak şekilde yapılandırılmıştır.

Projenin ana dizinindeyken tüm modülleri derleyin ve Docker imajlarını oluşturun:

Bash

# Önbelleği temizleyerek tüm servislerin sıfırdan derlenmesini sağlar
docker-compose build --no-cache
Tüm servisleri (PostgreSQL, RabbitMQ, Redis ve uygulama servisleri) başlatın:

Bash

docker-compose up
(Veya -d bayrağı ile arka planda çalıştırabilirsiniz: docker-compose up -d)

Tüm servisler ayağa kalktığında, platform kullanıma hazırdır. API Gateway http://localhost:8080 portundan yayın yapacaktır.
