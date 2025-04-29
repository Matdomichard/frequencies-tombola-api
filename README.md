# 🎟️ Frequencies Tombola API

Open-source backend for managing raffles: lots, tickets, participants, dynamic configuration and HelloAsso integration.

---

## ✨ Features

- Manage lots (CRUD)
- Manage participants and tickets
- Lottery: weighted random draws
- Dynamic tombola configuration (number of tickets, 1-lot guarantee)
- HelloAsso OAuth2 authentication & forms retrieval
- REST API secured (admin-only access)
- Ready for production deployment (Docker, Vercel, Azure, Heroku)

## 🚀 Technologies

- Java 21
- Spring Boot 3.4
- Spring Data JPA (PostgreSQL)
- Maven
- Lombok
- OpenAPI / Swagger UI
- Docker compatible

## 🏗️ Setup & Run locally

### Prerequisites

- Docker installed (for PostgreSQL)
- Java 21 installed
- Maven installed

### Clone the repository

```bash
git clone https://github.com/your-username/frequencies-tombola-api.git
cd frequencies-tombola-api
```

### Setup your PostgreSQL database (local Docker)

```bash
docker run -d   --name tombola-db   -e POSTGRES_DB=tombola   -e POSTGRES_USER=tombola_user   -e POSTGRES_PASSWORD=secret   -p 5432:5432   postgres:15
```

### Configure your local `application.properties`

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tombola
spring.datasource.username=tombola_user
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update

# HelloAsso
helloasso.client-id=your_helloasso_client_id
helloasso.client-secret=your_helloasso_client_secret
helloasso.api-url=https://api.helloasso.com
helloasso.organization-slug=your_organization_slug
```

🔥 **Tip**: In production, use environment variables instead of hardcoding secrets.

### Run the app

```bash
mvn spring-boot:run
```

App will be available at [http://localhost:8080](http://localhost:8080)

---

## 📚 API Documentation

Once the application is running:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📂 Project structure

| Folder       | Description                          |
|--------------|--------------------------------------|
| `controller/`| All REST API controllers             |
| `service/`   | Business logic (interfaces + impls)  |
| `entity/`    | JPA entities                         |
| `repository/`| JPA repositories                     |
| `dto/`       | Data Transfer Objects                |
| `mapper/`    | Entity-DTO mappers                   |

---

## ⚙️ Environment Variables

| Variable                    | Description                                 | Required |
|----------------------------|---------------------------------------------|----------|
| `HELLOASSO_CLIENT_ID`      | HelloAsso API client ID                     | ✅       |
| `HELLOASSO_CLIENT_SECRET`  | HelloAsso API client secret                 | ✅       |
| `HELLOASSO_API_URL`        | HelloAsso API base URL (default: prod)      | ❌       |
| `HELLOASSO_ORGANIZATION_SLUG` | HelloAsso organization slug             | ✅       |
| `DATABASE_URL`             | Full DB URL (Heroku, Azure...)              | ❌       |

---

## 🐳 Docker Support

Coming soon: ready-to-use `docker-compose.yml` for API + PostgreSQL.

---

## 📜 License

MIT License. See `LICENSE` for more details.

---

## ❤️ Contributing

- ⭐ Star the project
- 🐛 Open issues if you find bugs
- 🎉 Pull requests are welcome!
