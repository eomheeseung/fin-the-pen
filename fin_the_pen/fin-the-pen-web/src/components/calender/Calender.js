/* eslint-disable react/jsx-props-no-spreading */
import { Box, Stack, TextField } from '@mui/material';
import { grey } from '@mui/material/colors';
import { StaticDatePicker } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { PickersDay } from '@mui/x-date-pickers/PickersDay/PickersDay';
import moment from 'moment';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { EXPENDITURE, INCOME } from '../../utils/constants/categories';
import {
  selectDate, selectedDate, selectSchedules, selectViewMode,
} from '../../utils/redux/schedule/scheduleSlice';
import MarkedPickersDay from './scheduleMarker/MarkedPickersDay';
import MarkerStack from './scheduleMarker/MarkerStack';
import 'moment/locale/ko';

function Calender({ dateHeight }) {
  const dispatch = useDispatch();
  const value = useSelector(selectDate);
  const schedules = useSelector(selectSchedules);
  const viewMode = useSelector(selectViewMode);

  const DATE_SIZE = 32;
  const DATE_HEIGHT = dateHeight;

  useEffect(() => {
    dispatch(selectedDate(moment(new Date())));
  }, []);

  const renderDayInPicker = (day, _value, DayComponentProps) => {
    const daySchedules = schedules.filter((e) => e.date === day.format('YYYY-MM-DD'));

    const fixedWithdrawal = daySchedules.filter((s) => s.category.type === '고정 입출금');
    const nonFixedWithdrwal = daySchedules.filter((s) => s.category.type !== '고정 입출금');

    if (fixedWithdrawal.length > 0) {
      if (nonFixedWithdrwal.length > 0) {
        const categoryForMarker = INCOME.nested.concat(EXPENDITURE.nested)
          .map((c) => (daySchedules.findIndex(
            (s) => s.category.nestedType === c.type,
          ) === -1
            ? { ...c, color: '#FFFFFF' }
            : c
          ));
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
      const categoryForMarker = INCOME.nested.concat(EXPENDITURE.nested)
        .map((c) => (daySchedules.findIndex(
          (s) => s.category.nestedType === c.type,
        ) === -1
          ? { ...c, color: '#FFFFFF' }
          : c
        ));
      return (
        <Box sx={{ width: DATE_SIZE, marginX: 'auto' }} key={DayComponentProps.key}>
          <Stack>
            <PickersDay sx={{ marginBottom: 2 }} {...DayComponentProps} />
            <MarkerStack
              nonFixedWithdrwal={nonFixedWithdrwal}
              categoryForMarker={categoryForMarker}
            />
          </Stack>
        </Box>
      );
    }

    return <PickersDay sx={{ marginBottom: 2 }} {...DayComponentProps} />;
  };

  // eslint-disable-next-line arrow-body-style
  const renderAssetDayPicker = (day, _value, DayComponentProps) => {
    // 렌더링 방식은 추후 수정 얘정
    return <PickersDay sx={{ marginBottom: 2 }} {...DayComponentProps} />;
  };

  return (
    // eslint-disable-next-line max-len
    <LocalizationProvider dateAdapter={AdapterMoment} dateFormats={{ monthAndYear: 'yyyy년 MM월' }}>
      <Box
        sx={{
          '& > div': {
            minWidth: '100%',
          },
          '& > div > div, & > div > div > div, & .MuiCalendarPicker-root': {
            width: '100%',
            display: 'inline-flex',
            minHeight: DATE_HEIGHT * 6,
          },
          // 헤더 디자인을 위한 css
          '.MuiCalendarOrClockPicker-root > div': {
            width: '100%',
            margin: '0',
            minHeight: DATE_HEIGHT * 6,
          },
          '.MuiPickersCalendarHeader-root': {
            display: 'flex',
            width: '100vw',
            paddingX: 2,
          },
          '.MuiPickersArrowSwitcher-root': {
            width: '100vw',
            display: 'inline-flex',
            justifyContent: 'space-between',
          },
          '.MuiPickersCalendarHeader-label': {
            textAlign: 'center',
          },
          '.MuiPickersArrowSwitcher-spacer': {
            width: '50vw',
          },
          '.MuiPickersCalendarHeader-labelContainer': {
            position: 'absolute',
            left: 0,
            right: 0,
            alignItems: 'center',
            justifyContent: 'center',
          },

          // 데이 디자인을 위한 css
          '& .MuiTypography-caption': {
            width: '100%',
            margin: 0,
          },
          '& .PrivatePickersSlideTransition-root': {
            minHeight: DATE_HEIGHT * 5,
          },
          '& .PrivatePickersSlideTransition-root [role="row"]': {
            margin: 0,
          },
          '& .MuiPickersDay-dayWithMargin': {
            margin: 0,
          },
          '& .MuiDayPicker-weekContainer': {
            height: DATE_HEIGHT,
          },
          '& .MuiPickersDay-root': {
            width: DATE_SIZE,
            height: DATE_SIZE,
            marginX: 'auto',
          },
          '& .Mui-selected': {
            borderRadius: 2,
            backgroundColor: 'primary.main',
            color: 'white',
            fontWeight: 'bold',
          },
          '& [aria-current="date"]': {
            borderRadius: 2,
            backgroundColor: grey[100],
          },
        }}
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
          // eslint-disable-next-line react/jsx-props-no-spreading
          renderDay={viewMode === 'schedule' ? renderDayInPicker : renderAssetDayPicker}
          renderInput={(params) => <TextField {...params} />}
        />
      </Box>
    </LocalizationProvider>
  );
}

export default Calender;

/**
 * 어떤 방식으로 작업할 지는 모르겠지만
 * 파일 명 등은 알아서 수정해주세요
 */
