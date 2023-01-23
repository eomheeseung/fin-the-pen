import {
  Avatar, Box, Button, Container, CssBaseline, InputAdornment, TextField, Typography,
} from '@mui/material';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
// import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setHeaderOpenFalse, setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
// import PATH from '../../utils/constants/path';

function SignUpContainer() {
  const dispatch = useDispatch();
  // const navigate = useNavigate();
  useEffect(() => {
    dispatch(setHeaderOpenFalse()); // 페이지 진입 시 헤더 감추기
    return () => {
      dispatch(setHeaderOpenTrue()); // 페이지 탈출 시 헤더 복구
    };
  }, []);

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const user = {
      user_id: data.get('email'),
      password: data.get('password'),
      name: data.get('name'),
      phone_number: data.get('phoneNumber'),
    };
    alert(JSON.stringify(user));
    /**
     * @eomheeseung
     *
     * axios로 회원가입 요청하기
     * POST 방식으로 위 user 객체를 서버에 전달하려고 합니다.
     * 하단에 있는 요청 주소는 수정해주셔야 합니다.
     *
     */
    axios.post('회원가입을_위한_API_주소', user)
      .then((response) => {
        // 처리 결과
        alert(response);
      }).catch((error) => {
        // error 발생 시
        alert(`err : ${error}`);
      });
  };
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
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          계정 가입
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>

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
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
            InputProps={{
              endAdornment: <InputAdornment position="end"><Button variant="contained" size="small">사용가능</Button></InputAdornment>,
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
              endAdornment: <InputAdornment position="end"><Button variant="contained" size="small">인증완료</Button></InputAdornment>,
            }}
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
    </Container>
  );
}
export default SignUpContainer;
/**
 * 회원가입 페이지
 */
