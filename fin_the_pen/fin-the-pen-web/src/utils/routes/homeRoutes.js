import AnalysisContainer from '../../containers/analysis/AnalysisContainer';
import HomeContainer from '../../containers/home/HomeContainer';
import MoneyManagementContainer from '../../containers/moneyManagement/MoneyManagementContainer';
import MyPageContainer from '../../containers/mypage/MyPageContainer';
import NotificationContainer from '../../containers/notification/NotificationContainer';
import SettingsContainer from '../../containers/settings/SettingsContainer';
import SignInContainer from '../../containers/sign/SignInContainer';
import SignUpContainer from '../../containers/sign/SignUpContainer';
import TestContainer from '../../containers/test/TestContainer';
import PATH from '../constants/path';

const homeRoutes = [
  {
    path: PATH.home,
    element: <HomeContainer />,
  },
  {
    path: PATH.analysis,
    element: <AnalysisContainer />,
  },
  {
    path: PATH.signIn,
    element: <SignInContainer />,
  },
  {
    path: PATH.signUp,
    element: <SignUpContainer />,
  },
  {
    path: PATH.mypage,
    element: <MyPageContainer />,
  },
  {
    path: PATH.notification,
    element: <NotificationContainer />,
  },
  {
    path: PATH.moneyManagement,
    element: <MoneyManagementContainer />,
  },
  {
    path: PATH.settings,
    element: <SettingsContainer />,
  },
  {
    path: PATH.test,
    element: <TestContainer />,
  },
];

export default homeRoutes;
