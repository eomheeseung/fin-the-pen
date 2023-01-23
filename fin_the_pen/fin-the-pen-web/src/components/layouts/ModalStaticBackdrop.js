/* eslint-disable react/jsx-props-no-spreading */
import {
  Dialog,
} from '@mui/material';

export default function ModalStaticBackdrop(props) {
  const {
    width, component, open, ...other
  } = props;

  return (
    <Dialog
      sx={{ '& .MuiDialog-paper': { borderRadius: '1rem', width: '100%' } }}
      maxWidth={width}
      open={open}
      scroll="body"
      {...other}
    >
      {component}
    </Dialog>
  );
}
