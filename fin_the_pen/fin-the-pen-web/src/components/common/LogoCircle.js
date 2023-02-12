import { Avatar } from '@mui/material';
import logo from '../../assets/logos/logo_removebg.png';

function LogoCircle() {
  return (
    <Avatar sx={{ bgcolor: 'primary.main', width: 100, height: 100 }}>
      <img src={logo} alt="" width="50px" height="50px" />
    </Avatar>
  );
}
export default LogoCircle;
