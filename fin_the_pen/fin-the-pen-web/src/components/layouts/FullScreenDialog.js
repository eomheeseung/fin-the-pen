/* eslint-disable react/jsx-props-no-spreading */
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import CloseIcon from '@mui/icons-material/Close';
import Slide from '@mui/material/Slide';
import { forwardRef } from 'react';
import {
  Avatar, Box, Container, CssBaseline, Stack, TextField,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';

const Transition = forwardRef((props, ref) => <Slide direction="up" ref={ref} {...props} />);

export default function FullScreenDialog({ open, setOpen, ask }) {
  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    alert(JSON.stringify({
      number: data.get('number'),
    }));
  };

  return (
    <div>
      <Dialog
        fullScreen
        open={open}
        onClose={handleClose}
        TransitionComponent={Transition}
      >
        <AppBar sx={{ position: 'relative' }}>
          <Toolbar>
            <IconButton
              edge="start"
              color="inherit"
              onClick={handleClose}
              aria-label="close"
            >
              <CloseIcon />
            </IconButton>
            <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
              임시
            </Typography>
          </Toolbar>
        </AppBar>
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Box
            sx={{
              marginTop: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Avatar sx={{
              m: 1, bgcolor: 'secondary.main', width: 100, height: 100,
            }}
            >
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              자산 정보 입력
            </Typography>
            <Typography variant="caption">
              {ask.question}
            </Typography>

            <Box component="form" onSubmit={handleSubmit} noValidate>
              <TextField
                margin="normal"
                required
                fullWidth
                id="number"
                label={ask.label}
                name="number"
                autoFocus
              />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                Continue
              </Button>
            </Box>
          </Box>
          <Stack
            direction="row"
            justifyContent="flex-end"
            alignItems="center"
            spacing={2}
            mx={4}
          >
            <Button>질문 건너뛰기</Button>
          </Stack>
        </Container>
      </Dialog>
    </div>
  );
}
