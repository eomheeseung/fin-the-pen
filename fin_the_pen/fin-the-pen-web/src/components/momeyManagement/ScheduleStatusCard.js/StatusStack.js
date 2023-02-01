import { Box, Stack } from '@mui/material';

function StatusStack({ title, content }) {
  return (
    <Stack width="50%" sx={{ textAlign: 'center', paddingY: 2 }}>
      <Box>{title}</Box>
      <Box sx={{
        typography: 'h4', fontWeight: 'bold', color: 'primary.main', marginTop: 2,
      }}
      >
        {content}
      </Box>
    </Stack>
  );
}

export default StatusStack;
