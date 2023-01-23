import {
  BottomNavigation, BottomNavigationAction, Drawer, Paper,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import DataSaverOffIcon from '@mui/icons-material/DataSaverOff';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { useState } from 'react';
import SettingsIcon from '@mui/icons-material/Settings';
import PaidIcon from '@mui/icons-material/Paid';
import { useSelector } from 'react-redux';
import moment from 'moment';
import PATH from '../../utils/constants/path';
import ScheduleDrawer from '../calender/ScheduleDrawer';
import { INIT_SCHEDULE, SCHEDULE_DRAWER_MODE } from '../../utils/constants/schedule';
import { selectDate } from '../../utils/redux/schedule/scheduleSlice';

function BottomBar({ value, setValue }) {
  const date = useSelector(selectDate);
  const navigate = useNavigate();
  const [bottomDrawerOpen, setBottomDrawerOpen] = useState(false);

  return (
    <>
      <Paper
        sx={{
          position: 'fixed',
          bottom: 0,
          left: 0,
          right: 0,
          paddingBottom: 1,
        }}
        elevation={3}
      >
        <BottomNavigation
          value={value}
          onChange={(event, newValue) => {
            setValue(newValue);
          }}
        >
          <BottomNavigationAction label="홈" icon={<CalendarMonthIcon />} onClick={() => navigate(PATH.home)} />
          <BottomNavigationAction label="리포트" icon={<DataSaverOffIcon />} onClick={() => navigate(PATH.analysis)} />
          <BottomNavigationAction label="" icon={<AddCircleIcon />} onClick={() => setBottomDrawerOpen(true)} />
          <BottomNavigationAction label="자산관리" icon={<PaidIcon />} onClick={() => navigate(PATH.moneyManagement)} />
          <BottomNavigationAction label="설정" icon={<SettingsIcon />} onClick={() => navigate(PATH.settings)} />
        </BottomNavigation>
      </Paper>
      <Drawer
        open={bottomDrawerOpen}
        anchor="bottom"
        onClose={() => setBottomDrawerOpen(false)}
      >
        {/* 이 부분을 범용적으로 사용할 수 있게 만드는 건 어떨까? */}
        <ScheduleDrawer
          setBottomDrawerOpen={setBottomDrawerOpen}
          data={{
            ...INIT_SCHEDULE,
            date: moment(date).format('YYYY-MM-DD'),
            repeat_endDate: moment(date).format('YYYY-MM-DD'),
          }}
          mode={SCHEDULE_DRAWER_MODE.생성}
        />
      </Drawer>
    </>
  );
}
export default BottomBar;
/**
 * 하단 바
 */
