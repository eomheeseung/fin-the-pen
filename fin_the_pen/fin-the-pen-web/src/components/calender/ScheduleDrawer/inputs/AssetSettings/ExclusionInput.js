import {
  Stack, Switch, Typography,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { SCHEDULE_DRAWER } from '../../../../../utils/constants/schedule';
import { selectSchedule } from '../../../../../utils/redux/schedule/scheduleSlice';
import { updateExclusion } from '../../utils/schedule';

function ExclusionInput() {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);

  const changeExclustion = (state) => {
    updateExclusion(dispatch, schedule, state);
  };

  return (
    <Stack direction="row" justifyContent="space-between" alignItems="center" p={1}>
      <Typography>{SCHEDULE_DRAWER.exclusion_title}</Typography>
      <Stack direction="row" alignItems="center">
        <Switch
          id="exclusion"
          checked={schedule.exclusion}
          value={schedule.exclusion}
          onChange={changeExclustion}
        />
      </Stack>
    </Stack>
  );
}
export default ExclusionInput;
