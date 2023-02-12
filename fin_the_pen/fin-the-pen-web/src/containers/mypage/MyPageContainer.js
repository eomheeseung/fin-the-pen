/* eslint-disable no-restricted-globals */
import {
  Box, Button, Stack, Typography,
} from '@mui/material';
import { useEffect } from 'react';
import {
  // useDispatch,
  useSelector,
} from 'react-redux';
import { useNavigate } from 'react-router-dom';
import PATH from '../../utils/constants/path';
import { selectGuestMode } from '../../utils/redux/common/commonSlice';
import {
  // logOut,
  selectUser,
} from '../../utils/redux/user/userSlice';
import GuestDataManager from '../test/GuestDataManager';
import GuestMode from '../test/GuestMode';
import SchedulesData from '../test/SchedulesData';
import ScheduleFilterData from '../test/ScheduleFilterData';
import UserData from '../test/UserData';
import ScheduleData from '../test/ScheduleData';
// import SignInContainer from '../sign/SignInContainer';

function MyPageContainer() {
  const user = useSelector(selectUser);
  const guestMode = useSelector(selectGuestMode);
  const navigate = useNavigate();
  // const dispatch = useDispatch();
  const userLogOut = () => {
    if (confirm('게스트 계정은 로그아웃 하는 경우 지금까지 작업한 내용이 저장되지 않습니다. 중요한 자료는 미리 백업해주세요. (확인 시 모든 정보 날라감)')) {
      sessionStorage.clear();
      window.location.href = '';
      /**
       * logOut을 dispatch하지 않고, redux 스토어를 강제로 비워서 초기화 처리해버리는 중
       */
      // dispatch(logOut());
    }
  };
  useEffect(() => {
    // 로그인 안된 계정은 로그인 페이지로 강제연결
    if (user === null) {
      navigate(PATH.signIn);
    }
  }, [user]);
  return (
    <Box>
      {
        guestMode
          ? (
            <GuestDataManager />
          ) : (
            <Stack spacing={2} m={2} border={1} p={2}>
              <Typography>현재 작업한 내용은 모두 서버에 저장중입니다.</Typography>
            </Stack>
          )
      }
      <Box m={2}>
        <Button
          variant="contained"
          color="error"
          onClick={() => userLogOut()}
        >
          로그아웃
        </Button>
      </Box>

      <GuestMode />
      <UserData />
      <ScheduleData />
      <SchedulesData />
      <ScheduleFilterData />
    </Box>
  );
}
export default MyPageContainer;
