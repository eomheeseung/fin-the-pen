import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  settings: {
    // 화면설정
    앱비밀번호: false,
    인증단계: 1,
    화면테마: 'default',
    예산숨김: false,
    // 일정
    반복일정목록: [],
    숨김일정: false,
    숨김일정목록: false,
    // 보안/인증서
    비밀번호인증단계: 1,
    // 알림
    기본알림: false,
    진동: false,
    소리: false,
    알림음: '',
    잠금화면알람: false,
    일정알림시간: '5분전', // [5분전, 10분전, 15분전, 30분전, 1시간전, 자정 알림, 사용자지정]
    소비주의알림: false,
    정기입출금목록: [],
    // 연결관리
    마이데이터: [],
    캘린더목록: [],
  },
};

export const settingSlice = createSlice({
  name: 'setting',
  initialState,
  reducers: {
    updateSettings: (state, action) => {
      // eslint-disable-next-line no-param-reassign
      state.settings = action.payload;
    },
  },
});
export const { updateSettings } = settingSlice.actions;

export const selectSettings = (state) => state.setting.settings;

export default settingSlice.reducer;
