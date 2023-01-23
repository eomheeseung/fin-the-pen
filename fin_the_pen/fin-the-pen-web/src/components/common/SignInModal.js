import ClearIcon from '@mui/icons-material/Clear';
import {
  Box, Button, DialogContent, DialogTitle, IconButton,
  Stack, Typography,
} from '@mui/material';
import SignInContainer from '../../containers/sign/SignInContainer';

function SignInModal({ setOpen }) {
  return (
    <Box
      sx={{
        p: {
          xs: 1,
          sm: 2,
          md: 3,
          lg: 4,
          xl: 5,
          xxl: 6,
        },
      }}
    >
      <DialogTitle id="scroll-dialog-title">
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h4">
            로그인
          </Typography>
          <IconButton
            size="large"
            onClick={() => {
              setOpen(false);
            }}
          >
            <ClearIcon fontSize="inherit" />
          </IconButton>
        </Box>
      </DialogTitle>
      <DialogContent dividers>
        <SignInContainer setOpen={setOpen} />
      </DialogContent>
      <Stack direction="row" spacing={1}>
        <Button
          variant="contained"
          color="error"
          fullWidth
          onClick={() => setOpen(false)}
        >
          닫기
        </Button>
      </Stack>
    </Box>
  );
}

export default SignInModal;
