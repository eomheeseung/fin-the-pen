import { useSelector } from 'react-redux';
import TestBox from '../../components/test/TestBox';
import { selectSchedules } from '../../utils/redux/schedule/scheduleSlice';

function SchedulesData() {
  const schedules = useSelector(selectSchedules);
  return (
    <TestBox title="Redux : selectSchedules">
      {JSON.stringify(schedules)}
    </TestBox>
  );
}
export default SchedulesData;
