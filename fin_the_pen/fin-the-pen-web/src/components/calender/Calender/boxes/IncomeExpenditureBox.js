import { Box, Stack } from '@mui/material';

function IncomeExpenditureBox({
  income, expenditure, incomeColor, expenditureColor, pickersDay, children,
}) {
  return (
    <Box sx={{ width: 'calc(100vw / 7)' }}>
      <Box sx={{ display: 'flex', justifyContent: 'center' }}>
        {pickersDay}
      </Box>
      <Stack mb={1}>
        <Box
          sx={{
            fontSize: 'x-small', paddingRight: 1, color: expenditureColor, height: '15px',
          }}
          display="flex"
          justifyContent="flex-end"
        >
          {expenditure !== '0' && expenditure}
        </Box>
        <Box
          sx={{
            fontSize: 'x-small', paddingRight: 1, color: incomeColor, height: '15px',
          }}
          display="flex"
          justifyContent="flex-end"
        >
          {income !== '0' && income}
        </Box>
      </Stack>
      {children}
    </Box>
  );
}

export default IncomeExpenditureBox;
