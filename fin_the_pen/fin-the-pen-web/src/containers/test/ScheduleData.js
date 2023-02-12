import { useSelector } from 'react-redux';
import TestBox from '../../components/test/TestBox';
import { selectSchedule } from '../../utils/redux/schedule/scheduleSlice';

function ScheduleData() {
  const schedule = useSelector(selectSchedule);
  return (
    <TestBox title="Redux : selectSchedule">
      {JSON.stringify(schedule)}
    </TestBox>
  );
}
export default ScheduleData;
