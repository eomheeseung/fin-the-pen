import { useState } from 'react';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import ToggleListItem from '../../../components/settings/ToggleListItem';

function ThemeMode() {
  const [checked, setChecked] = useState(false);
  const handleToggle = () => {
    setChecked(!checked);
    // 여기에서 앱에서 할 행동을 지시
  };
  return (
    <ToggleListItem
      icon={<DarkModeIcon />}
      title="다크모드 켜기"
      checked={checked}
      setChecked={handleToggle}
    />
  );
}
export default ThemeMode;
