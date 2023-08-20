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
      const originalConfig = error.config;
      const CMResponseError = error.response;
      console.log("interceptors.response.err : ", error);

      if (originalConfig.url !== "/auth/signin" && CMResponseError) {
        // 401 status & option status(_retry = false)
        if (CMResponseError.status === 401 && !originalConfig._retry) {
          originalConfig._retry = true;
          console.log("interceptors.response.err : ", error);
          try {
            await dispatch(reissue());

            return api(originalConfig);
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
