import { useSelector } from 'react-redux';
import TestBox from '../../components/test/TestBox';
import { selectUser } from '../../utils/redux/user/userSlice';

function UserData() {
  const user = useSelector(selectUser);

  return (
    <TestBox title="Redux : selectUser">
      {JSON.stringify(user)}
    </TestBox>
  );
}
export default UserData;
