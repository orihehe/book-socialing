FROM node:20 AS build
WORKDIR /app
COPY book-socialing-fe/package.json .
COPY book-socialing-fe/pnpm-lock.yaml .
RUN npm install -g pnpm && pnpm install
COPY book-socialing-fe .
RUN pnpm build

FROM nginx:latest
COPY --from=build /app/dist /usr/share/nginx/html
COPY book-socialing-deploy/nginx/default.conf /etc/nginx/conf.d/default.conf
