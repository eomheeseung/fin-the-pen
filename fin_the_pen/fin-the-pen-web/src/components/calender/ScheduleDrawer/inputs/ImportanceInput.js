import {
  Button, Stack, Typography,
} from '@mui/material';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';

function ImportanceInput({ schedule, updateSchedule }) {
  return (
    <Stack direction="row" justifyContent="space-between" alignItems="center" p={1}>
      <Typography>{SCHEDULE_DRAWER.set_importance_title}</Typography>
      <Stack direction="row" alignItems="center" spacing={1}>
        <Button
          variant={schedule.importance === SCHEDULE_DRAWER.importance_high ? 'contained' : 'outlined'}
          id="importance"
          value={SCHEDULE_DRAWER.importance_high}
          onClick={updateSchedule}
          size="small"
        >
          {SCHEDULE_DRAWER.importance_high}
        </Button>
        <Button
          variant={schedule.importance === SCHEDULE_DRAWER.importance_middle ? 'contained' : 'outlined'}
          id="importance"
          value={SCHEDULE_DRAWER.importance_middle}
          onClick={updateSchedule}
          size="small"
        >
          {SCHEDULE_DRAWER.importance_middle}
        </Button>
        <Button
          variant={schedule.importance === SCHEDULE_DRAWER.importance_low ? 'contained' : 'outlined'}
          id="importance"
          value={SCHEDULE_DRAWER.importance_low}
          onClick={updateSchedule}
          size="small"
        >
          {SCHEDULE_DRAWER.importance_low}
        </Button>
      </Stack>
    </Stack>
  );
}
export default ImportanceInput;
