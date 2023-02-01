import {
  Box,
  Button,
  Divider,
  Stack, TextField,
} from '@mui/material';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';
import { selectSchedule } from '../../../../utils/redux/schedule/scheduleSlice';
import ModalStaticBackdrop from '../../../layouts/ModalStaticBackdrop';
import { updateSchedule } from '../utils/schedule';

function DateInput() {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);
  const [modalOpen, setModalOpen] = useState(false);
  const changeSchedule = (state) => {
    updateSchedule(dispatch, schedule, state);
  };
  return (
    <>
      <TextField
        id="date"
        label={SCHEDULE_DRAWER.date}
        type="date"
        fullWidth
        InputLabelProps={{
          shrink: true,
        }}
        value={schedule.date}
        onChange={changeSchedule}
        size="small"
      />
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        spacing={2}
        sx={{ width: '100%' }}
      >
        <TextField
          id="start_time"
          label={SCHEDULE_DRAWER.start_time}
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          fullWidth
          value={schedule.start_time}
          onChange={changeSchedule}
          size="small"
        />
        <TextField
          id="end_time"
          label={SCHEDULE_DRAWER.end_time}
          type="time"
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          fullWidth
          value={schedule.end_time}
          onChange={changeSchedule}
          size="small"
        />
      </Stack>
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        spacing={2}
        sx={{ width: '100%' }}
      >
        <TextField
          id="start_time"
          label={`${SCHEDULE_DRAWER.start_time} (동작x 구현중)`}
          fullWidth
          value={schedule.start_time}
          onClick={() => setModalOpen(!modalOpen)}
          size="small"
        />
        <TextField
          id="end_time"
          label={`${SCHEDULE_DRAWER.end_time} (동작x 구현중)`}
          fullWidth
          value={schedule.end_time}
          onClick={() => setModalOpen(!modalOpen)}
          size="small"
        />
      </Stack>
      <ModalStaticBackdrop
        keepMounted
        width="xl"
        open={modalOpen}
        component={(
          <Box>
            <Stack p={2}>
              <Stack direction="row" justifyContent="center">
                <Button>오전</Button>
                <Button>오후</Button>
              </Stack>
              <Box my={3}>
                <Divider />
              </Box>
              <Box>
                {
                  Array.from({ length: 12 }, (_, i) => i + 1).map((n) => <Button>{n}</Button>)
                }
              </Box>
              <Box my={3}>
                <Divider />
              </Box>
              <Stack direction="row" justifyContent="center">
                <Button>00</Button>
                <Button>30</Button>
              </Stack>
            </Stack>

            <Button fullWidth variant="contained" onClick={() => setModalOpen(false)}>닫기</Button>
          </Box>
        )}
      />
    </>

  );
}
export default DateInput;
