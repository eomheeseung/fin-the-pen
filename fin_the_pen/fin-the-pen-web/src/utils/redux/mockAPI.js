/* eslint-disable no-promise-executor-return */
/* eslint-disable import/prefer-default-export */
// A mock function to mimic making an async request for data
export function fetchMockLogin() {
  return new Promise((resolve) => setTimeout(() => resolve({
    data: {
      id: 'guest',
      user_id: 'guest',
      name: 'guest',
      password: '',
      bday: '2000-01-01',
      account_number: '123456789',
      goals: [
        {
          id: 0,
          question: '일년 동안의 저축 목표 금액은 얼마인가요?',
          label: '저축 목표 금액',
          answer: null,
          skip: true,
        },
        {
          id: 1,
          question: '월 기준 소비 목표 금액은 얼마인가요?',
          label: '소비 목표 금액',
          answer: null,
          skip: true,
        },
      ],
    },
  }), 2000));
}
