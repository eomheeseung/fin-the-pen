import { Typography } from '@mui/material';
import Accordion from '../../../../../common/accordions/Accordion';
import AccordionSummary from '../../../../../common/accordions/AccordionSummary';
import AccordionDetails from '../../../../../common/accordions/AccordionDetails';
import FilterChips from './FilterChips';

function FilterAccordion({ tag }) {
  return (
    <Accordion>
      <AccordionSummary
        aria-controls="panel1a-content"
        id="panel1a-header"
      >
        <Typography>{tag.type}</Typography>
      </AccordionSummary>
      <AccordionDetails>
        {tag.nested.map((el) => (
          <FilterChips key={el.type} nested={el} />
        ))}
      </AccordionDetails>
    </Accordion>
  );
}
export default FilterAccordion;
