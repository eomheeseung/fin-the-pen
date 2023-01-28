/* eslint-disable no-unused-vars */

const FIXED = {
  type: '고정 입출금',
  nested: [
    {
      type: '입금',
      categories: ['급여'],
      color: '#FF0000',
    },
    {
      type: '출금',
      categories: ['구독비', '교통비/자동차세', '(주거)관리비/월세', '핸드폰요금', '적금', '보험비', '카드이체료'],
      color: '#0000FF',
    },
  ],
};

const INCOME = {
  type: '수입',
  nested: [
    {
      type: '용돈',
      categories: ['용돈'],
      color: '#800080',
    },
    {
      type: '수익',
      categories: ['금융', '사업', '기타'],
      color: '#FFD700',
    },
  ],
};

const EXPENDITURE = {
  type: '지출',
  nested: [
    {
      type: '음식',
      categories: ['식비', '술', '카페'],
      color: '#008000',
    },
    {
      type: '생활',
      categories: ['생필품', '주거', '통신', '자녀/육아', '반려동물', '교육/학습', '의료/건강'],
      color: '#87CEEB',
    },
    {
      type: '교통',
      categories: ['자동차', '대중교통'],
      color: '#808080',
    },
    {
      type: '문화',
      categories: ['온라인쇼핑', '패션', '미용', '영화/전시', '여행/숙박'],
      color: '#FFC0CB',
    },
    {
      type: '금융',
      categories: ['저축', '투자'],
      color: '#A52A2A',
    },
  ],
};

const categoryFlatter = (obj) => {
  const flatNestedCategories = (type, nestedType, categories, color) => categories.map((title) => ({
    type,
    nestedType,
    title,
    color,
  }));
  const { type } = obj;
  return obj.nested
    .map((cat) => flatNestedCategories(type, cat.type, cat.categories, cat.color))
    .flat(Infinity);
};

const CATEGORIES = Object.freeze(
  [...categoryFlatter(FIXED), ...categoryFlatter(INCOME), ...categoryFlatter(EXPENDITURE)],
);

export default null;
export {
  CATEGORIES, FIXED, INCOME, EXPENDITURE,
};
