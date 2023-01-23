import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import LoginIcon from '@mui/icons-material/Login';
// import PersonIcon from '@mui/icons-material/Person';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import PATH from '../../../../utils/constants/path';
import { selectUser } from '../../../../utils/redux/user/userSlice';
import RoundedButton from '../../../common/RoundedButton';

function PersonalButton() {
  const navigate = useNavigate();
  const user = useSelector(selectUser);
  if (user === null) {
    return (
      <RoundedButton value="login" onClick={() => navigate(PATH.signIn)}>
        <LoginIcon />
      </RoundedButton>

    );
  }
  return (
    <RoundedButton value="user" onClick={() => navigate(PATH.mypage)}>
      {/* <PersonIcon /> */}
      <PersonOutlineOutlinedIcon />
    </RoundedButton>
  );
}

export default PersonalButton;
