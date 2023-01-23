/* eslint-disable no-nested-ternary */
import { Alert, Box } from '@mui/material';
import {
  blue, blueGrey, brown, green, indigo, pink, red,
} from '@mui/material/colors';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import AnalysisGraph from '../../components/analysis/AnalysisGraph';
import AnalysisHeader from '../../components/analysis/AnalysisHeader';
import AnalysisList from '../../components/analysis/analysisList/AnalysisList';
import AnalysisDetailCard from '../../components/analysis/detailCard/AnalysisDetailCard';
import { CATEGORIES } from '../../utils/constants/categories';
// import CATEGORIES from '../../utils/constants/categories';
import { setHeaderOpenTrue } from '../../utils/redux/common/commonSlice';
import { selectSchedules } from '../../utils/redux/schedule/scheduleSlice';

function AnalysisContainer() {
  const [data, setData] = useState([]); // color 추가할 계획
  const [total, setTotal] = useState([]); // color 추가할 계획
  const [showDetailCard, setShowDetailCard] = useState(false);
  const [selectedItem, setSelectedItem] = useState({});
  const schedules = useSelector(selectSchedules);
  const colorList = [
    indigo[100], indigo[200], indigo[300], indigo[400], indigo[500], indigo[600], indigo[700],
    green[100], green[200], green[300],
    blue[100], blue[200], blue[300], blue[400], blue[500], blue[600], blue[700],
    blueGrey[100], blueGrey[200],
    pink[100], red[100], pink[200], red[200], pink[300],
    brown[100], brown[200],
  ];

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(setHeaderOpenTrue('analysis'));
  }, []);

  useEffect(() => {
    const newData = [];
    // eslint-disable-next-line no-unused-vars
    let newTotal = 0;
    const expenditureCategories = CATEGORIES.filter((c) => c.type === '지출' || c.nestedType === '출금');

    // eslint-disable-next-line array-callback-return
    expenditureCategories.map((c, index) => {
      const schByCategory = schedules.filter((s) => s.category.title === c.title);
      const cnt = schByCategory.length;
      if (cnt > 0) {
        // eslint-disable-next-line function-paren-newline
        const spending = schByCategory.reduce(
          // eslint-disable-next-line prefer-arrow-callback
          function (result, schedule) {
            return {
              sum: result.sum + parseInt(schedule.expected_spending, 10),
              history: result.history.concat(schedule),
            };
          }, { sum: 0, history: [] });
        if (spending.sum > 0) {
          newData.push({
            id: c.title,
            label: c.title,
            value: spending.sum,
            color: colorList[index],
            history: spending.history,
          });
          newTotal += spending.sum;
        }
      }
    });
    console.log(newData);
    setTotal(newTotal);
    setData(newData.sort((a, b) => b.value - a.value));
  }, []);

  const clickListItem = (category) => {
    setShowDetailCard(true);
    setSelectedItem(category);
  };

  const closeDetailCard = () => {
    setShowDetailCard(false);
  };

  const hexToRGB = (hex, alpha) => {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);

    if (alpha) {
      return `rgba(${r}, ${g}, ${b}, ${alpha})`;
    }
    return `rgb(${r}, ${g}, ${b})`;
  };

  return (
    <Box>
      <AnalysisHeader />
      {total > 0 ? (
        showDetailCard
          ? <AnalysisDetailCard closeDetailCard={closeDetailCard} selectedItem={selectedItem} />
          : (
            <>
              <Box sx={{ width: '100vw', height: '100vw', padding: 5 }}>
                <AnalysisGraph data={data} total={total} />
              </Box>
              <AnalysisList data={data} clickListItem={clickListItem} hexToRGB={hexToRGB} />
            </>
          )
      ) : <Alert sx={{ margin: 2 }} severity="info">이체/지출 데이터가 존재하지 않습니다.</Alert>}
    </Box>
  );
}
export default AnalysisContainer;
/**
 * 분석 페이지
 */
