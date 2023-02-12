/* eslint-disable no-unused-vars */
import { Button, Stack, Tooltip } from '@mui/material';
import moment from 'moment';
import { useDispatch, useSelector } from 'react-redux';
import { v4 as uuidv4 } from 'uuid';
import { NEED_SIGN_IN, NOT_AVAILABLE } from '../../../../utils/constants/common';
import {
  NEED_TITLE, REPEAT_CYCLE, SCHEDULE_DRAWER,
} from '../../../../utils/constants/schedule';
import { selectGuestMode } from '../../../../utils/redux/common/commonSlice';
import {
  // createNewSchedule,
  createSchedule, getMonthSchedules,
  // mockCreateNewSchedule,
  modifySchedule, selectDate, selectSchedule,
} from '../../../../utils/redux/schedule/scheduleSlice';
import { selectUser } from '../../../../utils/redux/user/userSlice';
import { handleCreate } from '../utils/schedule';

/**
 * 각종 로직들 모듈로 이전 예정
 */

function ScheduleDrawerFooter({ mode, setBottomDrawerOpen }) {
  const date = useSelector(selectDate);
  const user = useSelector(selectUser);
  const guestMode = useSelector(selectGuestMode);
  const schedule = useSelector(selectSchedule);
  const dispatch = useDispatch();

  const handleModify = async () => {
    /**
     * 함수 완성되면 그 때 외부 모듈로 분리하겠습니다.
     */
    if (guestMode) {
      dispatch(modifySchedule(schedule));
      setBottomDrawerOpen(false);
    } else {
      alert(NOT_AVAILABLE);
    }
  };

  const handleMode = () => {
    switch (mode) {
      case 'create':
        handleCreate(dispatch, schedule, user, guestMode, date, setBottomDrawerOpen);
        break;
      case 'modify':
        handleModify();
        break;
      default:
        alert('잘못 된 요청입니다.');
    }
  };

  const handleSubmit = () => {
    if (schedule.event_name.length === 0) {
      alert(NEED_TITLE);
      return;
    }
    handleMode();
  };

  return (
    <Stack
      direction="row"
      spacing={1}
    >
      <Tooltip
        title={!guestMode && '아직 일반 모드에서는 동작하지 않습니다.'}
        placement="top"
      >
        <Button
          variant="contained"
          fullWidth
          disabled={user === null}
          onClick={() => handleSubmit()}
        >
          {user === null
            ? NEED_SIGN_IN
            : `${SCHEDULE_DRAWER.add_schedule[mode]}`}
        </Button>
      </Tooltip>
    </Stack>
  );
}

export default ScheduleDrawerFooter;
