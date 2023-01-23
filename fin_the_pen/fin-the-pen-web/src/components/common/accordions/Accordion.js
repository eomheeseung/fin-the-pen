/* eslint-disable react/jsx-props-no-spreading */
import styled from '@emotion/styled';
import MuiAccordion from '@mui/material/Accordion';

const Accordion = styled((props) => (
  <MuiAccordion disableGutters elevation={0} square {...props} />
))(({ theme }) => ({
  border: `1px solid ${theme.palette.divider}`,
  '&:not(:last-child)': {
    borderBottom: 0,
  },
  '&:before': {
    display: 'none',
  },
}));
export default Accordion;
