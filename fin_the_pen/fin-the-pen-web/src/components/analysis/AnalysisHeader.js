/* eslint-disable react/jsx-one-expression-per-line */
import { IconButton, Stack, Typography } from '@mui/material';
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import moment from 'moment';

function AnalysisHeader() {
  const today = moment(new Date()).format('YYYY년MM월DD일');
  return (
    <Stack direction="row" alignItems="center" justifyContent="space-between" px={2} my={2} sx={{ height: '30px' }}>
      <IconButton aria-label="delete" sx={{ padding: '5px', marginRight: '-3px' }}>
        <KeyboardArrowLeftIcon />
      </IconButton>
      <Stack alignItems="center">
        <Typography variant="h6" sx={{ fontWeight: 'bold' }}>카테고리별 소비 현황</Typography>
        <Typography variant="caption">{today} 기준</Typography>
      </Stack>
      <IconButton aria-label="delete" sx={{ padding: '5px', marginLeft: '-3px' }}>
        <KeyboardArrowRightIcon />
      </IconButton>
    </Stack>
  );
}

export default AnalysisHeader;
