import {
  Button, Paper, Stack, Typography,
} from '@mui/material';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import moment from 'moment';

function SpendingDetailCard({ schedule }) {
  return (
    <Paper sx={{ marginY: 1, paddingY: 2, paddingX: 3 }}>
      <Stack direction="row" justifyContent="space-between">
        <Typography>{moment(schedule.date).format('MM월 DD일')}</Typography>
        <Typography>{schedule.event_name}</Typography>
      </Stack>
      <Stack direction="row" justifyContent="space-between">
        <Typography variant="caption">
          <AccessTimeIcon sx={{ width: '10px', height: '10px', marginRight: 0.5 }} />
          {schedule.start_time}
        </Typography>
        <Typography>
          <Button
            variant="contained"
            size="small"
            sx={{
              borderRadius: 5, minWidth: 0, width: '20px', height: '20px', marginRight: 0.5,
            }}
          >
            {schedule.type}
          </Button>
          {schedule.expected_spending}
        </Typography>
      </Stack>
    </Paper>
  );
}

export default SpendingDetailCard;
