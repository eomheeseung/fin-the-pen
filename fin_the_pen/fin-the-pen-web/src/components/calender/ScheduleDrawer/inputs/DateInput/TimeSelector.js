import {
  Box,
  Button, Divider, Stack, Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { NOT_STABLE } from '../../../../../utils/constants/common';
import { TIME_SELECTOR } from '../../../../../utils/constants/schedule';
import { convert12to24, convert24to12 } from '../../../../../utils/tools';
import { switchTitle } from '../../utils/schedule';
import StateButton from './StateButton';

function CenterBox({ children }) {
  return (
    <Box sx={{ justifyContent: 'center', display: 'flex' }}>
      {children}
    </Box>
  );
}

function TimeSelector({
  timeId, currentTime, setModalOpen, changeSchedule,
}) {
  const [meridiem, setMeridiem] = useState(TIME_SELECTOR.meridiem.am);
  const [hours, setHours] = useState(9);
  const [minutes, setMinutes] = useState('00');

  const setTime = () => {
    changeSchedule({
      target: {
        id: timeId,
        value: convert12to24(meridiem, hours, minutes),
      },
    });
    setModalOpen(false);
  };

  const initTime = (currentMeridiem, currentHours, currentMinutes) => {
    setMeridiem(currentMeridiem);
    setHours(currentHours);
    setMinutes(currentMinutes);
  };

  useEffect(() => {
    const time12type = convert24to12(currentTime);
    const time = time12type[1].split(':');
    if (timeId) {
      initTime(time12type[0], Number(time[0]), time[1]);
    }
  }, [timeId, currentTime]);

  return (
    <Stack p={2}>
      <Typography color="error" variant="caption">{NOT_STABLE}</Typography>
      <Typography variant="h4">{`${switchTitle(timeId)} 설정`}</Typography>
      <CenterBox>
        <StateButton
          value={TIME_SELECTOR.meridiem.am}
          state={meridiem}
          setState={setMeridiem}
        />
        <StateButton
          value={TIME_SELECTOR.meridiem.pm}
          state={meridiem}
          setState={setMeridiem}
        />
      </CenterBox>
      <Box my={1}>
        <Divider />
      </Box>
      <CenterBox>
        <Typography>시</Typography>
      </CenterBox>
      <Box>
        { Array.from({ length: 12 }, (_, i) => i + 1).map((n) => (
          <StateButton
            key={n}
            value={n}
            state={hours}
            setState={setHours}
          >
            {n}
          </StateButton>
        ))}
      </Box>
      <Box my={1}>
        <Divider />
      </Box>
      <CenterBox>
        <Typography>분</Typography>
      </CenterBox>
      <CenterBox>
        <Box>
          <StateButton
            value={TIME_SELECTOR.minutes.zero}
            state={minutes}
            setState={setMinutes}
          >
            {TIME_SELECTOR.minutes.zero}
          </StateButton>
          <StateButton
            value={TIME_SELECTOR.minutes.thirty}
            state={minutes}
            setState={setMinutes}
          >
            {TIME_SELECTOR.minutes.thirty}
          </StateButton>
        </Box>
      </CenterBox>
      <Box my={1} />
      <Stack direction="row" spacing={1}>
        <Button fullWidth variant="contained" color="error" onClick={() => setModalOpen(false)}>닫기</Button>
        <Button fullWidth variant="contained" color="success" onClick={() => setTime()}>설정하기</Button>
      </Stack>
    </Stack>
  );
}

export default TimeSelector;
