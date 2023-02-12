import {
  Accordion, AccordionDetails, AccordionSummary, Alert,
  Box, Slide, Snackbar, Stack, Typography,
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import NameInput from './inputs/NameInput';
import DateInput from './inputs/DateInput';
import {
  SCHEDULE_DRAWER, SCHEDULE_DRAWER_MODE,
} from '../../../utils/constants/schedule';
import {
  selectSchedule, setDrawerSchedule,
} from '../../../utils/redux/schedule/scheduleSlice';
import CategoryInput from './inputs/CategoryInput';
import ALERTS from '../../../utils/constants/alerts';
import RepeatInput from './inputs/RepeatInput';
import AssetSettings from './inputs/AssetSettings';
import ScheduleDrawerHeader from './layouts/ScheduleDrawerHeader';
import ScheduleDrawerFooter from './layouts/ScheduleDrawerFooter';

function TransitionUp(props) {
  // eslint-disable-next-line react/jsx-props-no-spreading
  return <Slide {...props} direction="right" />;
}

function ScheduleDrawer({
  setDrawerWidth, setBottomDrawerOpen, data, mode,
}) {
  // 추후 삭제 예정
  const random = Math.floor((Math.random() * 5));

  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);

  const [expandAccordion, setExpandAccordion] = useState(mode !== SCHEDULE_DRAWER_MODE.생성);
  const [snackbarOpen, setSnackbarOpen] = useState(true);

  const handleExpand = () => {
    setExpandAccordion(!expandAccordion);
  };

  useEffect(() => {
    if (data) {
      dispatch(setDrawerSchedule(data));
    }
  }, []);

  const ref = useRef(null);
  useEffect(() => {
    // 현재 버그 있음
    console.log('width', ref.current ? ref.current.offsetWidth : 0);
    setDrawerWidth(ref.current ? ref.current.offsetWidth : 0);
  }, [ref.current]);

  return (
    <div ref={ref}>
      <Box>
        {
        schedule && (
          <Box>
            <Snackbar
              anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
              autoHideDuration={5000}
              open={snackbarOpen}
              onClose={() => {
                setSnackbarOpen(false);
              }}
              TransitionComponent={TransitionUp}
            >
              <Alert color={ALERTS[random].color} sx={{ width: '100%' }} icon={ALERTS[random].icon}>
                {ALERTS[random].message}
              </Alert>
            </Snackbar>
            <Stack
              justifyContent="space-between"
              spacing={2}
              m={1}
            >
              <ScheduleDrawerHeader
                mode={mode}
                setBottomDrawerOpen={setBottomDrawerOpen}
              />

              {/* 이벤트 제목 */}
              <NameInput />

              {/* 이벤트 일정 */}
              <DateInput />

              {/* 이벤트 반복 설정 */}
              <RepeatInput />

              {/* 이벤트 카테고리 */}
              <CategoryInput
                selected={
                  mode === SCHEDULE_DRAWER_MODE.생성 ? '' : schedule.category
                }
              />

              {/* 자산 설정하기 */}
              {mode === SCHEDULE_DRAWER_MODE.수정
                ? (
                  <AssetSettings mode={mode} />
                )
                : (
                  <Accordion sx={{ width: '100%' }} expanded={expandAccordion}>
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon />}
                      onClick={() => handleExpand()}
                      aria-controls="panel1a-content"
                      id="panel1a-header"
                    >
                      <Typography sx={{ fontWeight: 'bold' }}>{SCHEDULE_DRAWER.set_finance_title}</Typography>
                    </AccordionSummary>
                    <AccordionDetails sx={{ backgroundColor: '#F6F6F6' }}>
                      <AssetSettings mode={mode} />
                    </AccordionDetails>
                  </Accordion>
                )}

              {/* 제출 버튼 */}
              <ScheduleDrawerFooter
                mode={mode}
                setBottomDrawerOpen={setBottomDrawerOpen}
              />
            </Stack>
          </Box>
        )
      }
      </Box>
    </div>
  );
}
export default ScheduleDrawer;
