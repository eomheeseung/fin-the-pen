/* eslint-disable import/prefer-default-export */

/**
 * 유용한 함수들을 모아두는 곳.
 * 주석으로 부가 설명이 있으면 더 좋을 것 같아요.
 */

/**
 * isObjectValuesEmpty(obj)
 * 어떤 객체의 value 를 전수조사하여, 빈칸 '' 이 검출되지 않으면 -1을 반환하는 함수.
 * 즉, -1이 반환되면 이 객체에 빈칸이 없다는 의미이다.
 */
export const isObjectValuesEmpty = (obj) => (
  Object.values(obj).findIndex((v) => v === '')
);
