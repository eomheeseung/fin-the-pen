import {
  Box, IconButton, Stack, Typography,
} from '@mui/material';
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import { Outlet } from 'react-router-dom';

function ManagementLayout() {
  return (
    <Box>
      <Stack direction="row" alignItems="center" justifyContent="space-between" px={2} my={2} sx={{ height: '30px' }}>
        <IconButton aria-label="delete" sx={{ padding: '5px', marginRight: '-3px' }}>
          <KeyboardArrowLeftIcon />
        </IconButton>
        <Stack alignItems="center">
          <Typography variant="h6" sx={{ fontWeight: 'bold' }}>title</Typography>
        </Stack>
        <IconButton aria-label="delete" sx={{ padding: '5px', marginLeft: '-3px' }}>
          <KeyboardArrowRightIcon />
        </IconButton>
      </Stack>

      <Box>
        <Outlet />
      </Box>
    </Box>
  );
}

export default ManagementLayout;
