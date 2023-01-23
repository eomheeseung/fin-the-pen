import {
  Box, Paper, Stack,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
// import NotificationsIcon from '@mui/icons-material/Notifications';
import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import { selectHeaderMode, selectHeaderOpen } from '../../../utils/redux/common/commonSlice';
import FullScreenDialog from '../FullScreenDialog';
import RoundedButton from '../../common/RoundedButton';
import PATH from '../../../utils/constants/path';
import { selectUser } from '../../../utils/redux/user/userSlice';
import FilterButton from './buttons/FilterButton';
import PersonalButton from './buttons/PersonalButton';
import logo from '../../../assets/logos/logo_square.jpg';

function LogoButton({ navigate }) {
  return (
    <RoundedButton value="user" onClick={() => navigate(PATH.home)}>
      <img src={logo} alt="" width="26px" height="26px" />
    </RoundedButton>
  );
}

function TopBar() {
  const navigate = useNavigate();
  const user = useSelector(selectUser);
  const headerOpen = useSelector(selectHeaderOpen);
  const headerMode = useSelector(selectHeaderMode);
  const [fullScreenModalOpen, setFullScreenModalOpen] = useState(false);
  const [ask, setAsk] = useState({
    id: 0,
    question: '',
    label: '',
    answer: null,
    skip: false,
  });

  useEffect(() => {
    if (user !== null) {
      for (let index = 0; index < user.goals.length; index += 1) {
        const goal = user.goals[index];
        if (goal.skip === false && goal.answer === null) {
          setAsk(goal);
          setFullScreenModalOpen(true);
          break;
        }
      }
    }
  }, [user]);

  return (
    <>
      <Box sx={{ position: 'relative', zIndex: 100 }}>
        {
          headerOpen
          && (
            <Paper
              elevation={10}
              sx={{
                backgroundColor: 'primary.main',
                height: 100,
                borderTopLeftRadius: '0px',
                borderTopRightRadius: '0px',
                borderBottomLeftRadius: '15px',
                borderBottomRightRadius: '15px',
              }}
            >
              <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="flex-end"
                sx={{ height: 100 }}
              >

                {/* 헤더 좌측 메뉴 */}
                <Stack
                  direction="row"
                  justifyContent="space-between"
                  alignItems="flex-end"
                >
                  {headerMode === 'home' && (
                    <LogoButton navigate={navigate} />
                  )}
                  {headerMode === 'analysis' && (
                    <FilterButton />
                  )}
                </Stack>

                {/* 헤더 중앙 메뉴 */}
                <Stack
                  direction="row"
                  justifyContent="space-between"
                  alignItems="flex-end"
                >
                  {headerMode === 'analysis' && (
                    <LogoButton navigate={navigate} />
                  )}
                </Stack>

                {/* 헤더 우측 메뉴 */}
                <Stack
                  direction="row"
                  justifyContent="space-between"
                  alignItems="flex-end"
                >
                  {headerMode === 'home' && (
                    <>
                      <RoundedButton value="user" onClick={() => alert('준비 중인 메뉴')}>
                        <SearchIcon />
                      </RoundedButton>
                      <RoundedButton value="notification" onClick={() => navigate(PATH.notification)}>
                        {/* <NotificationsIcon /> */}
                        <NotificationsOutlinedIcon />
                      </RoundedButton>
                      <PersonalButton />
                    </>
                  )}
                  {headerMode === 'analysis' && (
                    <PersonalButton />
                  )}
                </Stack>

              </Stack>
            </Paper>
          )
        }
      </Box>
      <FullScreenDialog open={fullScreenModalOpen} setOpen={setFullScreenModalOpen} ask={ask} />
    </>

  );
}
export default TopBar;
/**
 * 상단 바
 */
