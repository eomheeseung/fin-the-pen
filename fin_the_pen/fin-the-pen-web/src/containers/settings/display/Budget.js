import { useState } from 'react';
import RequestQuoteIcon from '@mui/icons-material/RequestQuote';
import ToggleListItem from '../../../components/settings/ToggleListItem';

function Budget() {
  const [checked, setChecked] = useState(false);
  const handleToggle = () => {
    setChecked(!checked);
    // 여기에서 앱에서 할 행동을 지시
  };
  return (
    <ToggleListItem
      icon={<RequestQuoteIcon />}
      title="예산 숨기기"
      checked={checked}
      setChecked={handleToggle}
    />
  );
}
export default Budget;
