import SentimentVeryDissatisfiedIcon from '@mui/icons-material/SentimentVeryDissatisfied';
import SentimentDissatisfiedIcon from '@mui/icons-material/SentimentDissatisfied';
import SentimentSatisfiedIcon from '@mui/icons-material/SentimentSatisfied';
import SentimentVerySatisfiedIcon from '@mui/icons-material/SentimentVerySatisfied';
import SentimentSatisfiedAltIcon from '@mui/icons-material/SentimentSatisfiedAlt';

// 아이콘 변경 예정
const ALERTS = Object.freeze([
  {
    message: '경고! 자산이 거의 남지 않았습니다.',
    color: 'error',
    icon: <SentimentVeryDissatisfiedIcon fontSize="inherit" />,
  },
  {
    message: '주의! 자산을 조금 더 아껴보세요.',
    color: 'warning',
    icon: <SentimentDissatisfiedIcon fontSize="inherit" />,
  },
  {
    message: '보통! 아직까지는 괜찮습니다.',
    color: 'info', // 추후 색상 변경 예정
    icon: <SentimentSatisfiedIcon fontSize="inherit" />,
  },
  {
    message: '양호! 자산을 위한 소비계획이 알맞습니다.',
    color: 'success',
    icon: <SentimentSatisfiedAltIcon fontSize="inherit" />,
  },
  {
    message: '우수! 똑똑한 소비 생활을 실천중입니다.',
    color: 'secondary',
    icon: <SentimentVerySatisfiedIcon fontSize="inherit" />,
  },
]);

export default ALERTS;
