/* eslint-disable max-len */
import {
  Box,
  Button, Grid, Paper, Stack, Typography,
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
      <Grid container>
        <Grid item xs>
          <Stack direction="row" sx={{ fontSize: 'small', display: 'flex', alignItems: 'center' }}>
            <AccessTimeIcon sx={{
              width: '10px', height: '10px', marginRight: 0.5,
            }}
            />
            <Box>{schedule.start_time}</Box>
          </Stack>
        </Grid>
        <Grid item xs={4.5} sm={3} md={2} lg={1}>
          <Stack direction="row" justifyContent="space-between">
            <Button
              variant="contained"
              size="small"
              sx={{
                borderRadius: 5, minWidth: 0, width: '20px', height: '20px',
              }}
            >
              {schedule.type}
            </Button>
            <Box sx={{ color: 'primary.main' }}>{`${parseInt(schedule.expected_spending, 10).toLocaleString('ko-KR')}원`}</Box>
          </Stack>
        </Grid>
      </Grid>
    </Paper>
  );
}

export default SpendingDetailCard;
