/* eslint-disable max-len */
/* eslint-disable no-unused-vars */
import {
  Alert,
  Box, Grid, Paper, Stack,
} from '@mui/material';
import {
  lightBlue, pink, teal, yellow,
} from '@mui/material/colors';
import moment from 'moment/moment';
import { useSelector } from 'react-redux';
import ScheduleStatusCard from '../../components/momeyManagement/ScheduleStatusCard.js';
import SettingsPaper from '../../components/momeyManagement/SettingsPaper';
import { CATEGORIES } from '../../utils/constants/categories';
import { selectSchedules } from '../../utils/redux/schedule/scheduleSlice';
import { selectUser } from '../../utils/redux/user/userSlice';

function MoneyManagementContainer() {
  const schedules = useSelector(selectSchedules);
  const user = useSelector(selectUser);
  const today = moment();

  return (
    <Box sx={{ m: 3, wordBreak: 'keep-all' }}>
      {user
        ? (
          <>
            <Box sx={{ typography: 'h4', fontWeight: 'bold' }}>
              {`핀더팬과 함께 "${user.name}" 님의 자산과 일정을 관리하세요`}
            </Box>
            <SettingsPaper />
            <Box sx={{ typography: 'h4', fontWeight: 'bold' }}>
              My 스케줄 현황
            </Box>
            <ScheduleStatusCard
              month={today.format('M월')}
              numberOfSchedule={schedules.filter((s) => today.isSame(s.date, 'month') && today.isSameOrBefore(s.date, 'day')).length}
            />

          </>
        ) : <Alert severity="error">로그인이 필요한 페이지입니다.</Alert>}
    </Box>
  );
}
export default MoneyManagementContainer;
