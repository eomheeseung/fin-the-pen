/* eslint-disable react/jsx-props-no-spreading */
import {
  // eslint-disable-next-line max-len
  Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select, Stack, TextField,
} from '@mui/material';
import { LocalizationProvider, PickersDay, StaticDatePicker } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import moment from 'moment';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { DEADLINE, REPEAT } from '../../../../utils/constants/repeat';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';
import { selectSchedule, setDrawerSchedule } from '../../../../utils/redux/schedule/scheduleSlice';
import { updateRepeat, updateRepeatEndDate } from '../utils/schedule';

function RepeatInput() {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);

  const [openDatePickerModal, setOpenDatePickerModal] = useState(false);
  const [repeatEndDate, setRepeatEndDate] = useState(moment(schedule.repeat_endDate));

  const changeRepeat = (state) => {
    updateRepeat(dispatch, schedule, setOpenDatePickerModal, state);
  };

  const handleModalClose = () => {
    setOpenDatePickerModal(false);
    dispatch(setDrawerSchedule({
      ...schedule,
      repeat_endDate: moment(repeatEndDate).format('YYYY-MM-DD'),
    }));
  };

  const renderDayInPicker = (day, _value, DayComponentProps) => {
    if (moment(schedule.date).isSame(repeatEndDate)) {
      return <PickersDay {...DayComponentProps} />;
    }
    if (moment(schedule.date).isSame(day)) {
      return (
        <PickersDay
          sx={{
            borderTopRightRadius: 0, borderBottomRightRadius: 0, marginX: 0, width: '40px',
          }}
          className="Mui-selected"
          {...DayComponentProps}
        />
      );
    }
    if (moment(repeatEndDate).isSame(day)) {
      return (
        <PickersDay
          sx={{
            borderTopLeftRadius: 0, borderBottomLeftRadius: 0, marginX: 0, width: '40px',
          }}
          className="Mui-selected"
          {...DayComponentProps}
        />
      );
    }
    if (moment(schedule.date).isBefore(day) && moment(repeatEndDate).isAfter(day)) {
      return (
        <PickersDay
          sx={{
            borderRadius: 0, marginX: 0, width: '40px',
          }}
          className="Mui-selected"
          {...DayComponentProps}
        />
      );
    }
    return <PickersDay {...DayComponentProps} />;
  };

  return (
    <Box>
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        spacing={2}
        sx={{ width: '100%', marginTop: 1 }}
      >
        <FormControl fullWidth size="small">
          <InputLabel>{SCHEDULE_DRAWER.repeating_cycle}</InputLabel>
          <Select
            name="repeating_cycle"
            value={schedule.repeating_cycle}
            label={SCHEDULE_DRAWER.repeating_cycle}
            onChange={changeRepeat}
          >
            {REPEAT.map((r) => (<MenuItem id="repeating_cycle" key={Math.random()} value={r}>{r}</MenuItem>))}
          </Select>
        </FormControl>

        <FormControl
          fullWidth
          size="small"
          disabled={schedule.repeating_cycle === '없음'}
        >
          <InputLabel>{SCHEDULE_DRAWER.repeat_deadline}</InputLabel>
          <Select
            name="repeat_deadline"
            value={schedule.repeat_deadline === '캘린더에 표시' ? schedule.repeat_endDate : schedule.repeat_deadline}
            label={SCHEDULE_DRAWER.repeat_deadline}
            onChange={changeRepeat}
          >
            {DEADLINE.map((d) => (<MenuItem key={Math.random()} value={d}>{d}</MenuItem>))}
            <MenuItem disabled value={schedule.repeat_endDate}>{schedule.repeat_endDate}</MenuItem>
          </Select>
        </FormControl>
      </Stack>

      <Dialog
        open={openDatePickerModal}
        onClose={handleModalClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        sx={{
          '.MuiDialogContent-root': {
            padding: 0,
          },
        }}
      >
        <DialogTitle id="alert-dialog-title">
          날짜 선택
        </DialogTitle>
        <DialogContent>
          <LocalizationProvider dateAdapter={AdapterMoment}>
            <StaticDatePicker
              views={['day']}
              displayStaticWrapperAs="desktop"
              disableHighlightToday
              value={moment(repeatEndDate)}
              onChange={(newValue) => {
                updateRepeatEndDate(schedule, setRepeatEndDate, newValue);
              }}
              renderDay={renderDayInPicker}
              renderInput={(params) => <TextField {...params} />}
            />
          </LocalizationProvider>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleModalClose} autoFocus>
            설정
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default RepeatInput;
