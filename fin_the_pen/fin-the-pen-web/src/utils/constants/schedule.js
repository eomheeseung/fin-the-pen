const SCHEDULE_DRAWER = Object.freeze({
  drawer_title: {
    create: '새로운 이벤트',
    read: '일정',
    modify: '일정 편집',
  },
  name: '제목',
  date: '날짜',
  start_time: '시작',
  end_time: '종료',
  repeat: '반복',
  repeating_cycle: '반복 주기',
  repeat_deadline: '반복 종료 기한',
  category_title: '일정 카테고리',
  add_category: '+ 카테고리 추가',
  set_finance_title: '자산 설정하기',
  set_spending_title: '금액 설정',
  type_plus: '+', // 저장 데이터와 연동되어 있음
  type_minus: '-', // 저장 데이터와 연동되어 있음
  won: '원',
  expected_spending: '예상 비용',
  set_importance_title: '일정 중요도',
  importance_high: '상', // 저장 데이터와 연동되어 있음
  importance_middle: '중', // 저장 데이터와 연동되어 있음
  importance_low: '하', // 저장 데이터와 연동되어 있음
  exclusion_title: '예산에서 제외',
  add_schedule: {
    create: '일정 추가하기',
    read: '문구를 뭐로할까요',
    modify: '일정 삭제하기',
  },
});
const SCHEDULE_DRAWER_MODE = {
  수정: 'modify',
  생성: 'create',
};
const NEED_TITLE = '제목을 입력해야 합니다.';
const INIT_SCHEDULE = {
  event_name: '',
  alarm: false,
  date: new Date(),
  start_time: '09:00',
  end_time: '11:00',
  repeating_cycle: '없음',
  repeat_deadline: '없음',
  repeat_endDate: new Date(),
  category: {},
  type: SCHEDULE_DRAWER.type_minus,
  expected_spending: 0,
  importance: SCHEDULE_DRAWER.importance_middle,
  exclusion: false, // false면 포함
};
const REPEAT_CYCLE = {
  일간: 'days',
  주간: 'weeks',
  월간: 'months',
  연간: 'years',
};
const VIEW_MODE = {
  자산: 'asset',
  일정: 'schedule',
};

export default null;
export {
  SCHEDULE_DRAWER,
  SCHEDULE_DRAWER_MODE,
  NEED_TITLE,
  INIT_SCHEDULE,
  REPEAT_CYCLE,
  VIEW_MODE,
};
