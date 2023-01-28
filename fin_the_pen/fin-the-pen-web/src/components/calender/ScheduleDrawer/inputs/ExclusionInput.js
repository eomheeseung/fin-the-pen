import {
  Stack, Switch, Typography,
} from '@mui/material';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';

function ExclusionInput({ schedule, updateExclusion }) {
  return (
    <Stack direction="row" justifyContent="space-between" alignItems="center" p={1}>
      <Typography>{SCHEDULE_DRAWER.exclusion_title}</Typography>
      <Stack direction="row" alignItems="center">
        <Switch
          id="exclusion"
          checked={schedule.exclusion}
          value={schedule.exclusion}
          onChange={updateExclusion}
        />
      </Stack>
    </Stack>
  );
}
export default ExclusionInput;
