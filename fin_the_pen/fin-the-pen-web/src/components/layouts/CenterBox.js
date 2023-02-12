import { Box } from '@mui/material';

function CenterBox({ children }) {
  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
    >
      {children}
    </Box>
  );
}
export default CenterBox;
