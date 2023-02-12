import { Button } from '@mui/material';

function StateButton({ state, setState, value }) {
  return (
    <Button
      variant={state === value ? 'contained' : 'text'}
      onClick={() => setState(value)}
    >
      {value}
    </Button>
  );
}
export default StateButton;
