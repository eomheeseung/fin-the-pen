/* eslint-disable no-unused-vars */
import {
  Avatar, Box, Button, Container, CssBaseline, InputAdornment, Stack, TextField, Typography,
} from '@mui/material';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { setHeaderOpenFalse, setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import { fetchSignUp } from '../../utils/redux/API';
import { isObjectValuesEmpty } from '../../utils/tools';
import {
  NO_BLANKS, NO_DUPLICATION_ID, NO_SIGNAL_FROM_SERVER, SIGN_UP_SUCCESS,
} from '../../utils/constants/common';
import PATH from '../../utils/constants/path';
import CenterBox from '../../components/layouts/CenterBox';
import LogoCircle from '../../components/common/LogoCircle';

function SignUpContainer() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(setHeaderOpenFalse()); // 페이지 진입 시 헤더 감추기
    return () => {
      dispatch(setHeaderOpenTrue()); // 페이지 탈출 시 헤더 복구
    };
  }, []);

  const signUp = async (user) => {
    const result = await fetchSignUp(user);
    // 에러 핸들링
    if (result === undefined) {
      alert(`${NO_SIGNAL_FROM_SERVER} GUEST 계정으로 로그인 하세요.`);
      return;
    }
    if (result) {
      alert(SIGN_UP_SUCCESS);
      navigate(PATH.signIn);
    } else {
      alert(NO_DUPLICATION_ID);
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const user = {
      user_id: data.get('email'),
      password: data.get('password'),
      name: data.get('name'),
      phone_number: data.get('phoneNumber'),
    };
    const invalidIndex = isObjectValuesEmpty(user);
    if (invalidIndex === -1) {
      signUp(user);
    } else {
      alert(NO_BLANKS);
    }
  };
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
            계정 만들기
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
            id="name"
            label="성명"
            name="name"
            autoFocus
          />

          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="이메일 주소"
            name="email"
            autoComplete="email"
            autoFocus
          />

          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="비밀번호"
            type="password"
            id="password"
            autoComplete="current-password"
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <Button
                    variant="contained"
                    size="small"
                    color="success"
                  >
                    사용가능

                  </Button>
                </InputAdornment>
              ),
            }}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            id="phoneNumber"
            label="전화번호"
            name="phoneNumber"
            autoFocus
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <Button
                    variant="contained"
                    size="small"
                    color="success"
                  >
                    인증완료
                  </Button>
                </InputAdornment>
              ),
            }}
          />
          <Stack direction="row" spacing={1} my={2}>
            <Button
              fullWidth
              variant="contained"
              color="error"
              onClick={() => navigate(PATH.signIn)}
            >
              뒤로가기
            </Button>
            <Button
              type="submit"
              fullWidth
              variant="contained"
            >
              회원가입
            </Button>
          </Stack>
        </Box>
      </Stack>
    </CenterBox>
  );
}
export default SignUpContainer;
/**
 * 회원가입 페이지
 */
