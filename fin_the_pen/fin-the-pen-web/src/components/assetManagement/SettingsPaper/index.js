import { Grid } from '@mui/material';
import assetManagements from '../../../utils/constants/managements';
import RoundedPaper from '../../common/RoundedPaper';
import SettingCard from './SettingCard';

function SettingsPaper() {
  return (
    <RoundedPaper>
      <Grid container spacing={2} alignItems="stretch">
        {assetManagements.map((s) => (
          <SettingCard setting={s} key={Math.random()} />
        ))}
      </Grid>
    </RoundedPaper>
  );
}

export default SettingsPaper;
