# ==========================================
# ETAPA 1: Compilar el código con Maven
# ==========================================
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copiamos el pom y el código fuente (contexto = carpeta Backend)
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto omitiendo los tests para que sea más rápido
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: Levantar la aplicación
# ==========================================
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Copiamos el .jar que se generó en la Etapa 1
# Usamos el asterisco para que encuentre el archivo sin importar el nombre exacto
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]