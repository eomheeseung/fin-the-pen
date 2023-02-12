/* eslint-disable no-underscore-dangle */
/* eslint-disable react/jsx-props-no-spreading */
import { Box, TextField } from '@mui/material';
import { StaticDatePicker } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { PickersDay } from '@mui/x-date-pickers/PickersDay/PickersDay';
import moment from 'moment';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { grey, lightBlue, pink } from '@mui/material/colors';
import { CATEGORIES } from '../../../utils/constants/categories';
import {
  selectDate, selectedDate, selectSchedules, selectViewMode,
} from '../../../utils/redux/schedule/scheduleSlice';
import MarkedPickersDay from '../scheduleMarker/MarkedPickersDay';
import MarkerStack from '../scheduleMarker/MarkerStack';
import 'moment/locale/ko';
import CalenderBox from './boxes/CalenderBox';
import IncomeExpenditureBox from './boxes/IncomeExpenditureBox';
import { calculateIncomeExpenditure } from '../../../utils/tools';
import { makeMarkerData } from './utils/calender';
import WeeklyStatment from '../WeeklyStatment';

function Calender({ dateHeight }) {
  const dispatch = useDispatch();
  const value = useSelector(selectDate);
  const schedules = useSelector(selectSchedules);
  const viewMode = useSelector(selectViewMode);
  const today = moment(new Date());

  const DATE_SIZE = 25;
  const DATE_HEIGHT = dateHeight;

  useEffect(() => {
    dispatch(selectedDate(moment(new Date())));
  }, []);

  const renderDayInPicker = (day, _value, DayComponentProps) => {
    const daySchedules = schedules.filter((e) => e.date === day.format('YYYY-MM-DD')).map((s) => ({ ...s, category: CATEGORIES.find((c) => c.title === s.category) || { type: '미분류', color: '#C8A2C8' } }));

    const fixedWithdrawal = daySchedules.filter((s) => ['고정 입출금', '미분류'].includes(s.category.type));
    const nonFixedWithdrwal = daySchedules.filter((s) => s.category.type !== '고정 입출금');

    if (fixedWithdrawal.length > 0) {
      if (nonFixedWithdrwal.length > 0) {
        const categoryForMarker = makeMarkerData(daySchedules);
        return (
          <Box sx={{ width: 'calc(100vw / 7)' }} key={DayComponentProps.key}>
            <Box sx={{ display: 'flex', justifyContent: 'center' }}>
              <MarkedPickersDay
                color={fixedWithdrawal[0].category.color}
                DayComponentProps={DayComponentProps}
              />
            </Box>
            <MarkerStack
              nonFixedWithdrwal={nonFixedWithdrwal}
              categoryForMarker={categoryForMarker}
            />
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
        <Box sx={{ width: 'calc(100vw / 7)' }} key={DayComponentProps.key}>
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <PickersDay {...DayComponentProps} />
          </Box>
          <MarkerStack
            nonFixedWithdrwal={nonFixedWithdrwal}
            categoryForMarker={categoryForMarker}
          />
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
                width: `calc(100vw / 7 * (${today.diff(day, 'days')} + 1))`,
              }}
              >
                <WeeklyStatment
                  expenditure={schedules.filter((s) => day.isSameOrBefore(s.date) && day.isSame(s.date, 'week')).reduce((sum, current) => (current.type === '-' ? sum - parseInt(current.expected_spending, 10) : sum), 0)}
                  income={schedules.filter((s) => day.isSameOrBefore(s.date) && day.isSame(s.date, 'week')).reduce((sum, current) => (current.type === '+' ? sum + parseInt(current.expected_spending, 10) : sum), 0)}
                />
              </Box>
            )
            : (
          // 지난주들의 주별 수입/지출 표시
              <Box sx={{
                width: '100vw',
              }}
              >
                {/* <Box sx={{ fontSize: 'small', paddingRight: 1, color: 'primary.main' }}>
                  {calculateIncomeExpenditure(schedules, day, 'week', '-')}
                </Box>
                <Box sx={{ fontSize: 'small', color: grey[500] }}>
                  {calculateIncomeExpenditure(schedules, day, 'week', '+')}
                </Box> */}
                <WeeklyStatment
                  expenditure={calculateIncomeExpenditure(schedules, day, 'week', '-')}
                  income={calculateIncomeExpenditure(schedules, day, 'week', '+')}
                />
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
