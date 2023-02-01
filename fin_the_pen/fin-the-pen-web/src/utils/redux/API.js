/* eslint-disable camelcase */
/* eslint-disable consistent-return */
/* eslint-disable import/prefer-default-export */
import axios from 'axios';

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
    alert(JSON.stringify(response));
    return response.data;
  } catch (err) {
    alert(err);
  }
};

export const fetchSchedules = async (user_id) => {
  try {
    const response = await axios.post('/getAllSchedules', { user_id });
    // alert(JSON.stringify(response));
    return response.data;
  } catch (err) {
    // 나중에 alert로 복구 예정
    // alert(err);
    console.log(err);
  }
};
