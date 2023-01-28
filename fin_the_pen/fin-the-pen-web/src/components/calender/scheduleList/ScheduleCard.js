/* eslint-disable no-unused-vars */
import {
  Box, CardActionArea, Divider, Stack, Typography,
} from '@mui/material';
import { grey } from '@mui/material/colors';
import { useState } from 'react';

function ScheduleCard({ schedule, handleModal, category }) {
  return (
    <>
      <CardActionArea onClick={() => handleModal(schedule)}>
        <Box sx={{ width: '100vw', justifyContent: 'start' }}>
          <Box px={3} py={2}>
            <Stack direction="row">
              <Box
                sx={{
                  width: '15px', height: '15px', border: '4px solid', borderRadius: 3, borderColor: category.color, marginY: 'auto', marginRight: 2,
                }}
              />
              <Typography variant="caption">{`${schedule.start_time} - ${schedule.end_time}`}</Typography>
            </Stack>
            <Stack
              direction="row"
              pl={1}
              pt={1}
              alignItems="center"
              justifyContent="space-between"
            >
              <Typography
                variant="h6"
                noWrap
              >
                {schedule.event_name}
              </Typography>
              {/* 색상은 실제 소비 내역 데이터 연동 후 바꿀 예정 */}
              <Typography sx={{ color: grey[500] }}>
                {`${schedule.type}${parseInt(schedule.expected_spending, 10).toLocaleString('ko-KR')}`}
              </Typography>
            </Stack>
          </Box>
        </Box>
      </CardActionArea>
      <Divider />
    </>

  );
}

export default ScheduleCard;
