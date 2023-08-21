import api from "services/api";
import { reissue } from "slice/authSlice";

// 요청 인터셉터
const interceptors = (store) => {
  api.interceptors.request.use(
    (config) => {
      const accessToken = store.getState().auth.user?.data.accessToken;
      // console.log("interceptors.request : ", config);
      if (accessToken) {
        //  token에 Bearer 추가하여 header 설정
        config.headers["Authorization"] = "Bearer " + accessToken;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  const { dispatch } = store;

  // 응답 인터셉터
  api.interceptors.response.use(
    (res) => {
      // console.log("interceptors.response : ", res);
      return res;
    },
    async (error) => {
      // console.log("interceptors.response.err : ", error);
      const {
        config,
        response: { status },
      } = error;

      //  url check
      if (config.url !== "/auth/reissue" && config.url !== "/auth/login") {
        // 401 status & option config(_reissue = false)
        if (status === 401 && !config._reissue) {
          //  중복 수행 방지 체크
          config._reissue = true;
          try {
            //  token 재발행 Dispatch
            await dispatch(reissue());

            return api(config);
          } catch (err) {
            return Promise.reject(err);
          }
        }
      }

      return Promise.reject(error);
    }
  );
};

export default interceptors;
