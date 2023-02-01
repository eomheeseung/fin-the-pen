import { Button } from '@mui/material';
import moment from 'moment';
import { useDispatch, useSelector } from 'react-redux';
import { v4 as uuidv4 } from 'uuid';
import { NEED_SIGN_IN } from '../../../../utils/constants/common';
import {
  NEED_TITLE, REPEAT_CYCLE, SCHEDULE_DRAWER, SCHEDULE_DRAWER_MODE,
} from '../../../../utils/constants/schedule';
import { addSchedule, modifySchedule, selectSchedule } from '../../../../utils/redux/schedule/scheduleSlice';
import { selectUser } from '../../../../utils/redux/user/userSlice';

function DeprecatedButton({ mode, setBottomDrawerOpen }) {
  const dispatch = useDispatch();
  const user = useSelector(selectUser);
  const schedule = useSelector(selectSchedule);

  const addNewSchedule = () => {
    // 반복 일정 추가
    if ((schedule.repeating_cycle !== '없음') && (schedule.repeat_deadline !== '없음')) {
      let repeatDate = moment(schedule.date).add(1, REPEAT_CYCLE[schedule.repeating_cycle]);
      while (moment(schedule.repeat_endDate).isSameOrAfter(repeatDate)) {
        dispatch(addSchedule({ ...schedule, id: uuidv4(), date: repeatDate.format('YYYY-MM-DD') }));
        repeatDate = moment(repeatDate).add(1, REPEAT_CYCLE[schedule.repeating_cycle]);
      }
    }
    // 원래 일정 추가
    dispatch(addSchedule({ ...schedule, id: uuidv4() }));
    setBottomDrawerOpen(false);
  };

  const modifySelectedSchedule = () => {
    dispatch(modifySchedule(schedule));
    setBottomDrawerOpen(false);
  };

  const handleSubmit = () => {
    if (schedule.event_name.length === 0) {
      alert(NEED_TITLE);
      return;
    }
    if (mode === SCHEDULE_DRAWER_MODE.생성) {
      addNewSchedule();
    } else {
      modifySelectedSchedule();
    }
  };
  return (
    <Button
      variant="contained"
      fullWidth
      disabled={user === null}
      onClick={() => handleSubmit()}
    >
      {
    user === null
      ? NEED_SIGN_IN
      : `${SCHEDULE_DRAWER.add_schedule[mode]} (guest)`
  }
    </Button>
  );
}
export default DeprecatedButton;
