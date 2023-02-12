import moment from 'moment';
import { v4 as uuidv4 } from 'uuid';
import { REPEAT_CYCLE, SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';
import { createSchedule, getMonthSchedules, setDrawerSchedule } from '../../../../utils/redux/schedule/scheduleSlice';

/**
 * index
 */

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

export const switchTitle = (id) => {
  switch (id) {
    case 'start_time':
      return '일정 시작 시각';
    case 'end_time':
      return '일정 종료 시각';
    default:
      return 'error';
  }
};

/**
 * Footer
 */

export const handleCreate = async (
  dispatch,
  schedule,
  user,
  guestMode,
  date,
  setBottomDrawerOpen,
) => {
  const scheduleWithUuid = {
    ...schedule,
    id: uuidv4(),
    user_id: user.user_id,
  };
  // 반복 일정 추가
  if ((schedule.repeating_cycle !== '없음') && (schedule.repeat_deadline !== '없음')) {
    let repeatDate = moment(schedule.date).add(1, REPEAT_CYCLE[schedule.repeating_cycle]);
    while (moment(schedule.repeat_endDate).isSameOrAfter(repeatDate)) {
      // eslint-disable-next-line no-await-in-loop
      await dispatch(createSchedule({
        ...scheduleWithUuid,
        id: uuidv4(),
        date: repeatDate.format('YYYY-MM-DD'),
      }));
      repeatDate = moment(repeatDate).add(1, REPEAT_CYCLE[schedule.repeating_cycle]);
    }
  }
  // 원래 일정 추가
  await dispatch(createSchedule(scheduleWithUuid));
  if (!guestMode) { // 게스트 모드가 아니라면, 현재 서버 상태를 새롭게 요청하기
    dispatch(getMonthSchedules({
      user_id: user.user_id,
      date: moment(date).format('YYYY-MM'),
    }));
  }
  setBottomDrawerOpen(false);
};
