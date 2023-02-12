import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { Stack } from '@mui/material';
import { setHeaderOpenFalse, setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import {
  login, mockLogin, selectStatus, selectUser,
} from '../../utils/redux/user/userSlice';
import PATH from '../../utils/constants/path';
import { isObjectValuesEmpty } from '../../utils/tools';
import { NO_BLANKS } from '../../utils/constants/common';
import CenterBox from '../../components/layouts/CenterBox';
import LogoCircle from '../../components/common/LogoCircle';

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
    <CenterBox>
      <Stack
        justifyContent="center"
        alignItems="center"
        px={1}
      >
        <LogoCircle />
        <Stack my={2}>
          <Typography component="h1" variant="h5">
            핀더펜과 함께 자산설계를 시작하세요!
          </Typography>
        </Stack>

        <Box
          component="form"
          onSubmit={handleSubmit}
          noValidate
          sx={{ maxWidth: '400px' }}
        >
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

          <Button onClick={() => alert('You forget a thousand things every day. Make sure this is one of them :)')}>
            비밀번호를 잊으셨나요?
          </Button>

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            로그인
          </Button>

          <Button onClick={() => navigate(PATH.signUp)}>
            계정이 없으신가요?
          </Button>
        </Box>

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
        <Typography color="text.secondary">
          {'Copyright © '}
          <Link color="inherit" href="https://mui.com/">
            핀더펜
          </Link>
          {' '}
          {new Date().getFullYear()}
          .
        </Typography>

      </Stack>

    </CenterBox>
  );
}
