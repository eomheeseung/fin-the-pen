import { Box } from '@mui/material';

function RoundedBorderBox({ children }) {
  return (
    <Box sx={{
      p: 2, border: '2px solid', borderRadius: 2, borderColor: 'primary.main',
    }}
    >
      {children}
    </Box>
  );
}

export default RoundedBorderBox;
