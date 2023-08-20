import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
    "Cache-Control": "no-cache",
  },
  //  cors 통신 (cookie 허용)
  withCredentials: true,
});

export default api;
