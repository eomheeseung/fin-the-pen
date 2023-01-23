/* eslint-disable no-unused-vars */
import {
  Box,
  Divider,
} from '@mui/material';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import Calender from '../../components/calender/Calender';
import ScheduleList from '../../components/calender/scheduleList/ScheduleList';
import ScheduleViewMode from '../../components/calender/ScheduleViewMode';
import { setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import { selectViewMode } from '../../utils/redux/schedule/scheduleSlice';

function HomeConatiner() {
  const dispatch = useDispatch();
  const viewMode = useSelector(selectViewMode);

  useEffect(() => {
    dispatch(setHeaderOpenTrue('home'));
  }, []);

  return (
    <Box>
      <Calender dateHeight={viewMode === 'schedule' ? 50 : 120} />
      {viewMode === 'schedule' && (
      <>
        <Divider />
        <ScheduleList />
      </>
      )}
      <ScheduleViewMode />
    </Box>
  );
}
export default HomeConatiner;
