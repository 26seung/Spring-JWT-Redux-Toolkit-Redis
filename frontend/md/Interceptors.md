### Axios Interceptors

---

[공식문서 : https://axios-http.com/kr/docs/interceptors](https://axios-http.com/kr/docs/interceptors)

인터셉터(Interceptors)란? `then` 또는 `catch로` 처리되기 전에 요청과 응답을 가로챌 수 있다

### 적용 이유?

1. API 요청시 마다 공통된 기본 URL을 설정하고 싶다.
2. axios 통신을 하면서 고정된 헤더값이 필요하다.
3. 에러가 발생하는 경우 공통된 처리가 필요하다.

서버에 토큰 인증을 필요로 하는 API 요청을 할때마다 헤더에 Authorization 토큰을 넣어줘야하고, `401(Unauthorized)` 에러가 서버로부터 들어오면 `토큰을 갱신해준 후 재요청`을 보내는 과정을 한 곳에서 모두 처리하여 중복 코드를 제거하고 유지보수성을 향상시키기 위해 `Axios 인터셉터를 적용`.

사용자 지정 config로 새로운 Axios 인스턴스를 생성하여 사용하니 더욱 편리해진다.

---

##### Axios 인터셉터

```js
// 요청 인터셉터 추가하기
axios.interceptors.request.use(
  function (config) {
    // 요청이 전달되기 전에 작업 수행
    return config;
  },
  function (error) {
    // 요청 오류가 있는 작업 수행
    return Promise.reject(error);
  }
);

// 응답 인터셉터 추가하기
axios.interceptors.response.use(
  function (response) {
    // 2xx 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
    // 응답 데이터가 있는 작업 수행
    return response;
  },
  function (error) {
    // 2xx 외의 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
    // 응답 오류가 있는 작업 수행
    return Promise.reject(error);
  }
);
```

##### 사용자 지정 config로 새로운 Axios 인스턴스를 생성

```js
// 여기서 생성한 instance는 위의 인터셉터에서 axios를 대체해서 커스텀 사용가능

const instance = axios.create({
  baseURL: "https://some-domain.com/api/",
  timeout: 1000,
  headers: { "X-Custom-Header": "foobar" },
});
```
