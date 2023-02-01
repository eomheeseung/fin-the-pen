/* eslint-disable no-underscore-dangle */
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
import IncomeExpenditureBox from './boxes/IncomeExpenditureBox';
import { calculateIncomeExpenditure } from '../../../utils/tools';

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

  // 실제 지출 데이터를 불러오기 전이기 때문에 일정 데이터의 지출 데이터 사용중
  const renderAssetDayPicker = (day, _value, DayComponentProps) => {
    const weekday = day.format('dd');
    const isSameOrBefore = day.isSameOrBefore(today);

    // 오늘이 이달의 마지막 주에 해당하고 후달이 일요일로 시작하지 않은 경우 회색바가 그려지는 문제를 해결하기 위한 코드
    if (!day.isSame(value, 'month') && today.isBefore(value)) {
      return <PickersDay {...DayComponentProps} />;
    }

    // 오늘 이전의 일별 수입/지출, 주별 수입/지출을 표시하기 위한 조건문
    if (isSameOrBefore && weekday === '일') {
      const income = !day.isSame(value, 'month') ? '0' : calculateIncomeExpenditure(schedules, day, 'day', '+');
      const expenditure = !day.isSame(value, 'month') ? '0' : calculateIncomeExpenditure(schedules, day, 'day', '-');

      return (
        <IncomeExpenditureBox
          key={DayComponentProps.key}
          income={income}
          expenditure={expenditure}
          incomeColor={pink[100]}
          expenditureColor={lightBlue[200]}
          pickersDay={<PickersDay {...DayComponentProps} />}
        >
          {day.isSame(today, 'week')
            ? (
          // 이번주의 주별 수입/지출 표시
              <Box sx={{
                width: `calc(100vw / 7 * (${today.diff(day, 'days')} + 1))`, background: grey[200], overflow: 'visible', borderRadius: 3, display: 'flex', justifyContent: 'flex-end', paddingX: 2, height: '20px',
              }}
              >
                <Box sx={{ fontSize: 'small', paddingRight: 1, color: 'primary.main' }}>
                  {schedules.filter((s) => day.isSameOrBefore(s.date) && day.isSame(s.date, 'week')).reduce((sum, current) => (current.type === '-' ? sum - parseInt(current.expected_spending, 10) : sum), 0)}
                </Box>
                <Box sx={{ fontSize: 'small', color: grey[500] }}>
                  {schedules.filter((s) => day.isSameOrBefore(s.date) && day.isSame(s.date, 'week')).reduce((sum, current) => (current.type === '+' ? sum + parseInt(current.expected_spending, 10) : sum), 0)}
                </Box>
              </Box>
            )
            : (
          // 지난주들의 주별 수입/지출 표시
              <Box sx={{
                width: '100vw', background: grey[200], overflow: 'visible', borderRadius: 3, display: 'flex', justifyContent: 'flex-end', paddingX: 2, height: '20px',
              }}
              >
                <Box sx={{ fontSize: 'small', paddingRight: 1, color: 'primary.main' }}>
                  {calculateIncomeExpenditure(schedules, day, 'week', '-')}
                </Box>
                <Box sx={{ fontSize: 'small', color: grey[500] }}>
                  {calculateIncomeExpenditure(schedules, day, 'week', '+')}
                </Box>
              </Box>
            )}
        </IncomeExpenditureBox>
      );
    }

    // 뒷날의 일별 수입/지출액 표시
    return (
      <IncomeExpenditureBox
        key={DayComponentProps.key}
        income={calculateIncomeExpenditure(schedules, day, 'day', '+')}
        expenditure={calculateIncomeExpenditure(schedules, day, 'day', '-')}
        incomeColor={isSameOrBefore ? pink[100] : grey[500]}
        expenditureColor={isSameOrBefore ? lightBlue[200] : grey[500]}
        pickersDay={<PickersDay {...DayComponentProps} />}
      />
    );
  };

  return (
    <LocalizationProvider dateAdapter={AdapterMoment} dateFormats={{ monthAndYear: 'yyyy년 MM월' }}>
      <CalenderBox
        dateHeight={DATE_HEIGHT}
        dateSize={DATE_SIZE}
        // 달력 높이 계산을 위한 해당 월의 주수 계산
        week={moment(value).endOf('month').week() - moment(value).startOf('month').week() + 1}
      >
        <StaticDatePicker
          views={['day']}
          displayStaticWrapperAs="desktop"
          disableHighlightToday
          dayOfWeekFormatter={(day) => day.substring(0, 3)}
          value={value}
          onChange={(newValue) => {
            dispatch(selectedDate(moment(newValue)));
          }}
          onMonthChange={(month) => {
            dispatch(selectedDate(moment(month._d)));
          }}
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
