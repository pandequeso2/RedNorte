# ==========================================
# ETAPA 1: Construir la aplicación React con Vite
# ==========================================
FROM node:20-alpine AS builder
WORKDIR /build

# Copiar archivos de dependencias primero (cacheo)
COPY package*.json ./
RUN npm install

# Copiar el resto del código fuente
COPY . .

# Compilar la aplicación (Vite genera en /build/dist)
RUN npm run build

# ==========================================
# ETAPA 2: Servir con Nginx
# ==========================================
FROM nginx:alpine
# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar los archivos estáticos generados en la etapa anterior
COPY --from=builder /build/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]