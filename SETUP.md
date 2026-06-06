# Inicio rápido (desarrollo local)

## Requisitos

- **Java 21** (el proyecto compila con OpenJDK 21)
- **Maven** (incluido `./mvnw` en el repo)
- **Docker Desktop** (recomendado) o PostgreSQL 16+ en `localhost:5432`

## 1. Base de datos

Con Docker (recomendado):

```bash
./scripts/dev-up.sh
```

Esto levanta PostgreSQL en `localhost:5432` con:

| Campo    | Valor            |
|----------|------------------|
| Base     | `marketplace_db` |
| Usuario  | `postgres`       |
| Password | `postgres`       |

Sin Docker: crea la base manualmente:

```sql
CREATE DATABASE marketplace_db;
```

## 2. Variables de entorno (opcional)

Los valores por defecto en `application.properties` coinciden con Docker. Si necesitas otros credenciales:

```bash
cp .env.example .env
# edita .env
```

## 3. Ejecutar la API

```bash
./scripts/run.sh
```

Alternativa:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./mvnw spring-boot:run
```

La API queda en **http://localhost:8081**.

## 4. Probar endpoints

### Bruno

1. Instala [Bruno](https://www.usebruno.com/).
2. Abre la colección: `bruno/marketplace-api`
3. Selecciona el entorno **Local**
4. Ejecuta las carpetas en orden (`01-auth` → `07-payments`)
5. Tras cada respuesta, actualiza en el entorno las variables `buyerId`, `sellerUserId`, `productId`, etc. según los IDs devueltos en `data`

### Verificación rápida

```bash
curl -s -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test","email":"test@example.com","password":"123456","role":"BUYER"}'
```

## 5. Tests

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./mvnw test
```

Los tests usan H2 en memoria (`src/test/resources/application.properties`).

## Problemas frecuentes

| Síntoma | Solución |
|---------|----------|
| `Unable to locate a Java Runtime` | Define `JAVA_HOME` apuntando a JDK 21 |
| Docker: socket no encontrado | Abre **Docker Desktop** y vuelve a ejecutar `./scripts/dev-up.sh` |
| Error de conexión a PostgreSQL | Confirma que el contenedor está arriba: `docker compose ps` |
| Puerto ocupado | Cambia `server.port` en `application.properties` (por defecto: **8081**) |
