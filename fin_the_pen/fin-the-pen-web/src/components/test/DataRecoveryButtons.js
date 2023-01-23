import {
  Box, Button, Divider, Stack, TextField, Typography,
} from '@mui/material';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { setSchedules } from '../../utils/redux/schedule/scheduleSlice';
import ModalStaticBackdrop from '../layouts/ModalStaticBackdrop';

function DataRecoveryButtons() {
  const dispatch = useDispatch();
  const [open, setOpen] = useState(false);
  const [text, setText] = useState('');
  const handleChange = (event) => {
    setText(event.target.value);
  };
  const recoverData = () => {
    const obj = JSON.parse(text);
    const { schedules } = obj;
    dispatch(setSchedules(schedules));
    alert('복구가 완료됐습니다.');
    setOpen(false);
  };
  return (
    <>
      <Button
        variant="contained"
        color="success"
        onClick={() => setOpen(true)}
      >
        데이터 복구하기 (수동)
      </Button>
      <ModalStaticBackdrop
        keepMounted
        width="xl"
        open={open}
        component={(
          <Box p={3}>
            <Typography variant="h5">복사하셨던 백업 데이터를 입력하세요</Typography>

            <Box my={3}>
              <Divider />
            </Box>

            <TextField
              fullWidth
              value={text}
              onChange={handleChange}
            />

            <Box my={3}>
              <Divider />
            </Box>
            <Stack direction="row" spacing={1}>
              <Button fullWidth variant="contained" onClick={() => setOpen(false)}>닫기</Button>
              <Button fullWidth variant="contained" onClick={() => recoverData()} color="success">복구하기</Button>
            </Stack>
          </Box>
        )}
      />
    </>
  );
}
export default DataRecoveryButtons;
