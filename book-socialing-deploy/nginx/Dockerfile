# Stage 1: Build the frontend application
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json (or pnpm-lock.yaml)
COPY ../book-socialing-fe/package.json ./
COPY ../book-socialing-fe/pnpm-lock.yaml ./

# Install dependencies with pnpm
RUN npm install -g pnpm
RUN pnpm install

# Copy the rest of the frontend source code
COPY ../book-socialing-fe/ ./

# Build the application
RUN pnpm run build

# Stage 2: Serve the application with Nginx
FROM nginx:latest

# Copy the built static files from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy the custom Nginx configuration
COPY ./default.conf /etc/nginx/conf.d/default.conf

# Expose port 80 and 443
EXPOSE 80 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
