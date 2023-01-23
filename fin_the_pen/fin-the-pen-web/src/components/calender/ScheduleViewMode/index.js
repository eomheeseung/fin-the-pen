import { Box, Button } from '@mui/material';
import { grey } from '@mui/material/colors';
import { useDispatch, useSelector } from 'react-redux';
import { VIEW_MODE } from '../../../utils/constants/schedule';
import { changeViewMode, selectViewMode } from '../../../utils/redux/schedule/scheduleSlice';

function ScheduleViewMode() {
  const dispatch = useDispatch();
  const viewMode = useSelector(selectViewMode);

  return (
    <Box
      sx={{
        width: '100vw',
        position: 'fixed',
        bottom: '80px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <Box
        sx={{
          display: 'inline-flex',
          borderRadius: 4,
          backgroundColor: grey[200],
        }}
      >
        <Button
          sx={{ borderRadius: 4 }}
          variant={viewMode === VIEW_MODE.자산 ? 'contained' : 'text'}
          onClick={() => dispatch(changeViewMode(VIEW_MODE.자산))}
        >
          자산
        </Button>
        <Button
          sx={{ borderRadius: 4 }}
          variant={viewMode === VIEW_MODE.일정 ? 'contained' : 'text'}
          onClick={() => dispatch(changeViewMode(VIEW_MODE.일정))}
        >
          일정
        </Button>
      </Box>
    </Box>
  );
}

export default ScheduleViewMode;
