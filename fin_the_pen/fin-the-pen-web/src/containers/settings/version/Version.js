import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { Box } from '@mui/material';
import ClickableListItem from '../../../components/settings/ClickableListItem';

function Version() {
  return (
    <Box onClick={() => window.open('https://github.com/soyoung125/fin-the-pen-web/deployments/activity_log?environment=github-pages')}>
      <ClickableListItem
        icon={<CheckCircleIcon />}
        title="앱 버전"
        subTitle="새 창에서 보기"
      />
    </Box>
  );
}
export default Version;
