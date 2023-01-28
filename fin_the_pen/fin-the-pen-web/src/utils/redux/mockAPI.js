/* eslint-disable no-promise-executor-return */
/* eslint-disable import/prefer-default-export */
// A mock function to mimic making an async request for data
export function fetchMockLogin() {
  return new Promise((resolve) => setTimeout(() => resolve({
    data: {
      id: 0,
      user_id: 'guest@finthepen.com',
      name: 'guest',
      bday: '2000-01-01',
      registerDate: '2023-01-25T14:57:08.023+00:00',
      phone_number: '010-4413-5698',
      // account_number: '123456789', // 백엔드에서 먼저 추가 필요(?)
      // goals: [
      //   {
      //     id: 0,
      //     question: '일년 동안의 저축 목표 금액은 얼마인가요?',
      //     label: '저축 목표 금액',
      //     answer: null,
      //     skip: true,
      //   },
      //   {
      //     id: 1,
      //     question: '월 기준 소비 목표 금액은 얼마인가요?',
      //     label: '소비 목표 금액',
      //     answer: null,
      //     skip: true,
      //   },
      // ],
    },
  }), 2000));
}
