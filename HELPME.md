# personal-finance help me

## Troubleshooting & Support Guide

This document provides solutions to common issues and guidance on how to get help with the **Personal Finance Management
System** (built with **Quarkus**).

---

## 1. Installation Issues

### ❓ **Maven Build Fails**

**Error Message:**\
`Could not resolve dependencies for project ...`

**Solution:**

- Ensure **Java 17+** is installed:
  ```bash
  java -version
  ```
- Check if **Maven** is installed and up to date:
  ```bash
  mvn -version
  ```
- Try rebuilding with:
  ```bash
  mvn clean package -U
  ```
  The `-U` flag forces a dependency update.

### ❓ **Quarkus Command Not Found**

**Solution:**

- Ensure you have the **Quarkus CLI** installed:
  ```bash
  quarkus --version
  ```
- If not installed, install it via SDKMAN:
  ```bash
  sdk install quarkus
  ```

---

## 2. Application Fails to Start

### ❓ **Port Already in Use**

**Error Message:**\
`java.net.BindException: Address already in use`

**Solution:**

- Find and stop the conflicting process:
  ```bash
  lsof -i :8080
  ```
  ```bash
  kill -9 <PID>
  ```
- Change the default port by modifying `application.properties`:
  ```
  quarkus.http.port=9090
  ```

### ❓ **Database Connection Issues**

**Error Message:**\
`javax.persistence.PersistenceException: Unable to connect to database`

**Solution:**

- Ensure your database is running.
- Verify the database URL and credentials in `application.properties`:
  ```
  quarkus.datasource.db-kind=postgresql
  quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/personal_finance
  quarkus.datasource.username=your_user
  quarkus.datasource.password=your_password
  ```
- If using **Docker**, restart your database container:
  ```bash
  docker restart <container_id>
  ```

### ❓ **Application Crashes on Startup**

**Error Message:**\
`java.lang.RuntimeException: Unable to start Quarkus application`

**Solution:**

- Run in **development mode** for detailed logs:
  ```bash
  quarkus dev
  ```
- If there are missing dependencies, try:
  ```bash
  mvn quarkus:dev
  ```

---

## 3. API Issues

### ❓ **Authentication Failure**

**Error Message:**\
`401 Unauthorized`

**Solution:**

- Ensure you are sending the correct **JWT token** in request headers.
- Verify authentication configurations in `application.properties`:
  ```
  quarkus.smallrye-jwt.enabled=true
  quarkus.http.auth.policy.default.roles-allowed=user
  ```
- Try logging in again to get a new token.

### ❓ **CORS Policy Blocked**

**Error Message:**\
`Access to fetch at 'http://localhost:8080/api' from origin 'http://frontend-app.com' has been blocked by CORS policy`

**Solution:**

- Enable CORS in `application.properties`:
  ```
  quarkus.http.cors=true
  quarkus.http.cors.origins=*
  quarkus.http.cors.methods=GET,POST,PUT,DELETE
  ```

---

## 4. Running with Docker

### ❓ **Docker Container Exits Immediately**

**Error Message:**\
`Exited (1)`

**Solution:**

- Check logs:
  ```bash
  docker logs personal-finance
  ```
- Ensure your database container is running before starting the application.

### ❓ **Database Connection Fails in Docker**

**Solution:**

- If using **Docker Compose**, ensure the database service is correctly named:
  ```
  quarkus.datasource.jdbc.url=jdbc:postgresql://db:5432/personal_finance
  ```
  where `db` is the service name in `docker-compose.yml`.

---

## 5. How to Get Help

### 📌 **Reporting Issues**

If you encounter a bug or need help, create an issue in the repository:

🔗 [GitHub Issues](https://github.com/ffracas/personal-finance/issues)

### 📌 **Community Support**

Check Quarkus [community forums](https://quarkus.io/community/) for discussions and solutions.

### 📌 **Debugging Tips**

- Run the application in **dev mode** to get more logs:
  ```bash
  quarkus dev
  ```
- Enable **detailed logging** in `application.properties`:
  ```
  quarkus.log.level=DEBUG
  ```
- Check `logs/app.log` (if configured) for additional insights.

---

This guide should help you troubleshoot most issues with the project. If problems persist, feel free to reach out via
GitHub Issues. 🚀

