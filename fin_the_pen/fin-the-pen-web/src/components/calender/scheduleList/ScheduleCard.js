/* eslint-disable no-unused-vars */
import {
  Box, CardActionArea, Divider, Stack, Typography,
} from '@mui/material';
import { useState } from 'react';

function ScheduleCard({ schedule, handleModal }) {
  return (
    <>
      <CardActionArea onClick={() => handleModal(schedule)}>
        <Box sx={{ width: '100vw', justifyContent: 'start' }}>
          <Box px={3} py={2}>
            <Stack direction="row">
              <Box
                sx={{
                  width: '15px', height: '15px', border: '4px solid', borderRadius: 3, borderColor: schedule.category.color, marginY: 'auto', marginRight: 2,
                }}
              />
              <Typography variant="caption">{`${schedule.start_time} - ${schedule.end_time}`}</Typography>
            </Stack>
            <Typography variant="h6" pl={1} pt={1}>{schedule.event_name}</Typography>
          </Box>
        </Box>
      </CardActionArea>
      <Divider />
    </>

  );
}

export default ScheduleCard;
