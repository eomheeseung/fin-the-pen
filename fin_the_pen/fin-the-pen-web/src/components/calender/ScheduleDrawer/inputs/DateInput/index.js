import {
  Alert, Stack, TextField,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { SCHEDULE_DRAWER, WRONG_TIME_ORDER } from '../../../../../utils/constants/schedule';
import { selectSchedule } from '../../../../../utils/redux/schedule/scheduleSlice';
import { isTimeOrderCorrect } from '../../../../../utils/tools';
import ModalStaticBackdrop from '../../../../layouts/ModalStaticBackdrop';
import { updateSchedule } from '../../utils/schedule';
import TimeSelector from './TimeSelector';

function DateInput() {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);
  const [timeId, setTimeId] = useState(null);
  const [currentTime, setCurrentTime] = useState('09:00');
  const [modalOpen, setModalOpen] = useState(false);
  const [error, setError] = useState(null);

  const changeSchedule = (state) => {
    updateSchedule(dispatch, schedule, state);
  };

  const openModal = (id, time) => {
    setTimeId(id);
    setCurrentTime(time);
    setModalOpen(!modalOpen);
  };

  useEffect(() => {
    if (isTimeOrderCorrect(schedule.start_time, schedule.end_time)) {
      setError(false);
    } else {
      setError(true);
    }
  }, [schedule]);

  return (
    <>
      <TextField
        id="date"
        label={SCHEDULE_DRAWER.date}
        type="date"
        fullWidth
        InputLabelProps={{
          shrink: true,
        }}
        value={schedule.date}
        onChange={changeSchedule}
        size="small"
      />
      {/* <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        spacing={2}
        sx={{ width: '100%' }}
      >
        <TextField
          id="start_time"
          label={SCHEDULE_DRAWER.start_time}
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          fullWidth
          value={schedule.start_time}
          onChange={changeSchedule}
          size="small"
        />
        <TextField
          id="end_time"
          label={SCHEDULE_DRAWER.end_time}
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          fullWidth
          value={schedule.end_time}
          onChange={changeSchedule}
          size="small"
        />
      </Stack> */}
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        spacing={2}
        sx={{ width: '100%' }}
      >
        <TextField
          id="start_time"
          label={`${SCHEDULE_DRAWER.start_time} (beta)`}
          fullWidth
          value={schedule.start_time}
          onClick={() => openModal('start_time', schedule.start_time)}
          size="small"
        />
        <TextField
          id="end_time"
          label={`${SCHEDULE_DRAWER.end_time} (beta)`}
          fullWidth
          value={schedule.end_time}
          onClick={() => openModal('end_time', schedule.end_time)}
          size="small"
        />
      </Stack>
      {
        error && (
          <Stack justifyContent="center">
            <Alert color="error">
              {WRONG_TIME_ORDER}
            </Alert>
          </Stack>
        )
      }
      <ModalStaticBackdrop
        keepMounted
        width="xs"
        open={modalOpen}
        component={(
          <TimeSelector
            timeId={timeId}
            currentTime={currentTime}
            setModalOpen={setModalOpen}
            changeSchedule={changeSchedule}
          />
        )}
      />
    </>

  );
}
export default DateInput;
