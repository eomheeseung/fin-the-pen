import {
  Box, Button, IconButton, Stack,
} from '@mui/material';
import BorderColorIcon from '@mui/icons-material/BorderColor';

function Title({ type, title }) {
  return (
    <Stack direction="row" justifyContent="space-between" alignItems="center">
      <Stack direction="row">
        <Button
          variant="contained"
          size="small"
          sx={{
            borderRadius: 5, minWidth: 0, width: '20px', height: '20px', marginY: 'auto', mr: 1,
          }}
        >
          {type}
        </Button>
        <Box sx={{ typography: 'h6', fontWeight: 'bold' }}>{title}</Box>
      </Stack>
      <Stack direction="row" alignItems="center" sx={{ color: 'primary.main' }}>
        <Box>총 n건</Box>
        <IconButton color="primary" onClick={() => console.log('정기 입금')}>
          <BorderColorIcon fontSize="small" />
        </IconButton>
      </Stack>
    </Stack>
  );
}

export default Title;
