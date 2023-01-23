/* eslint-disable no-param-reassign */
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { fetchMockLogin } from '../mockAPI';

const initialState = {
  user: null,
  status: 'idle',
};

export const mockLogin = createAsyncThunk(
  // 지연 있는 메소드
  'user/mockLogin',
  async () => {
    const response = await fetchMockLogin();
    // The value we return becomes the `fulfilled` action payload
    return response.data;
  },
);

export const userSlice = createSlice({
  name: 'user',
  initialState,
  // The `reducers` field lets us define reducers and generate associated actions
  reducers: {
    // Use the PayloadAction type to declare the contents of `action.payload`
    initStatus: (state) => {
      state.status = 'idle';
    },
    logOut: (state) => {
      state.user = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(mockLogin.pending, (state) => {
        // incrementAsync가 진행중일 때
        state.status = 'loading';
      })
      .addCase(mockLogin.fulfilled, (state, action) => {
        // incrementAsync가 끝나면
        state.status = 'idle';
        state.user = action.payload;
      });
  },
});

export const { initStatus, logOut } = userSlice.actions;

export const selectUser = (state) => state.user.user;
export const selectStatus = (state) => state.user.status;

export default userSlice.reducer;
