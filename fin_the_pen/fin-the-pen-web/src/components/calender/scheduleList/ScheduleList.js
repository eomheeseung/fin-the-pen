import {
  Box, Drawer, Stack, Typography,
} from '@mui/material';
import moment from 'moment';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { CATEGORIES } from '../../../utils/constants/categories';
import { SCHEDULE_DRAWER_MODE } from '../../../utils/constants/schedule';
import { fetchSchedules } from '../../../utils/redux/API';
import { selectGuestMode } from '../../../utils/redux/common/commonSlice';
import { selectDate, selectSchedules, setSchedules } from '../../../utils/redux/schedule/scheduleSlice';
import { selectUser } from '../../../utils/redux/user/userSlice';
import ScheduleDrawer from '../ScheduleDrawer';
import ScheduleCard from './ScheduleCard';

function ScheduleList() {
  const dispatch = useDispatch();
  const [bottomDrawerOpen, setBottomDrawerOpen] = useState(false);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const schedules = useSelector(selectSchedules);
  const date = moment(useSelector(selectDate)).format('YYYY-MM-DD');
  const user = useSelector(selectUser);
  const guestMode = useSelector(selectGuestMode);

  const handleModal = (schedule) => {
    setSelectedSchedule(schedule);
    setBottomDrawerOpen(true);
  };

  const getSchedules = async () => {
    console.log('전체 데이터를 수신할 위치');
    const result = await fetchSchedules(user.user_id);
    console.log(result);
    dispatch(setSchedules(result));
  };

  useEffect(() => {
    // 게스트 모드가 아닌 경우에만 서버에 데이터를 요청
    if (user && !guestMode) {
      getSchedules();
    }
  }, [date]);

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
          .filter((schedule) => schedule.date === date)
          .map((schedule) => (
            <ScheduleCard
              schedule={schedule}
              category={(CATEGORIES.find((c) => c.title === schedule.category) || { color: '#C8A2C8' })}
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
