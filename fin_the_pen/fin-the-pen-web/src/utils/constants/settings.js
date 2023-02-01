import {
  lightBlue, pink, teal, yellow,
} from '@mui/material/colors';
import PATH from './path';

const momeyManagementSettings = Object.freeze([
  {
    title: '저축 목표 금액 설정',
    color: pink[200],
    path: PATH.savingsGoal,
  },
  {
    title: '정기 입출금액 설정',
    color: yellow[200],
    path: PATH.regularDepositWithdrawal,
  },
  {
    title: '카테고리별 자산 설정',
    color: lightBlue[200],
    path: PATH.assetsByCategory,
  },
  {
    title: '일정 관리',
    color: teal[200],
    path: PATH.scheduleManagement,
  },
]);

export default momeyManagementSettings;
