import { useSelector } from 'react-redux';
import TestBox from '../../components/test/TestBox';
import { selectGuestMode } from '../../utils/redux/common/commonSlice';

function GuestMode() {
  const guestMode = useSelector(selectGuestMode);

  return (
    <TestBox title="Redux : selectGuestMode">
      {JSON.stringify(guestMode)}
    </TestBox>
  );
}
export default GuestMode;
