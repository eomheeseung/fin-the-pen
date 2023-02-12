import {
  Stack, Typography,
} from '@mui/material';
import moment from 'moment';
import { useSelector } from 'react-redux';
import { selectDate, selectSchedules } from '../../../utils/redux/schedule/scheduleSlice';
import { calculateIncomeExpenditure } from '../../../utils/tools';
import StatusStack from '../../assetManagement/ScheduleStatusCard/StatusStack';
import SwitchingHeader from '../../common/SwitchingHeader';
import RoundedPaper from '../../common/RoundedPaper';
import RoundedBorderBox from '../../common/RoundedBorderBox';

function MonthlyStatement() {
  const schedules = useSelector(selectSchedules);
  const date = useSelector(selectDate);

  return (
    <RoundedPaper>
      <SwitchingHeader>
        <Typography variant="h6" sx={{ fontWeight: 'bold' }}>{`${moment(date).format('M')}월`}</Typography>
        <Typography variant="caption">{moment(date).format('YYYY')}</Typography>
      </SwitchingHeader>

      <RoundedBorderBox>
        <Stack direction="row" spacing={2}>
          <StatusStack
            title="수입"
            content={`+${calculateIncomeExpenditure(schedules, moment(date), 'month', '+')}`}
          />

          <StatusStack
            title="지출"
            content={`-${calculateIncomeExpenditure(schedules, moment(date), 'month', '-')}`}
          />
        </Stack>
      </RoundedBorderBox>
    </RoundedPaper>
  );
}

export default MonthlyStatement;
