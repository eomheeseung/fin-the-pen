import { Box, Typography } from '@mui/material';
import moment from 'moment';
import SwitchingHeader from '../common/SwitchingHeader';

function AnalysisHeader() {
  const today = moment(new Date()).format('YYYY년 M월');
  return (
    <Box sx={{ px: 2 }}>
      <SwitchingHeader>
        <Typography variant="h6" sx={{ fontWeight: 'bold' }}>카테고리별 소비 현황</Typography>
        <Typography variant="caption">{`${today} 기준`}</Typography>
      </SwitchingHeader>
    </Box>
  );
}

export default AnalysisHeader;
