import { ToggleButton } from '@mui/material';

function RoundedButton({ children, onClick, value }) {
  return (
    <ToggleButton value={value} sx={{ color: 'white', borderRadius: 30, borderWidth: 0 }} onClick={onClick}>
      {children}
    </ToggleButton>
  );
}
export default RoundedButton;
