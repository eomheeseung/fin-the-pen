import { Button, Stack, Typography } from '@mui/material';
import ClearIcon from '@mui/icons-material/Clear';
import { useDispatch, useSelector } from 'react-redux';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import { SCHEDULE_DRAWER, SCHEDULE_DRAWER_MODE } from '../../../../utils/constants/schedule';
import { deleteSchedule, selectSchedule } from '../../../../utils/redux/schedule/scheduleSlice';

function ScheduleDrawerHeader({ mode, setBottomDrawerOpen }) {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);

  const deleteSelectedSchedule = () => {
    dispatch(deleteSchedule(schedule.id));
    setBottomDrawerOpen(false);
  };

  return (
    <Stack direction="row" alignItems="center" justifyContent="space-between">
      <Button onClick={() => setBottomDrawerOpen(false)}><ClearIcon /></Button>
      <Typography variant="h5" sx={{ fontWeight: 'bold' }}>{SCHEDULE_DRAWER.drawer_title[mode]}</Typography>

      {
        mode === SCHEDULE_DRAWER_MODE.수정
          ? <Button onClick={() => deleteSelectedSchedule()} color="error"><DeleteForeverIcon /></Button>
          : <Button disabled />
      }
    </Stack>
  );
}
export default ScheduleDrawerHeader;
