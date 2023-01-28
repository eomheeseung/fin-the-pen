/* eslint-disable max-len */
/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
import moment from 'moment';

const initialState = {
  date: moment(new Date()),
  schedules: [],
  filtered: [],
  filtered_date: {
    start: moment(new Date()),
    end: moment(new Date()),
  },
  viewMode: 'asset',
};

export const scheduleSlice = createSlice({
  name: 'schedule',
  initialState,
  reducers: {
    addSchedule: (state, action) => {
      state.schedules.push(action.payload);
    },
    deleteSchedule: (state, action) => {
      state.schedules = state.schedules.filter((s) => s.id !== action.payload);
    },
    modifySchedule: (state, action) => {
      state.schedules = state.schedules.map((s) => (s.id === action.payload.id ? action.payload : s));
    },
    setSchedules: (state, action) => {
      state.schedules = action.payload;
    },
    selectedDate: (state, action) => {
      state.date = action.payload;
    },
    updateFilter: (state, action) => {
      /**
       * 1. 배열 안에 이미 있는 단어라면 제거
       * 2. 배열 안에 없는 단어라면 추가
       */
      if (state.filtered.includes(action.payload)) {
        state.filtered = state.filtered.filter((f) => f !== action.payload);
      } else {
        const set = new Set([...state.filtered].concat(action.payload));
        state.filtered = Array.from(set);
      }
    },
    updateFiltersForce: (state, action) => {
      /**
       * mode가 write라면 들어온 categories를 state.filtered에 강제로 추가
       * mode가 remove라면 들어온 categories를 state.filtered에서 강제로 제거
       */
      const { mode, categories } = action.payload;
      switch (mode) {
        case 'write':
          state.filtered = Array.from(new Set([...state.filtered].concat(categories)));
          break;
        case 'remove':
          categories.forEach((cat) => {
            state.filtered = state.filtered.filter((f) => f !== cat);
          });
          break;
        default:
          alert('잘못 된 요청입니다.');
      }
    },
    initFilter: (state) => {
      state.filtered = [];
    },
    changeViewMode: (state, action) => {
      state.viewMode = action.payload;
    },
  },
});
export const {
  addSchedule,
  deleteSchedule,
  setSchedules,
  selectedDate,
  modifySchedule,
  updateFilter,
  updateFiltersForce,
  initFilter,
  changeViewMode,
} = scheduleSlice.actions;

export const selectSchedules = (state) => state.schedule.schedules;
export const selectDate = (state) => state.schedule.date;
export const selectFiltered = (state) => state.schedule.filtered;
export const selectFilteredDate = (state) => state.schedule.filtered_date;
export const selectViewMode = (state) => state.schedule.viewMode;

export default scheduleSlice.reducer;
