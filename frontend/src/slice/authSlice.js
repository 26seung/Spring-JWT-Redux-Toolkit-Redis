import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import AuthService from "services/auth/AuthService";

//  회원가입 Dispatch
export const join = createAsyncThunk(
  "auth/join",
  async ({ username, password }, thunkAPI) => {
    try {
      const response = await AuthService.join(username, password);
      console.log("createAsyncThunk join Dispatch :", response);
      return response.data;
    } catch (error) {
      console.log("createAsyncThunk join error Dispatch :", error);
      const rejected = error.response.data;
      return thunkAPI.rejectWithValue(rejected);
    }
  }
);

//  로그인 Dispatch
export const login = createAsyncThunk(
  "auth/login",
  async ({ username, password }, thunkAPI) => {
    try {
      const response = await AuthService.login(username, password);
      console.log("createAsyncThunk login Dispatch : ", response);
      return response.data;
    } catch (error) {
      console.log("createAsyncThunk login error Dispatch : ", error);
      const rejected = error.response.data;
      return thunkAPI.rejectWithValue(rejected);
    }
  }
);

//  로그아웃 Dispatch
export const logout = createAsyncThunk("auth/logout", async (thunkAPI) => {
  try {
    const response = await AuthService.logout();
    console.log("createAsyncThunk logout Dispatch :", response);
    return response.data;
  } catch (error) {
    console.log("createAsyncThunk logout error Dispatch :", error);
    const rejected = error.response.data;
    return thunkAPI.rejectWithValue(rejected);
  }
});

//  token 재발행 Dispatch
export const reissue = createAsyncThunk("auth/reissue", async (thunkAPI) => {
  try {
    const response = await AuthService.reissue();
    console.log("createAsyncThunk reissue Dispatch : ", response);
    return response.data;
  } catch (error) {
    console.log("createAsyncThunk reissue error Dispatch : ", error);
    const rejected = error.response.data;
    return thunkAPI.rejectWithValue(rejected);
  }
});

//  초기 paylod 값은 null, fulfilled 시 주입
const initialState = { user: null, loading: null };

//  (reducers: 동기 , extraReducers: 비동기) 에 따라 구분 사용
const authSlice = createSlice({
  name: "auth",
  initialState,
  extraReducers: (builder) => {
    builder
      //  회원가입 reducer
      .addCase(join.fulfilled, (state, action) => {
        state.loading = false;
      })
      .addCase(join.rejected, (state, action) => {
        state.loading = false;
      })
      //  로그인 reducer
      .addCase(login.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload;
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.user = null;
      })
      //  로그아웃 reducer
      .addCase(logout.fulfilled, (state, action) => {
        state.loading = false;
        state.user = null;
      })
      //  token 갱신 reducer
      .addCase(reissue.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(reissue.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload;
      })
      .addCase(reissue.rejected, (state, action) => {
        state.loading = false;
        state.user = null;
      });
  },
});

const { reducer } = authSlice;
export default reducer;

//  useSelector() Function
export const selectCurrentUser = (state) => state.auth.user;
export const selectCurrentLoading = (state) => state.auth.loading;
