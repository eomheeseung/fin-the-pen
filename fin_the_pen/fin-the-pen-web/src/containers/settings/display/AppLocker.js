import { useState } from 'react';
import HttpsIcon from '@mui/icons-material/Https';

import ToggleListItem from '../../../components/settings/ToggleListItem';
import ClickableListItem from '../../../components/settings/ClickableListItem';

function AppLocker() {
  const [checked, setChecked] = useState(false);
  const handleToggle = () => {
    setChecked(!checked);
    // 여기에서 앱에서 할 행동을 지시
  };
  return (
    <>
      <ToggleListItem
        icon={<HttpsIcon />}
        title="앱 비밀번호 설정"
        checked={checked}
        setChecked={handleToggle}
      />
      {
      checked && (
        <ClickableListItem to="/test" title="비밀번호 인증 단계" subTitle="1단계" />
      )
    }
    </>
  );
}
export default AppLocker;
