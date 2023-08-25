### Nginx 사용

---

`Nginx` 를 사용하여 페이지 접근하는 경우 최상위 url 인경우에는 `index.html` 로 정상적으로 페이지 라우팅이 연결되지만, 다른 url 접근시 `404 오류`가 발생하는 경우가 발생한다.

해당 문제 해결은 `nginx 서버` 내부에 접근하여
`/etc/nginx/conf.d/default.conf` 해당 경로의 파일에서 설정값을 추가해주어야 함.

```js
try_files $uri $uri/ /index.html;
```

Dockerfile 사용시에는 파일을 미리 작성해두고, 해당 명령어를 실행해 주면 된다.

#### Dockerfile 내부 입력

```dockerfile
# Docker 내부 nginx 설정 디렉토리를 삭제 (default.conf)
RUN rm -rf /etc/nginx/conf.d
# 로컬에서 만든 conf 폴더를 nginx의 서버에 복사
COPY nginx/nginx.conf /etc/nginx/conf.d/
```

##### nginx/nginx.conf 파일

```js
server {
  listen 80;
  location / {
    root   /usr/share/nginx/html;
    index  index.html index.htm;
    try_files $uri $uri/ /index.html;
  }
  error_page   500 502 503 504  /50x.html;
  location = /50x.html {
    root   /usr/share/nginx/html;
  }
}
```
