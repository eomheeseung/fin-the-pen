import { Paper } from '@mui/material';

function RoundedPaper({ children }) {
  return (
    <Paper
      elevation={8}
      sx={{
        marginY: 2, padding: 2, textAlign: 'center', borderRadius: 3,
      }}
    >
      {children}
    </Paper>
  );
}

export default RoundedPaper;
