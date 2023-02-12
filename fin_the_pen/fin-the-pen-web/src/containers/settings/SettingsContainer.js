/* eslint-disable react/jsx-props-no-spreading */
import {
  Box, Button, TextField, Typography,
} from '@mui/material';
import AppLocker from './display/AppLocker';
import Budget from './display/Budget';
import ThemeMode from './display/ThemeMode';
import Schedule from './schedule/Schedule';
import Version from './version/Version';
import Accordion from '../../components/common/accordions/Accordion';
import AccordionSummary from '../../components/common/accordions/AccordionSummary';
import AccordionDetails from '../../components/common/accordions/AccordionDetails';
import Change from './version/Change';

export default function SettingsContainer() {
  const handleSubmit = (event) => {
    const data = new FormData(event.currentTarget);
    const requiredData = {
      client_id: data.get('client_id'),
      client_secret: data.get('client_secret'),
    };
    const tmpWindow = window.open('about:blank');

    tmpWindow.location = `${'https://testapi.openbanking.or.kr/oauth/2.0/authorize?'
    + 'response_type=code&'
    + 'client_id='}${
      requiredData.client_id
    }&redirect_uri=http://localhost:63342/fin-the-pen/fin_the_pen.main/resource/home&`
    + 'scope=login inquiry transfer&'
    + 'state=12345678901234567890123456789012&'
    + 'auth_type=0';
  };

  return (
    <>
      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>화면 설정</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <AppLocker />
          <ThemeMode />
          <Budget />
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>일정</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Schedule />
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>보안 / 인증서</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography>메뉴가 올 자리</Typography>
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>알림</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography>메뉴가 올 자리</Typography>
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>연결 관리</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography>메뉴가 올 자리</Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate>
            <TextField
              required
              fullWidth
              id="client_id"
              label="client_id(추후 삭제 예정)"
              name="client_id"
              autoComplete="client_id"
              autoFocus
            />
            {/* <TextField
              required
              fullWidth
              id="client_secret"
              label="client_secret(추후 삭제 예정)"
              name="client_secret"
              autoComplete="client_secret"
              autoFocus
            /> */}
            <Button type="submit">인증하기</Button>
          </Box>
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>약관 및 정책</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography>메뉴가 올 자리</Typography>
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>앱 버전</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Version />
          <Change />
        </AccordionDetails>
      </Accordion>

      <Accordion>
        <AccordionSummary
          // expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>서비스 초기화 및 탈퇴</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography>메뉴가 올 자리</Typography>
        </AccordionDetails>
      </Accordion>
    </>
  );
}
