import {
  Box,
  List, Typography,
} from '@mui/material';
import AnalysisListItem from './AnaylysisListItem';

function AnalysisList({ data, clickListItem, hexToRGB }) {
  return (
    <Box mx={3}>
      <Typography sx={{ mt: 4 }} variant="h6" component="div">
        Your Ranking
      </Typography>
      <List>
        {data.map((d, index) => (
          <AnalysisListItem
            key={d.label}
            category={d}
            rank={(index + 1)}
            clickListItem={clickListItem}
            bgColor={hexToRGB(d.color, 0.5)}
          />
        ))}
      </List>
    </Box>
  );
}

export default AnalysisList;
