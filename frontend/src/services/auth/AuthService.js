import api from "services/api";

// redux-toolkit 을 사용하여 비동기 통신하는 경우 해당 서비스에서
// .catch() 사용하여 오류를 return 하면 redux 처리부분에서 정상적인 응답을 받은것으로 확인하여
// 비동기호출 실패인 경우인 "rejected" 가 아닌 호출 성공으로 "fulfilled" 이 사용된다.

//  회원가입 서비스
const join = (username, password) => {
  return api
    .post("/auth/join", {
      username,
      password,
    })
    .then((response) => {
      console.log("AuthService join response : ", response);
      alert("회원가입에 성공하였습니다.");
      return response;
    });
};

// 로그인 서비스
const login = (username, password) => {
  return api
    .post("/auth/login", {
      username,
      password,
    })
    .then((response) => {
      console.log("AuthService login response : ", response);
      return response;
    });
};

//  로그아웃 서비스
const logout = () => {
  return api.post("/auth/logout").then((response) => {
    console.log("AuthService logout response : ", response);
    return response;
  });
};

//  token 재발행 서비스
const reissue = () => {
  return api.post("/auth/reissue").then((response) => {
    console.log("AuthService reissue response : ", response);
    return response;
  });
};

const AuthService = {
  join,
  login,
  logout,
  reissue,
};

export default AuthService;
