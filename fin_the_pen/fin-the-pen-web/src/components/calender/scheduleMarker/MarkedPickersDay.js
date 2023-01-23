/* eslint-disable react/jsx-props-no-spreading */
import { PickersDay } from '@mui/x-date-pickers';

function MarkedPickersDay({ color, DayComponentProps }) {
  return (
    <PickersDay
      sx={{
        border: 1,
        borderRadius: 2,
        borderColor: color,
      }}
      {...DayComponentProps}
    />
  );
}

export default MarkedPickersDay;
