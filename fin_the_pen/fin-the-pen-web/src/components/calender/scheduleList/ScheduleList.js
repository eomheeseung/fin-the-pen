import {
  Box, Drawer, Stack, Typography,
} from '@mui/material';
import moment from 'moment';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { SCHEDULE_DRAWER_MODE } from '../../../utils/constants/schedule';
import { selectDate, selectSchedules } from '../../../utils/redux/schedule/scheduleSlice';
import ScheduleDrawer from '../ScheduleDrawer';
import ScheduleCard from './ScheduleCard';

function ScheduleList() {
  const [bottomDrawerOpen, setBottomDrawerOpen] = useState(false);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const schedules = useSelector(selectSchedules);
  const date = moment(useSelector(selectDate)).format('YYYY-MM-DD');

  const handleModal = (schedule) => {
    setSelectedSchedule(schedule);
    setBottomDrawerOpen(true);
  };

  return (
    <>
      {schedules.filter((el) => el.date === date).length === 0 && (
        <Stack
          justifyContent="center"
          alignItems="center"
        >
          <Box my={5}>
            <Typography>
              {date}
              에 등록된 일정이 없습니다!
            </Typography>
          </Box>
        </Stack>
      )}
      {
        schedules
          .filter((el) => el.date === date)
          .map((el) => (
            <ScheduleCard
              schedule={el}
              key={Math.random()}
              handleModal={handleModal}
            />
          ))
      }
      <Drawer
        open={bottomDrawerOpen}
        anchor="bottom"
        onClose={() => setBottomDrawerOpen(false)}
      >
        {/* 이 부분을 범용적으로 사용할 수 있게 만드는 건 어떨까? */}
        <ScheduleDrawer
          setBottomDrawerOpen={setBottomDrawerOpen}
          data={selectedSchedule}
          mode={SCHEDULE_DRAWER_MODE.수정}
        />
      </Drawer>
    </>
  );
}

export default ScheduleList;
