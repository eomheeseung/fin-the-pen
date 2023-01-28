/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  headerOpen: true,
  headerMode: 'home',
  guestMode: false,
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
    setGuestModeTrue: (state) => {
      state.guestMode = true;
    },
    setGuestModeFalse: (state) => {
      state.guestMode = false;
    },
  },
});
export const {
  setHeaderOpenTrue,
  setHeaderOpenFalse,
  setGuestModeTrue,
  setGuestModeFalse,
} = commonSlice.actions;

export const selectHeaderOpen = (state) => state.common.headerOpen;
export const selectHeaderMode = (state) => state.common.headerMode;
export const selectGuestMode = (state) => state.common.guestMode;

export default commonSlice.reducer;
