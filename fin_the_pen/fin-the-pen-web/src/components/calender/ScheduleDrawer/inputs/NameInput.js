/* eslint-disable no-unused-vars */
import {
  FormControl, IconButton, InputAdornment, OutlinedInput,
} from '@mui/material';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { useDispatch, useSelector } from 'react-redux';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';
import { updateAlarm, updateSchedule } from '../utils/schedule';
import { selectSchedule } from '../../../../utils/redux/schedule/scheduleSlice';

function NameInput({ setSchedule }) {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);

  const changeSchedule = (state) => {
    updateSchedule(dispatch, schedule, state);
  };

  const changeAlarm = () => {
    updateAlarm(dispatch, schedule);
  };
  return (
    <FormControl fullWidth>
      <OutlinedInput
        id="event_name"
        startAdornment={<InputAdornment position="start">{SCHEDULE_DRAWER.name}</InputAdornment>}
        endAdornment={(
          <InputAdornment position="end">
            <IconButton
              aria-label="toggle password visibility"
              onClick={changeAlarm}
              edge="end"
            >
              {schedule.alarm ? <NotificationsNoneIcon color="primary" /> : <NotificationsNoneIcon />}
            </IconButton>
          </InputAdornment>
        )}
        value={schedule.event_name}
        onChange={changeSchedule}
        size="small"
      />
    </FormControl>
  );
}
export default NameInput;
