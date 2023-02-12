import { Box } from '@mui/material';
import { useSelector } from 'react-redux';
import { selectUser } from '../../../../utils/redux/user/userSlice';
import Saving from './goals/Saving';
import Personal from './goals/Personal';

function SavingsGoal() {
  const user = useSelector(selectUser);

  return (
    <>
      <Box sx={{ fontWeight: 'bold' }}>
        {`"${user.name}"님의 한해 저축 목표입니다.`}
      </Box>

      <Saving />

      <Personal />
    </>
  );
}

export default SavingsGoal;
