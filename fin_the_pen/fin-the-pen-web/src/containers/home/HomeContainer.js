/* eslint-disable no-unused-vars */
import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Alert, Box, Button, Divider, Snackbar, Stack, Typography,
} from '@mui/material';
import moment from 'moment';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import ScheduleStatusCard from '../../components/assetManagement/ScheduleStatusCard';
import Calender from '../../components/calender/Calender';
import MonthlyStatement from '../../components/calender/MonthlyStatement.js';
import ScheduleList from '../../components/calender/scheduleList/ScheduleList';
import ScheduleViewMode from '../../components/calender/ScheduleViewMode';
import ALERTS from '../../utils/constants/alerts';
import { fetchMonthSchedules } from '../../utils/redux/API';
import { selectGuestMode, setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import {
  changeViewMode, getMonthSchedules, selectDate, selectSchedules, selectViewMode, setSchedules,
} from '../../utils/redux/schedule/scheduleSlice';
import { selectUser } from '../../utils/redux/user/userSlice';

function HomeConatiner() {
  const dispatch = useDispatch();
  const viewMode = useSelector(selectViewMode);
  const date = useSelector(selectDate);
  const user = useSelector(selectUser);
  const guestMode = useSelector(selectGuestMode);
  const schedules = useSelector(selectSchedules);
  const today = moment();

  // 추후 삭제 예정
  const random = Math.floor((Math.random() * 5));
  const [snackbarOpen, setSnackbarOpen] = useState(true);

  const [expandAccordion, setExpandAccordion] = useState(false);

  const handleExpand = () => {
    setExpandAccordion(!expandAccordion);
  };

  useEffect(() => {
    dispatch(setHeaderOpenTrue('home'));
    dispatch(changeViewMode('schedule'));
  }, []);

  // const getSchedules = async () => {
  //   console.log('월별 데이터를 수신할 위치');
  //   const result = await fetchMonthSchedules({
  //     user_id: user.user_id,
  //     date: moment(date).format('YYYY-MM'),
  //   });
  //   console.log(moment(date).format('YYYY-MM'));
  //   console.log(result);
  //   dispatch(setSchedules(result));
  // };

  const getSchedules = () => {
    dispatch(getMonthSchedules({
      user_id: user.user_id,
      date: moment(date).format('YYYY-MM'),
    }));
  };

  useEffect(() => {
    // 게스트 모드가 아닌 경우에만 서버에 데이터를 요청
    if (user && !guestMode) {
      getSchedules();
    }
  }, [date]);

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
      {viewMode === 'schedule' ? (
        <>
          <Calender dateHeight={50} />
          <Divider />
          <ScheduleList />
        </>
      ) : (
        <Box sx={{ mb: 8 }}>
          <Box sx={{ mx: 2 }}>
            <MonthlyStatement />
          </Box>

          <Accordion expanded={expandAccordion} disableGutters elevation={0}>
            <AccordionSummary>
              <Stack direction="row" justifyContent="space-between" width="100%">
                <Typography sx={{ display: 'flex', alignItems: 'center', fontWeight: 'bold' }}>전체 내역</Typography>
                <Button onClick={handleExpand}>{expandAccordion ? '달력 닫기' : '달력 보기'}</Button>
              </Stack>
            </AccordionSummary>
            <AccordionDetails sx={{ p: 0 }}>
              <Calender dateHeight={85} />
            </AccordionDetails>
          </Accordion>

          <Box sx={{ mx: 2 }}>
            <ScheduleStatusCard
              month={today.format('M월')}
              numberOfSchedule={schedules.filter((s) => today.isSame(s.date, 'month') && today.isSameOrBefore(s.date, 'day')).length}
            />
          </Box>
        </Box>
      )}
      <ScheduleViewMode />
    </Box>
  );
}
export default HomeConatiner;
