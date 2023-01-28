import {
  FormControl, IconButton, InputAdornment, OutlinedInput,
} from '@mui/material';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';

function NameInput({
  schedule, updateSchedule, updateAlarm,
}) {
  return (
    <FormControl fullWidth>
      <OutlinedInput
        id="event_name"
        startAdornment={<InputAdornment position="start">{SCHEDULE_DRAWER.name}</InputAdornment>}
        endAdornment={(
          <InputAdornment position="end">
            <IconButton
              aria-label="toggle password visibility"
              onClick={updateAlarm}
              edge="end"
            >
              {schedule.alarm ? <NotificationsNoneIcon color="primary" /> : <NotificationsNoneIcon />}
            </IconButton>
          </InputAdornment>
        )}
        value={schedule.event_name}
        onChange={updateSchedule}
        size="small"
      />
    </FormControl>
  );
}
export default NameInput;
