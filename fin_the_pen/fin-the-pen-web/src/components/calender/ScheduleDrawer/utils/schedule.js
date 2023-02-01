import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';
import { setDrawerSchedule } from '../../../../utils/redux/schedule/scheduleSlice';

/* eslint-disable import/prefer-default-export */
export const updateSchedule = (dispatch, schedule, state) => {
  dispatch(setDrawerSchedule({ ...schedule, [state.target.id]: state.target.value }));
};

export const updateAlarm = (dispatch, schedule) => {
  dispatch(setDrawerSchedule({ ...schedule, alarm: !schedule.alarm }));
};

export const updateRepeat = (dispatch, schedule, setOpenDatePickerModal, state) => {
  if ((state.target.name === 'repeating_cycle') && (state.target.value === '없음')) {
    dispatch(setDrawerSchedule({
      ...schedule,
      [state.target.name]: state.target.value,
      repeat_deadline: '없음',
    }));
  } else {
    dispatch(setDrawerSchedule({
      ...schedule,
      [state.target.name]: state.target.value,
    }));
  }
  if ((state.target.name === 'repeat_deadline') && (state.target.value !== '없음')) {
    setOpenDatePickerModal(true);
  }
};

export const updateRepeatEndDate = (schedule, setRepeatEndDate, endDate) => {
  if (endDate.isBefore(schedule.date)) {
    alert('반복 종료일을 다시 선택해주세요.');
  } else {
    setRepeatEndDate(endDate);
  }
};

export const updateCategory = (dispatch, schedule, category) => {
  dispatch(setDrawerSchedule({ ...schedule, category }));
};

export const updateSpendingType = (dispatch, schedule) => {
  if (schedule.type === SCHEDULE_DRAWER.type_plus) {
    dispatch(setDrawerSchedule({ ...schedule, type: SCHEDULE_DRAWER.type_minus }));
  } else {
    dispatch(setDrawerSchedule({ ...schedule, type: SCHEDULE_DRAWER.type_plus }));
  }
};

export const updateExclusion = (dispatch, schedule, state) => {
  dispatch(setDrawerSchedule({ ...schedule, exclusion: state.target.checked }));
};
