import {
  Accordion, AccordionDetails, AccordionSummary, Box, Button, Divider, Stack, TextField, Typography,
} from '@mui/material';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
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
            <Accordion>
              <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography>민하님이 1.22에 제공해주신 데이터</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <Typography>
                  {JSON.stringify({
                    schedules: [{
                      event_name: '카드 대금 ',
                      alarm: false,
                      date: '2023-01-01',
                      start_time: '09:00',
                      end_time: '11:00',
                      repeating_cycle: '없음',
                      repeat_deadline: '없음',
                      repeat_endDate: '2023-01-01',
                      category: '카드이체료',
                      type: '-',
                      expected_spending: '600000',
                      importance: '상',
                      exclusion: false,
                      id: '15d754bd-b169-46f9-82c8-be25c09b9c04',
                    }, {
                      event_name: '길동이랑 저녁식사 ',
                      alarm: false,
                      date: '2023-01-02',
                      start_time: '18:00',
                      end_time: '19:00',
                      repeating_cycle: '없음',
                      repeat_deadline: '없음',
                      repeat_endDate: '2023-01-02',
                      category: '식비',
                      type: '-',
                      expected_spending: '15000',
                      importance: '중',
                      exclusion: false,
                      id: '0063d15c-9dca-4357-8bf3-db3d0f1c65f4',
                    }, {
                      event_name: '파마 예약 ',
                      alarm: false,
                      date: '2023-01-04',
                      start_time: '15:00',
                      end_time: '17:00',
                      repeating_cycle: '없음',
                      repeat_deadline: '없음',
                      repeat_endDate: '2023-01-04',
                      category: '미용',
                      type: '-',
                      expected_spending: '100000',
                      importance: '중',
                      exclusion: false,
                      id: '73129bbf-387d-495e-a4b0-d583a33b4c20',
                    }, {
                      event_name: '올리브영에서 틴트사기 ',
                      alarm: false,
                      date: '2023-01-05',
                      start_time: '09:00',
                      end_time: '11:00',
                      repeating_cycle: '없음',
                      repeat_deadline: '없음',
                      repeat_endDate: '2023-01-05',
                      category: '미용',
                      type: '-',
                      expected_spending: '15000',
                      importance: '중',
                      exclusion: false,
                      id: '5b63fe2c-981b-452e-9b76-a4ae637d486d',
                    }, {
                      event_name: '호걸이랑 영화보기 ',
                      alarm: false,
                      date: '2023-01-12',
                      start_time: '09:00',
                      end_time: '11:00',
                      repeating_cycle: '없음',
                      repeat_deadline: '없음',
                      repeat_endDate: '2023-01-12',
                      category: '영화/전시',
                      type: '-',
                      expected_spending: '15000',
                      importance: '중',
                      exclusion: false,
                      id: 'c4e39156-a59c-45e4-91d2-d5a96e109ffd',
                    }, {
                      event_name: '미미랑 도너츠 먹으러가기 ',
                      alarm: false,
                      date: '2023-01-12',
                      start_time: '09:00',
                      end_time: '11:00',
                      repeating_cycle: '없음',
                      repeat_deadline: '없음',
                      repeat_endDate: '2023-01-12',
                      category: '카페',
                      type: '-',
                      expected_spending: '10000',
                      importance: '중',
                      exclusion: false,
                      id: '0278d711-7fed-46ae-9421-839b80da9b5d',
                    }],
                    settings: {
                      앱비밀번호: false, 인증단계: 1, 화면테마: 'default', 예산숨김: false, 반복일정목록: [], 숨김일정: false, 숨김일정목록: false, 비밀번호인증단계: 1, 기본알림: false, 진동: false, 소리: false, 알림음: '', 잠금화면알람: false, 일정알림시간: '5분전', 소비주의알림: false, 정기입출금목록: [], 마이데이터: [], 캘린더목록: [],
                    },
                  })}
                </Typography>
              </AccordionDetails>
            </Accordion>

            <Box my={3}>
              <Divider />
            </Box>

            <Stack direction="row" spacing={1}>
              <Button fullWidth variant="contained" onClick={() => setOpen(false)} color="error">닫기</Button>
              <Button fullWidth variant="contained" onClick={() => recoverData()} color="success">복구하기</Button>
            </Stack>
          </Box>
        )}
      />
    </>
  );
}
export default DataRecoveryButtons;
