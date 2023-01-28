/* eslint-disable no-unused-vars */
import {
  Alert, Box, Divider, Snackbar,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import Calender from '../../components/calender/Calender';
import MonthlyStatement from '../../components/calender/MonthlyStatement.js';
import ScheduleList from '../../components/calender/scheduleList/ScheduleList';
import ScheduleViewMode from '../../components/calender/ScheduleViewMode';
import ALERTS from '../../utils/constants/alerts';
import { setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import { changeViewMode, selectViewMode } from '../../utils/redux/schedule/scheduleSlice';

function HomeConatiner() {
  const dispatch = useDispatch();
  const viewMode = useSelector(selectViewMode);

  // 추후 삭제 예정
  const random = Math.floor((Math.random() * 5));
  const [snackbarOpen, setSnackbarOpen] = useState(true);

  useEffect(() => {
    dispatch(setHeaderOpenTrue('home'));
    dispatch(changeViewMode('asset'));
  }, []);

  const handleClose = () => {
    setSnackbarOpen(false);
  };

  return (
    <Box>
      <Snackbar
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
        autoHideDuration={5000}
        open={snackbarOpen}
        onClose={handleClose}
      >
        <Alert color={ALERTS[random].color} sx={{ width: '100%' }} icon={ALERTS[random].icon}>
          {ALERTS[random].message}
        </Alert>
      </Snackbar>
      {viewMode === 'schedule'
        ? (
          <>
            <Calender dateHeight={50} />
            <Divider />
            <ScheduleList />
          </>
        )
        : (
          <>
            <Calender dateHeight={100} />
            <MonthlyStatement />
          </>
        )}
      <ScheduleViewMode />
    </Box>
  );
}
export default HomeConatiner;
