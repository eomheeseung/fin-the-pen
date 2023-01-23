/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  headerOpen: true,
  headerMode: 'home',
};

export const commonSlice = createSlice({
  name: 'common',
  initialState,
  reducers: {
    setHeaderOpenTrue: (state, action) => {
      const mode = action.payload;
      if (mode !== undefined) {
        state.headerMode = mode;
      }
      state.headerOpen = true;
    },
    setHeaderOpenFalse: (state) => {
      state.headerOpen = false;
    },
  },
});
export const { setHeaderOpenTrue, setHeaderOpenFalse } = commonSlice.actions;

export const selectHeaderOpen = (state) => state.common.headerOpen;
export const selectHeaderMode = (state) => state.common.headerMode;

export default commonSlice.reducer;
