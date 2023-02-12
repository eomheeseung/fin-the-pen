import { Box } from '@mui/material';
import { grey } from '@mui/material/colors';

function WeeklyStatment({ expenditure, income }) {
  return (
    <Box sx={{
      background: grey[200], overflow: 'visible', borderRadius: 3, display: 'flex', justifyContent: 'flex-end', paddingX: 2, height: '20px',
    }}
    >
      <Box sx={{ fontSize: 'small', paddingRight: 1, color: 'primary.main' }}>
        {`-${expenditure}`}
      </Box>
      <Box sx={{ fontSize: 'small', color: grey[500] }}>
        {`+${income}`}
      </Box>
    </Box>
  );
}

export default WeeklyStatment;
