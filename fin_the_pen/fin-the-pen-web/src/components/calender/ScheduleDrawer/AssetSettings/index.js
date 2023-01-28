import { Card, Stack } from '@mui/material';
import ExclusionInput from '../inputs/ExclusionInput';
import ImportanceInput from '../inputs/ImportanceInput';
import SpendingInput from '../inputs/SpendingInput';

function AssetSettings({
  schedule, updateSchedule, updateSpendingType, updateExclusion, mode,
}) {
  return (
    <Stack spacing={1}>
      <Card>
        <SpendingInput
          schedule={schedule}
          updateSchedule={updateSchedule}
          updateSpendingType={updateSpendingType}
          mode={mode}
        />
      </Card>
      <Card>
        <ImportanceInput
          schedule={schedule}
          updateSchedule={updateSchedule}
        />
      </Card>
      <Card>
        <ExclusionInput
          schedule={schedule}
          updateExclusion={updateExclusion}
        />
      </Card>
    </Stack>
  );
}

export default AssetSettings;
