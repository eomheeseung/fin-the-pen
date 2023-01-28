/* eslint-disable max-len */
/* eslint-disable react/jsx-props-no-spreading */
import { Box, Stack, TextField } from '@mui/material';
import { StaticDatePicker } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { PickersDay } from '@mui/x-date-pickers/PickersDay/PickersDay';
import moment from 'moment';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { grey, lightBlue, pink } from '@mui/material/colors';
import { CATEGORIES, EXPENDITURE, INCOME } from '../../../utils/constants/categories';
import {
  selectDate, selectedDate, selectSchedules, selectViewMode,
} from '../../../utils/redux/schedule/scheduleSlice';
import MarkedPickersDay from '../scheduleMarker/MarkedPickersDay';
import MarkerStack from '../scheduleMarker/MarkerStack';
import 'moment/locale/ko';
import CalenderBox from './boxes/CalenderBox';

function Calender({ dateHeight }) {
  const dispatch = useDispatch();
  const value = useSelector(selectDate);
  const schedules = useSelector(selectSchedules);
  const viewMode = useSelector(selectViewMode);
  const today = moment(new Date());

  const DATE_SIZE = 32;
  const DATE_HEIGHT = dateHeight;

  useEffect(() => {
    dispatch(selectedDate(moment(new Date())));
  }, []);

  /**
   * 해당 일의 일정을 받아 카테고리 수별로 마커위치를 고정하기 위해 새로운 배열을 생성해 반환하는 함수
   * @param {Array} daySchedules 해당 일의 일정 배열
   * @returns {Array} 카테고리별 일정 마커를 표시하기 휘한 길이 7의 배열 (색상 표시를 위해 color 요소 필수)
   */
  const makeMarkerData = (daySchedules) => {
    const emptyData = Array(6).fill().map(() => ({ color: '#FFFFFF' }));
    const categoryForMarker = INCOME.nested.concat(EXPENDITURE.nested)
      .filter((c) => (daySchedules.findIndex(
        (s) => s.category.nestedType === c.type,
      ) !== -1));

    switch (categoryForMarker.length) {
      case 1:
        return [emptyData[0], ...categoryForMarker, ...emptyData.slice(-5)];
      case 2:
        return [...emptyData.slice(-4), ...categoryForMarker, emptyData[0]];
      case 3:
        return [...categoryForMarker, ...emptyData.slice(-4)];
      case 4:
        return [...emptyData.slice(-3), ...categoryForMarker];
      case 5:
        return [...categoryForMarker.slice(0, 3), emptyData[0], ...categoryForMarker.slice(-2), emptyData[0]];
      case 6:
        return [categoryForMarker[0], emptyData[0], ...categoryForMarker.slice(1)];
      default:
        return categoryForMarker;
    }
  };

  const renderDayInPicker = (day, _value, DayComponentProps) => {
    const daySchedules = schedules.filter((e) => e.date === day.format('YYYY-MM-DD')).map((s) => ({ ...s, category: CATEGORIES.find((c) => c.title === s.category) || { type: '미분류', color: '#C8A2C8' } }));

    const fixedWithdrawal = daySchedules.filter((s) => ['고정 입출금', '미분류'].includes(s.category.type));
    const nonFixedWithdrwal = daySchedules.filter((s) => s.category.type !== '고정 입출금');

    if (fixedWithdrawal.length > 0) {
      if (nonFixedWithdrwal.length > 0) {
        const categoryForMarker = makeMarkerData(daySchedules);
        return (
          <Box sx={{ width: DATE_SIZE, marginX: 'auto' }} key={DayComponentProps.key}>
            <Stack>
              <MarkedPickersDay
                color={fixedWithdrawal[0].category.color}
                DayComponentProps={DayComponentProps}
              />
              <MarkerStack
                nonFixedWithdrwal={nonFixedWithdrwal}
                categoryForMarker={categoryForMarker}
              />
            </Stack>
          </Box>
        );
      }
      return (
        <MarkedPickersDay
          color={fixedWithdrawal[0].category.color}
          DayComponentProps={DayComponentProps}
          key={DayComponentProps.key}
        />
      );
    }

    if (nonFixedWithdrwal.length > 0) {
      const categoryForMarker = makeMarkerData(daySchedules);
      return (
        <Box sx={{ width: DATE_SIZE, marginX: 'auto' }} key={DayComponentProps.key}>
          <Stack>
            <PickersDay {...DayComponentProps} />
            <MarkerStack
              nonFixedWithdrwal={nonFixedWithdrwal}
              categoryForMarker={categoryForMarker}
            />
          </Stack>
        </Box>
      );
    }

    return <PickersDay {...DayComponentProps} />;
  };

  const renderAssetDayPicker = (day, _value, DayComponentProps) => {
    const renderDay = DayComponentProps.day;
    const weekday = renderDay.format('dd');

    // 이달에 해당하지 않은 날은 아무것도 표시하지 않기 위한 조건문
    if (!renderDay.isSame(today, 'month')) {
      return <PickersDay {...DayComponentProps} />;
    }

    // 오늘 이전의 일별 수입/지출, 주별 수입/지출을 표시하기 위한 조건문
    if (renderDay.isSameOrBefore(today)) {
      if (weekday === '일') {
        return (
          <Box sx={{ width: 'calc(100vw / 7)' }} key={DayComponentProps.key}>
            <Box sx={{ display: 'flex', justifyContent: 'center' }}>
              <PickersDay {...DayComponentProps} />
            </Box>
            <Stack mb={1}>
              <Box sx={{ fontSize: 'x-small', paddingRight: 2, color: lightBlue[200] }} display="flex" justifyContent="flex-end">-10000</Box>
              <Box sx={{ fontSize: 'x-small', paddingRight: 2, color: pink[100] }} display="flex" justifyContent="flex-end">+10000</Box>
            </Stack>
            {renderDay.isSame(today, 'week')
              ? (
                // 이번주의 주별 수입/지출 표시
                <Box sx={{
                  width: `calc(100vw / 7 * (${today.diff(renderDay, 'days')} + 1))`, background: grey[200], overflow: 'visible', borderRadius: 3, display: 'flex', justifyContent: 'flex-end', paddingX: 2,
                }}
                >
                  <Box sx={{ fontSize: 'small', paddingRight: 1, color: 'primary.main' }}>-10000</Box>
                  <Box sx={{ fontSize: 'small', color: grey[500] }}>+10000</Box>
                </Box>
              )
              : (
                // 지난주들의 주별 수입/지출 표시
                <Box sx={{
                  width: '100vw', background: grey[200], overflow: 'visible', borderRadius: 3, display: 'flex', justifyContent: 'flex-end', paddingX: 2,
                }}
                >
                  <Box sx={{ fontSize: 'small', paddingRight: 1, color: 'primary.main' }}>+10000</Box>
                  <Box sx={{ fontSize: 'small', color: grey[500] }}>-10000</Box>
                </Box>
              )}
          </Box>
        );
      }

      // 지난 일들의 일별 수입/지출액 표시
      return (
        <Box sx={{ width: 'calc(100vw / 7)' }} key={DayComponentProps.key}>
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <PickersDay {...DayComponentProps} />
          </Box>
          <Stack>
            <Box sx={{ fontSize: 'x-small', paddingRight: 2, color: lightBlue[300] }} display="flex" justifyContent="flex-end">-10000</Box>
            <Box sx={{ fontSize: 'x-small', paddingRight: 2, color: pink[200] }} display="flex" justifyContent="flex-end">+10000</Box>
          </Stack>
        </Box>
      );
    }

    // 뒷날의 일별 수입/지출액 표시
    return (
      <Box sx={{ width: 'calc(100vw / 7)' }} key={DayComponentProps.key}>
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <PickersDay {...DayComponentProps} />
        </Box>
        <Stack>
          <Box sx={{ fontSize: 'x-small', paddingRight: 2, color: grey[500] }} display="flex" justifyContent="flex-end">-10000</Box>
          <Box sx={{ fontSize: 'x-small', paddingRight: 2, color: grey[500] }} display="flex" justifyContent="flex-end">+10000</Box>
        </Stack>
      </Box>
    );
  };

  return (
    <LocalizationProvider dateAdapter={AdapterMoment} dateFormats={{ monthAndYear: 'yyyy년 MM월' }}>
      <CalenderBox dateHeight={DATE_HEIGHT} dateSize={DATE_SIZE}>
        <StaticDatePicker
          views={['day']}
          displayStaticWrapperAs="desktop"
          disableHighlightToday
          dayOfWeekFormatter={(day) => day.substring(0, 3)}
          value={value}
          onChange={(newValue) => {
            dispatch(selectedDate(moment(newValue)));
          }}
          // eslint-disable-next-line react/jsx-props-no-spreading
          renderDay={viewMode === 'schedule' ? renderDayInPicker : renderAssetDayPicker}
          renderInput={(params) => <TextField {...params} />}
        />
      </CalenderBox>
    </LocalizationProvider>
  );
}

export default Calender;

/**
 * 어떤 방식으로 작업할 지는 모르겠지만
 * 파일 명 등은 알아서 수정해주세요
 */
