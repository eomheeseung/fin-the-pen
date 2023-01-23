import {
  ListItem, ListItemButton, ListItemIcon, ListItemText, Typography,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

function ClickableListItem({
  icon, to, title, subTitle,
}) {
  const navigate = useNavigate();
  return (
    <ListItemButton onClick={() => to && navigate(to)}>
      <ListItem>
        <ListItemIcon>
          {icon}
        </ListItemIcon>
        <ListItemText id={title} primary={title} />
        <Typography sx={{ color: 'gray' }}>{subTitle}</Typography>
      </ListItem>
    </ListItemButton>
  );
}
export default ClickableListItem;
