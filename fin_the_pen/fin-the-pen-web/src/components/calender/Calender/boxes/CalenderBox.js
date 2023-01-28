import { Box } from '@mui/material';

function CalenderBox({ dateHeight, dateSize, children }) {
  const DATE_HEIGHT = dateHeight;
  const DATE_SIZE = dateSize;
  return (
    <Box
      sx={{
        '& > div': {
          minWidth: '100%',
        },
        '& > div > div, & > div > div > div, & .MuiCalendarPicker-root': {
          width: '100%',
          display: 'inline-flex',
          minHeight: DATE_HEIGHT * 5 + 100,
        },
        // 헤더 디자인을 위한 css
        '.MuiCalendarOrClockPicker-root > div': {
          width: '100%',
          margin: '0',
          minHeight: DATE_HEIGHT * 5 + 100,
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
      }}
    >
      {children}
    </Box>
  );
}
export default CalenderBox;
