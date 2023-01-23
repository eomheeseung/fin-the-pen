import { Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

function TestContainer() {
  const navigate = useNavigate();
  return (
    <>
      <Typography variant="h4">테스트</Typography>
      <Typography>이 페이지는 아직 어떤 페이지가 구현되지 않았을 때 임시로 이동하게 하는 페이지 입니다.</Typography>
      <Button variant="contained" color="error" onClick={() => navigate(-1)}>Go Back</Button>
    </>
  );
}
export default TestContainer;
