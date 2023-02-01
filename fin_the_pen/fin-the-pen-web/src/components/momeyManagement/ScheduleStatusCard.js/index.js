import { Box, Stack } from '@mui/material';
import StatusStack from './StatusStack';

function ScheduleStatusCard({ month, numberOfSchedule }) {
  return (
    <Box sx={{
      marginTop: 3, border: '2px solid', borderRadius: 2, borderColor: 'primary.main',
    }}
    >
      <Stack direction="row">
        <StatusStack
          title={`${month} 남은 일정`}
          content={`${numberOfSchedule}개`}
        />

        <StatusStack
          title="추천 소비 금액"
          content="xxxxx원"
        />
      </Stack>
    </Box>
  );
}

export default ScheduleStatusCard;
