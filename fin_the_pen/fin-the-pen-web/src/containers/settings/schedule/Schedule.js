import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import ClickableListItem from '../../../components/settings/ClickableListItem';

function Schedule() {
  return (
    <ClickableListItem icon={<EventAvailableIcon />} to="/test" title="일정관리" subTitle="" />
  );
}
export default Schedule;
