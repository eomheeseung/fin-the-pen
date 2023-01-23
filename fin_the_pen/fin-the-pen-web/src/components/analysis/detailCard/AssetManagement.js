import { Stack, Typography } from '@mui/material';

function AssetManagement() {
  return (
    <Stack
      justifyContent="center"
      sx={{
        borderRadius: 3, backgroundColor: 'primary.main', padding: 2, marginBottom: 2,
      }}
    >
      <Typography variant="buttontext">1월 22일 기준 카페 지출 내역 총 4견 (25,000원)</Typography>
      <Typography variant="buttontext">
        {'\'카페\''}
        {' '}
        카테고리 소비 목표액인 100,000의 25%
      </Typography>
    </Stack>
  );
}

export default AssetManagement;
