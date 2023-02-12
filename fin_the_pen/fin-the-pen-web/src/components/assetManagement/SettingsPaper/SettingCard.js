import { Box, CardActionArea, Grid } from '@mui/material';
import { useNavigate } from 'react-router-dom';

function SettingCard({ setting }) {
  const navigatie = useNavigate();

  return (
    <Grid item xs={6}>
      <CardActionArea onClick={() => navigatie(setting.path)}>
        <Box sx={{
          height: '15vh',
          backgroundColor: setting.color,
          paddingX: 1,
          borderRadius: 4,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
        >
          {setting.title}
        </Box>
      </CardActionArea>
    </Grid>
  );
}

export default SettingCard;
