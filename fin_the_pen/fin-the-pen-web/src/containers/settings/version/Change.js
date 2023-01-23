import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { Box } from '@mui/material';
import ClickableListItem from '../../../components/settings/ClickableListItem';

function Change() {
  return (
    <Box onClick={() => window.open('https://github.com/soyoung125/fin-the-pen-web/commits/main')}>
      <ClickableListItem
        icon={<CheckCircleIcon />}
        title="변경 사항"
        subTitle="새 창에서 보기"
      />
    </Box>
  );
}
export default Change;
