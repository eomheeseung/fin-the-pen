/* eslint-disable react/jsx-props-no-spreading */
import {
  Autocomplete, TextField,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { CATEGORIES } from '../../../../utils/constants/categories';
import { SCHEDULE_DRAWER } from '../../../../utils/constants/schedule';
import { selectSchedule } from '../../../../utils/redux/schedule/scheduleSlice';
import { updateCategory } from '../utils/schedule';

export default function CategoryInput({ selected }) {
  const dispatch = useDispatch();
  const schedule = useSelector(selectSchedule);

  const [value, setValue] = useState(selected);
  const [inputValue, setInputValue] = useState('');

  useEffect(() => {
    if (value) {
      const category = CATEGORIES.filter((cat) => cat.title === value);
      if (category.length > 0) {
        updateCategory(dispatch, schedule, category[0].title);
      }
    }
  }, [value]);

  return (
    <div>
      <Autocomplete
        value={value}
        onChange={(event, newValue) => {
          setValue(newValue);
        }}
        inputValue={inputValue}
        onInputChange={(event, newInputValue) => {
          setInputValue(newInputValue);
        }}
        id="category"
        options={CATEGORIES.map((cat) => cat.title)}
        renderInput={(params) => (
          <TextField
            {...params}
            label={SCHEDULE_DRAWER.category_title}
          />
        )}
        size="small"
      />
    </div>
  );
}
