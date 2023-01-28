/* eslint-disable max-len */
import {
  Box, Grid,
} from '@mui/material';
import { lightBlue, pink } from '@mui/material/colors';
import StatementCard from './StatementCard';

function MonthlyStatement() {
  return (
    <Box mx={2}>
      <Grid container spacing={2}>
        <StatementCard
          title="수입"
          value="100000원"
          color={pink[100]}
        />
        <StatementCard
          title="지출"
          value="100000원"
          color={lightBlue[200]}
        />
      </Grid>
    </Box>
  );
}

export default MonthlyStatement;
