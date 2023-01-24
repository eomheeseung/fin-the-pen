/* eslint-disable max-len */
import {
  Box,
  Button, Paper, Stack, Typography,
} from '@mui/material';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import moment from 'moment';

function SpendingDetailCard({ schedule, bgColor }) {
  return (
    <Paper sx={{
      marginY: 1, paddingY: 2, paddingX: 3, borderRadius: 3, borderLeft: 12, borderLeftColor: bgColor,
    }}
    >
      <Stack direction="row" justifyContent="space-between">
        <Typography sx={{ fontWeight: 'bold' }}>{moment(schedule.date).format('MM월 DD일')}</Typography>
        <Typography sx={{ fontWeight: 'bold' }}>{schedule.event_name}</Typography>
      </Stack>
      <Stack direction="row" justifyContent="space-between">
        <Box sx={{ fontSize: 'small', display: 'flex', alignItems: 'center' }}>
          <AccessTimeIcon sx={{
            width: '10px', height: '10px', marginRight: 0.5, verticalAlign: 'baseline',
          }}
          />
          {schedule.start_time}
        </Box>
        <Box sx={{ color: 'primary.main' }}>
          <Button
            variant="contained"
            size="small"
            sx={{
              borderRadius: 5, minWidth: 0, width: '20px', height: '20px', marginRight: 2,
            }}
          >
            {schedule.type}
          </Button>
          {schedule.expected_spending}
          원
        </Box>
      </Stack>
    </Paper>
  );
}

export default SpendingDetailCard;
