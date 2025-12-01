Antifraud Service

Модуль **Antifraud** — часть учебного проекта по моделированию банковской системы `p_ss_bank_416`.  
Я отвечал за **полный цикл разработки антифрод-сервиса**:  
от проектирования REST API и бизнес-логики до настройки мониторинга, логирования и интеграции с Prometheus / Grafana / Loki.

Antifraud Service отвечает за:
- регистрацию и обработку **подозрительных переводов**:
  - по счетам (`SuspiciousAccountTransfers`)
  - по банковским картам (`SuspiciousCardTransfer`)
  - по телефонам (`SuspiciousPhoneTransfers`)
- хранение и отображение **истории аудита** операций (`Audit`)
- предоставление REST API для других сервисов банковской системы
- сбор **метрик** для Prometheus
- централизованное **логирование** в Loki с визуализацией в Grafana

Сервис построен как отдельное Spring Boot-приложение и может развёртываться независимо от других модулей системы.

## Архитектура и структура модулей

Пакеты модуля `antifraud`:

- `aspect` — сквозная функциональность (аудит операций, `AuditAspect`)
- `config` — конфигурация (Swagger, таймеры/метрики)
- `controller` — REST-контроллеры
- `dto` — DTO для входящих/исходящих данных
- `entity` — JPA-сущности (`Audit`, `SuspiciousAccountTransfers`, `SuspiciousCardTransfer`, `SuspiciousPhoneTransfers`)
- `exception.handler` — глобальная обработка ошибок
- `mapper` — маппинги сущностей в DTO и обратно
- `repository` — репозитории Spring Data JPA
- `service` / `service.impl` — бизнес-логика
- `util` — вспомогательные утилиты
- `resources/db.changelog` — скрипты Liquibase для схемы `anti_fraud`

Отдельный модуль логирования и мониторинга:
- `prometheus.yml` — конфигурация Prometheus (job `anti-fraud`)
- `promtail-config.yaml` — конфигурация Promtail для отправки логов в Loki
- `loki-config.yaml` — конфигурация Loki
- `docker-compose.yml` — инфраструктура: PostgreSQL, Prometheus, Grafana, Loki, Promtail

##  Используемые технологии

| Технология             | Назначение                                     |
|------------------------|-----------------------------------------------|
| **Java 17**            | язык разработки                               |
| **Spring Boot 2/3**    | основной фреймворк приложения                 |
| **Spring Web**         | REST API                                      |
| **Spring Data JPA**    | доступ к БД                                   |
| **PostgreSQL**         | основная СУБД                                 |
| **Liquibase**          | управление миграциями и схемой `anti_fraud`  |
| **Hibernate Validator**| валидация DTO                                 |
| **Lombok**             | сокращение boilerplate-кода                   |
| **Spring Boot Actuator** | технические эндпоинты и метрики           |
| **Micrometer + Prometheus** | сбор метрик                         |
| **Grafana**            | дашборды по метрикам и логам                  |
| **Loki + Promtail**    | централизованный сбор и хранение логов        |
| **Swagger / OpenAPI**  | документация REST API                         |
| **JUnit 5, Spring Test** | модульные и интеграционные тесты           |

---

##  Запуск проекта

### 1. Предварительные требования

- JDK 17 (ARM64 для Apple Silicon / x86_64 для Intel)
- Maven 3.8+
- Docker и Docker Compose

### 2. Запуск инфраструктуры (БД + мониторинг)

В каталоге модуля `antifraud`:

```bash
cd antifraud
docker-compose up -d
Поднимаются сервисы:
bank-db (PostgreSQL)
prometheus
grafana
loki
promtail
далее необходимо выполнить команду для создания схемы CREATE SCHEMA anti_fraud

### 2. Запуск Antifraud приложения
основное приложение: http://localhost:8086/api/V1/anti-fraud
management / actuator: http://localhost:8186/actuator
Swagger UI доступен по адресу: http://localhost:8086/api/V1/anti-fraud/swagger-ui/index.html

Основные группы эндпоинтов:
Подозрительные переводы по счетам
GET /suspicious-account-transfers
GET /suspicious-account-transfers/{id}
POST /suspicious-account-transfers
PUT /suspicious-account-transfers/{id}
DELETE /suspicious-account-transfers/{id}

Подозрительные переводы по картам
аналогично ...card-transfers

Подозрительные переводы по телефонам
аналогично ...phone-transfers

Аудит операций
Аудит реализован через аспект AuditAspect:
перехватываются методы сервисов createNew* и update*
сериализуется состояние сущности в JSON (через Jackson ObjectMapper)
создаётся запись в таблице anti_fraud.audit
заполняются поля:
entityType
operationType (CREATE / UPDATE)
createdBy / modifiedBy
createdAt / modifiedAt
entityJson / newEntityJson
Таким образом, можно восстановить историю изменений каждой подозрительной операции.


Пример тела запроса для создания подозрительного перевода по счёту:
{
  "accountTransferId": 10,
  "isBlocked": true,
  "isSuspicious": true,
  "blockedReason": "Лимит превышен",
  "suspiciousReason": "Аномальная активность клиента"
}

Actuator поднят на отдельном порту 8186:
список эндпоинтов:
http://localhost:8186/actuator
состояние приложения:
http://localhost:8186/actuator/health
метрики для Prometheus:
http://localhost:8186/actuator/prometheus


Prometheus поднимается через docker-compose и доступен по адресу: http://localhost:9090

Тестирование
Модуль покрыт тестами:
JUnit 5
Spring Boot Test / MockMvc
Тестируются:
контроллеры подозрительных переводов
валидация входящих DTO
обработка ошибок и коды ответов (422, 404 и т.д.)
сервисная логика

