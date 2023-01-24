/* eslint-disable react/no-unescaped-entities */
import { Stack, Typography } from '@mui/material';
import moment from 'moment';

function AssetManagement({ selectedItem }) {
  return (
    <Stack
      justifyContent="center"
      sx={{
        borderRadius: 3, backgroundColor: 'primary.main', padding: 3, marginBottom: 2, color: 'white', textAlign: 'center',
      }}
    >
      <Typography variant="buttontext">
        {moment(new Date()).format('M월 D일')}
        {' '}
        기준
        {' '}
        {selectedItem.label}
        {' '}
        지출 내역 총
        {' '}
        {selectedItem.history.length}
        건 (
        {selectedItem.value.toLocaleString('ko-KR')}
        원)
      </Typography>
      <Typography variant="buttontext">
        '
        {selectedItem.label}
        '
        {' '}
        카테고리 소비 목표액인 100,000의 25%
      </Typography>
    </Stack>
  );
}

export default AssetManagement;
