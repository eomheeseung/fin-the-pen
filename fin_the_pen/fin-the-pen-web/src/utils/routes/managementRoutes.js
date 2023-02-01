import AssetsByCategory from '../../containers/moneyManagement/settings/AssetsByCategory';
import RegularDepositWithdrawal from '../../containers/moneyManagement/settings/RegularDepositWithdrawal';
import SavingsGoal from '../../containers/moneyManagement/settings/SavingsGoalContainer';
import ScheduleManagement from '../../containers/moneyManagement/settings/ScheduleManagement';
import PATH from '../constants/path';

const managementRoutes = [
  {
    path: PATH.savingsGoal,
    element: <SavingsGoal />,
  },
  {
    path: PATH.regularDepositWithdrawal,
    element: <RegularDepositWithdrawal />,
  },
  {
    path: PATH.assetsByCategory,
    element: <AssetsByCategory />,
  },
  {
    path: PATH.scheduleManagement,
    element: <ScheduleManagement />,
  },
];

export default managementRoutes;
