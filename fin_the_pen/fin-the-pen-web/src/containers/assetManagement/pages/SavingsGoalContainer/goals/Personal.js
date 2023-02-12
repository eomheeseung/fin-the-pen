/* eslint-disable no-unused-vars */
import {
  Box, Button, Divider, FormControl, Grid, IconButton, InputAdornment,
  OutlinedInput, Stack, Switch, TextField, Typography,
} from '@mui/material';
import BorderColorIcon from '@mui/icons-material/BorderColor';
import { useEffect, useState } from 'react';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import ClearIcon from '@mui/icons-material/Clear';
import ModalStaticBackdrop from '../../../../../components/layouts/ModalStaticBackdrop';
import RoundedBorderBox from '../../../../../components/common/RoundedBorderBox';
import { SOMETHING_IS_WRONG } from '../../../../../utils/constants/common';

function Personal() {
  const [personalGoalModalOpen, setPersonalGoalModalOpen] = useState(false);

  const init = {
    name: 'i-Mac',
    money: 2027100,
    deadline: '2024-01-01',
    type: 'month', // day||month
    autoSaving: true,
  };
  const [personalGoal, setPersonalGoal] = useState(init);

  const changePersonalGoal = (state) => {
    setPersonalGoal({ ...personalGoal, [state.target.id]: state.target.value });
  };

  const divisionByType = (type, money) => {
    switch (type) {
      case 'day':
        return money / 365;
      case 'month':
        return money / 12;
      default:
        return SOMETHING_IS_WRONG;
    }
  };

  useEffect(() => {
    console.log(personalGoal);
  }, [personalGoal]);

  return (
    <>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Box sx={{ fontWeight: 'bold' }}>
          당신의 또 다른 목표는 무엇인가요?
        </Box>
        <IconButton color="primary" onClick={() => setPersonalGoalModalOpen(true)}>
          <BorderColorIcon fontSize="small" />
        </IconButton>
      </Stack>

      <Grid container spacing={1} textAlign="center" mt={0}>
        <Grid item xs={6}>
          <Stack
            justifyContent="space-around"
            sx={{
              borderRadius: 2, backgroundColor: 'primary.main', color: 'white', p: 2, height: '100%',
            }}
          >
            <Box mb={2}>나의 목표</Box>
            <Box>i-Mac</Box>
            <Box>xxxxxxx원</Box>
          </Stack>
        </Grid>

        <Grid item xs={6}>
          <RoundedBorderBox>
            <Stack
              direction="row"
              justifyContent="space-between"
            >
              <Box>기간</Box>
              <Box sx={{ color: 'primary.main' }}>YY/MM/DD</Box>
            </Stack>
          </RoundedBorderBox>

          <Box my={1} />

          <RoundedBorderBox>
            <Box mb={1}>핀더펜 MONEY</Box>
            <Box sx={{ color: 'primary.main' }}>xxxxxxx원</Box>
          </RoundedBorderBox>
        </Grid>
      </Grid>

      <ModalStaticBackdrop
        keepMounted
        width="xs"
        open={personalGoalModalOpen}
        component={(
          <Stack p={2} spacing={1}>
            <Stack direction="row" alignItems="center" justifyContent="space-between">
              <IconButton onClick={() => setPersonalGoalModalOpen(false)}>
                <ClearIcon />
              </IconButton>
              <Typography variant="h5" sx={{ fontWeight: 'bold' }}>
                Personal Goal
              </Typography>
              <IconButton onClick={() => setPersonalGoal(init)} color="error">
                <DeleteForeverIcon />
              </IconButton>
            </Stack>
            <Box my={1}>
              <Divider />
            </Box>

            <Stack spacing={1}>

              {/* 목표 */}
              <FormControl fullWidth>
                <OutlinedInput
                  id="name"
                  startAdornment={<InputAdornment position="start">목표</InputAdornment>}
                  value={personalGoal.name}
                  onChange={changePersonalGoal}
                  size="small"
                  inputProps={{
                    style: { textAlign: 'right' },
                  }}
                />
              </FormControl>

              {/* 금액 */}
              <FormControl fullWidth>
                <OutlinedInput
                  id="money"
                  startAdornment={<InputAdornment position="start">금액</InputAdornment>}
                  value={personalGoal.money}
                  onChange={changePersonalGoal}
                  size="small"
                  inputProps={{
                    style: { textAlign: 'right' },
                  }}
                />
              </FormControl>

              {/* 기한 */}
              <TextField
                id="deadline"
                type="date"
                fullWidth
                /**
                 * 다음 링크를 참고하면 InputProps / inputProps 두 가지의 속성을 지원하고 있는데
                 * 대소문자를 고려했을 때 중복된 속성명임에도 불구하고 다른 기능을 지원하는 듯 함.
                 * https://mui.com/material-ui/api/text-field/
                 */
                InputProps={{
                  startAdornment: <InputAdornment position="start">기한</InputAdornment>,
                }}
                // eslint-disable-next-line react/jsx-no-duplicate-props
                inputProps={{
                  style: { textAlign: 'right' },
                }}
                value={personalGoal.deadline}
                onChange={changePersonalGoal}
                size="small"
              />

              {/* 적금 단위 */}
              <Stack direction="row" spacing={1}>
                <Button
                  fullWidth
                  id="type"
                  variant={personalGoal.type === 'day' ? 'contained' : 'outlined'}
                  onClick={() => changePersonalGoal({
                    target: {
                      id: 'type',
                      value: 'day',
                    },
                  })}
                >
                  하루 기준
                </Button>
                <Button
                  fullWidth
                  id="type"
                  variant={personalGoal.type === 'month' ? 'contained' : 'outlined'}
                  onClick={() => changePersonalGoal({
                    target: {
                      id: 'type',
                      value: 'month',
                    },
                  })}
                >
                  한달 기준
                </Button>
              </Stack>

              {/* 필요 적금 액 */}
              <FormControl fullWidth>
                <OutlinedInput
                  startAdornment={<InputAdornment position="start">필요 적금액</InputAdornment>}
                  value={divisionByType(personalGoal.type, personalGoal.money)}
                  size="small"
                  inputProps={{
                    style: { textAlign: 'right' },
                  }}
                />
              </FormControl>

              {/* 자동 적금 */}
              <FormControl fullWidth>
                <OutlinedInput
                  startAdornment={<InputAdornment position="start">자동 적금</InputAdornment>}
                  endAdornment={(
                    <Switch
                      checked={personalGoal.autoSaving}
                      onChange={() => changePersonalGoal({
                        target: {
                          id: 'autoSaving',
                          value: !personalGoal.autoSaving,
                        },
                      })}
                      inputProps={{ 'aria-label': 'controlled' }}
                    />
)}
                  size="small"
                  readOnly
                />
              </FormControl>

            </Stack>
            <Button fullWidth variant="contained">
              나만의 목표 설정하기
            </Button>
          </Stack>
        )}
      />
    </>
  );
}

export default Personal;
