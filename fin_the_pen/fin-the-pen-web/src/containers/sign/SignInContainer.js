/* eslint-disable no-unused-vars */
/* eslint-disable jsx-a11y/anchor-is-valid */
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { Stack } from '@mui/material';
import axios from 'axios';
import { setHeaderOpenFalse, setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import {
  login,
  mockLogin, selectStatus, selectUser, setUser,
} from '../../utils/redux/user/userSlice';
import PATH from '../../utils/constants/path';
import { isObjectValuesEmpty } from '../../utils/tools';
import { NO_BLANKS } from '../../utils/constants/common';
// import { createTheme, ThemeProvider } from '@mui/material/styles';

function Copyright(props) {
  return (
    // eslint-disable-next-line react/jsx-props-no-spreading
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        핀더펜
      </Link>
      {' '}
      {new Date().getFullYear()}
      .
    </Typography>
  );
}

// const theme = createTheme();

export default function SignInContainer() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector(selectUser);
  const status = useSelector(selectStatus);

  const guestLogin = () => {
    dispatch(mockLogin());
    // 추가로 이런 저런 설정을 여기에서 해줘야 함.
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const sign = {
      user_id: data.get('email'),
      password: data.get('password'),
    };
    const invalidIndex = isObjectValuesEmpty(sign);
    if (invalidIndex === -1) {
      dispatch(login(sign));
    } else {
      alert(NO_BLANKS);
    }
  };

  useEffect(() => {
    dispatch(setHeaderOpenFalse()); // 페이지 진입 시 헤더 감추기
    return () => {
      dispatch(setHeaderOpenTrue()); // 페이지 탈출 시 헤더 복구
    };
  }, []);

  useEffect(() => {
    // 로그인에 성공하는 경우를 감지하여 home으로 보내버림. 혹은, 이미 로그인 된 상태라면 홈으로 강제 이동
    if (user !== null) {
      navigate(PATH.home);
    }
  }, [user]);

  return (
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

        <Stack my={2}>
          <Typography component="h1" variant="h5">
            핀더펜과 함께 자산설계를 시작하세요!
          </Typography>
        </Stack>

        <Box component="form" onSubmit={handleSubmit} noValidate>
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            autoFocus
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
          />

          <Link onClick={() => alert('You forget a thousand things every day. Make sure this is one of them :)')}>
            비밀번호를 잊으셨나요?
          </Link>

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            로그인
          </Button>

          <Link to={PATH.signUp}>
            계정이 없으신가요?
          </Link>
        </Box>
        {/* 서버 감지에 실패한 경우에만 아래 버튼이 뜨게 개선 예정 */}
        <Button
          type="submit"
          fullWidth
          variant="contained"
          color="error"
          sx={{ mt: 3, mb: 2 }}
          onClick={() => guestLogin()}
        >
          {status === 'idle' ? 'Guest 계정으로 로그인 하기' : '로그인 중 ...'}
        </Button>
      </Box>
      <Copyright sx={{ mt: 8, mb: 4 }} />
    </Container>

  );
}