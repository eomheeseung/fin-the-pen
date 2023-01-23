import { Stack, Typography } from '@mui/material';

function TestBox({ title, children }) {
  return (
    <Stack spacing={2} m={2} border={1} p={2}>
      <Typography>{title}</Typography>
      <Typography sx={{ wordBreak: 'break-all' }}>
        {children}
      </Typography>
    </Stack>
  );
}
export default TestBox;
