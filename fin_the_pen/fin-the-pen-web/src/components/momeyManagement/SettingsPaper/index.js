import { Grid, Paper } from '@mui/material';
import momeyManagementSettings from '../../../utils/constants/settings';
import SettingCard from './SettingCard';

function SettingsPaper() {
  return (
    <Paper
      elevation={8}
      sx={{
        marginTop: 2, marginBottom: 5, padding: 2, textAlign: 'center', borderRadius: 3,
      }}
    >
      <Grid container spacing={2} alignItems="stretch">
        {momeyManagementSettings.map((s) => (
          <SettingCard setting={s} />
        ))}
      </Grid>
    </Paper>
  );
}

export default SettingsPaper;
