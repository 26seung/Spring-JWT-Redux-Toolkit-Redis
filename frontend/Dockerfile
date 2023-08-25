FROM node:18.17.0-alpine as build
WORKDIR /app
COPY package.json /app/package.json
RUN npm install --silent
COPY . /app
RUN npm run build

# production environment
FROM nginx:1.23-alpine
COPY --from=build /app/build /usr/share/nginx/html
# Docker 내부 nginx 설정 디렉토리를 삭제 (default.conf)
RUN rm -rf /etc/nginx/conf.d
# 로컬에서 만든 conf 폴더를 nginx의 서버에 복사
COPY nginx/nginx.conf /etc/nginx/conf.d/
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]