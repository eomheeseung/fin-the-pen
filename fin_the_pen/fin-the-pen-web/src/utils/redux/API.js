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
