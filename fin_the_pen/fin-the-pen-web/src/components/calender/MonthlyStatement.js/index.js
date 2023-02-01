/* eslint-disable max-len */
import {
  Box, Grid,
} from '@mui/material';
import { lightBlue, pink } from '@mui/material/colors';
import moment from 'moment';
import { useSelector } from 'react-redux';
import { selectDate, selectSchedules } from '../../../utils/redux/schedule/scheduleSlice';
import { calculateIncomeExpenditure } from '../../../utils/tools';
import StatementCard from './StatementCard';

function MonthlyStatement() {
  const schedules = useSelector(selectSchedules);
  const date = useSelector(selectDate);

  return (
    <Box mx={2} mb="80px">
      <Grid container spacing={2}>
        <StatementCard
          title="수입"
          value={calculateIncomeExpenditure(schedules, moment(date), 'month', '+')}
          color={pink[100]}
        />
        <StatementCard
          title="지출"
          value={calculateIncomeExpenditure(schedules, moment(date), 'month', '-')}
          color={lightBlue[200]}
        />
      </Grid>
    </Box>
  );
}

export default MonthlyStatement;
