import { Card, Stack } from '@mui/material';
import ExclusionInput from '../ExclusionInput';
import ImportanceInput from '../ImportanceInput';
import SpendingInput from '../SpendingInput';

function AssetSettings({ mode }) {
  return (
    <Stack spacing={1}>
      <Card>
        <SpendingInput mode={mode} />
      </Card>
      <Card>
        <ImportanceInput />
      </Card>
      <Card>
        <ExclusionInput />
      </Card>
    </Stack>
  );
}

export default AssetSettings;
