import {
  Stack, TextField,
} from '@mui/material';
import { ADD_SCHEDULE } from '../../../../utils/constants/schedule';

function DateInput({ schedule, updateSchedule }) {
  return (
    <>
      <TextField
        id="date"
        label={ADD_SCHEDULE.date}
        type="date"
        fullWidth
        InputLabelProps={{
          shrink: true,
        }}
        value={schedule.date}
        onChange={updateSchedule}
        size="small"
      />
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        spacing={2}
        sx={{ width: '100%' }}
      >
        <TextField
          id="start_time"
          label={ADD_SCHEDULE.start_time}
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          fullWidth
          value={schedule.start_time}
          onChange={updateSchedule}
          size="small"
        />
        <TextField
          id="end_time"
          label={ADD_SCHEDULE.end_time}
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          fullWidth
          value={schedule.end_time}
          onChange={updateSchedule}
          size="small"
        />
      </Stack>
    </>

  );
}
export default DateInput;
