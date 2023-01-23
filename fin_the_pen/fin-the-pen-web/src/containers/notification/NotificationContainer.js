import {
  Avatar, IconButton, List, ListItem, ListItemAvatar, ListItemText,
} from '@mui/material';
import { cloneElement } from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import FolderIcon from '@mui/icons-material/Folder';

function generate(element) {
  return Array.from({ length: 30 }).map((value) => cloneElement(element, {
    key: value,
  }));
}
function NotificationContainer() {
  return (
    <List dense>
      {generate(
        <ListItem
          secondaryAction={(
            <IconButton edge="end" aria-label="delete">
              <DeleteIcon />
            </IconButton>
                  )}
        >
          <ListItemAvatar>
            <Avatar>
              <FolderIcon />
            </Avatar>
          </ListItemAvatar>
          <ListItemText
            primary="Single-line item"
            secondary="Secondary text"
          />
        </ListItem>,
      )}
    </List>
  );
}

export default NotificationContainer;
