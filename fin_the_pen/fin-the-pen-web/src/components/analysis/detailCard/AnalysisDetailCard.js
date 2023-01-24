/* eslint-disable max-len */
import {
  Alert,
  Card, CardContent, CardHeader, IconButton, Stack, Typography,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import AssetManagement from './AssetManagement';
import ALERTS from '../../../utils/constants/alerts';
import SpendingDetailCard from './SpendingDetailCard';

function AnalysisDetailCard({ closeDetailCard, selectedItem }) {
  const random = Math.floor((Math.random() * 5));

  return (
    <Card sx={{ height: '100%', border: '0px' }} variant="outlined">
      <CardHeader
        action={(
          <IconButton onClick={() => closeDetailCard()}>
            <CloseIcon />
          </IconButton>
        )}
      />
      <CardContent>
        <Stack direction="row" justifyContent="space-between" alignItems="flex-end">
          <Typography variant="h4" sx={{ fontWeight: 'bold' }}>{`${selectedItem.label} 지출 내역`}</Typography>
          <Typography sx={{ color: 'primary.main', fontWeight: 'bold' }}>{`총 ${selectedItem.history.length}건`}</Typography>
        </Stack>
        <Stack sx={{ borderRadius: 3, marginBottom: 2 }}>
          {selectedItem.history.map((s) => (
            <SpendingDetailCard schedule={s} key={Math.random()} bgColor={selectedItem.color} />
          ))}
        </Stack>
        <AssetManagement selectedItem={selectedItem} />
        <Alert color={ALERTS[random].color} sx={{ width: '100%' }} icon={ALERTS[random].icon}>
          {ALERTS[random].message}
        </Alert>
      </CardContent>
    </Card>
  );
}

export default AnalysisDetailCard;
