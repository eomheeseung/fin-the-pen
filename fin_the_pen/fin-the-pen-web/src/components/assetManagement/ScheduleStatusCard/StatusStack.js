import { Box, Stack } from '@mui/material';

function StatusStack({ title, content }) {
  return (
    <Stack width="50%" sx={{ textAlign: 'center' }}>
      <Box>{title}</Box>
      <Box sx={{
        typography: 'h4', fontWeight: 'bold', color: 'primary.main', marginTop: 1,
      }}
      >
        {content}
      </Box>
    </Stack>
  );
}

export default StatusStack;
