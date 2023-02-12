import {
  Button, Stack, Tooltip, Typography,
} from '@mui/material';
import ClearIcon from '@mui/icons-material/Clear';
import { useDispatch, useSelector } from 'react-redux';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import { SCHEDULE_DRAWER, SCHEDULE_DRAWER_MODE } from '../../../../utils/constants/schedule';
import { deleteSchedule, selectSchedule } from '../../../../utils/redux/schedule/scheduleSlice';
import { selectGuestMode } from '../../../../utils/redux/common/commonSlice';

function ScheduleDrawerHeader({ mode, setBottomDrawerOpen }) {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);
  const guestMode = useSelector(selectGuestMode);

  const deleteSelectedSchedule = () => {
    if (window.confirm('정말로 삭제 하시겠습니까?')) {
      console.log(schedule.id);
      dispatch(deleteSchedule(schedule.id));
      setBottomDrawerOpen(false);
    }
  };

  return (
    <Stack direction="row" alignItems="center" justifyContent="space-between">
      <Button onClick={() => setBottomDrawerOpen(false)}><ClearIcon /></Button>
      <Typography variant="h5" sx={{ fontWeight: 'bold' }}>{SCHEDULE_DRAWER.drawer_title[mode]}</Typography>

      {
        mode === SCHEDULE_DRAWER_MODE.수정
          ? (
            <Tooltip
              title={!guestMode && '아직 일반 모드에서는 동작하지 않습니다.'}
              placement="top"
            >
              <Button
                onClick={() => deleteSelectedSchedule()}
                color="error"
              >
                <DeleteForeverIcon />
              </Button>
            </Tooltip>

          )
          : <Button disabled />
      }
    </Stack>
  );
}
export default ScheduleDrawerHeader;
