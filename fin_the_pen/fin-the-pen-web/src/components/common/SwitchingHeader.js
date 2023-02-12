import { IconButton, Stack } from '@mui/material';
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';

function SwitchingHeader({ children, handleClickLeftArrow, handleClickRightArrow }) {
  return (
    <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ height: '100%' }}>
      <IconButton
        aria-label="delete"
        sx={{
          padding: '5px', marginRight: '-3px', border: '1px solid', borderRadius: 2,
        }}
        onClick={handleClickLeftArrow}
      >
        <KeyboardArrowLeftIcon />
      </IconButton>
      <Stack alignItems="center">
        {children}
      </Stack>
      <IconButton
        aria-label="delete"
        sx={{
          padding: '5px', marginLeft: '-3px', border: '1px solid', borderRadius: 2,
        }}
        onClick={handleClickRightArrow}
      >
        <KeyboardArrowRightIcon />
      </IconButton>
    </Stack>
  );
}

export default SwitchingHeader;
