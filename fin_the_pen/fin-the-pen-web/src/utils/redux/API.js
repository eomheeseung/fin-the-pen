/* eslint-disable camelcase */
/* eslint-disable consistent-return */
/* eslint-disable import/prefer-default-export */
import axios from 'axios';

/**
 * 반드시 서버로 요청할 때 JSON 형식이어야 함!
 */

export const fetchSignUp = async (user) => {
  try {
    const response = await axios.post('/fin-the-pen-web/sign-up', user);
    return response.data;
  } catch (err) {
    alert(err);
  }
};

export const fetchLogin = async (sign) => {
  try {
    const response = await axios.post('/fin-the-pen-web/sign-in', sign);
    return response.data;
  } catch (err) {
    alert(err);
  }
};

export const fetchCreateSchedule = async (schedule) => {
  try {
    const response = await axios.post('/createSchedule', schedule);
    // alert(JSON.stringify(response));
    return response.data;
  } catch (err) {
    alert(err);
  }
};

export const fetchDeleteSchedule = async (id) => {
  try {
    console.log({ id });
    const response = await axios.post('/deleteSchedule', { id });
    // alert(JSON.stringify(response));
    return response.data;
  } catch (err) {
    alert(err);
  }
};

export const fetchMonthSchedules = async (schedule) => {
  try {
    const response = await axios.post('/getMonthSchedules', schedule);
    // alert(JSON.stringify(response));
    return response.data;
  } catch (err) {
    // 나중에 alert로 복구 예정
    // alert(err);
    console.log(err);
  }
};
