/* eslint-disable react/jsx-props-no-spreading */
import {
  Autocomplete, TextField,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { CATEGORIES } from '../../../../utils/constants/categories';
import { ADD_SCHEDULE } from '../../../../utils/constants/schedule';

export default function CategoryInput({ updateCategory, selected }) {
  const [value, setValue] = useState(selected);
  const [inputValue, setInputValue] = useState('');
  useEffect(() => {
    if (value) {
      const category = CATEGORIES.filter((cat) => cat.title === value);
      if (category.length > 0) {
        updateCategory(category[0]);
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
        renderInput={(params) => <TextField {...params} label={ADD_SCHEDULE.category_title} />}
        size="small"
      />
    </div>
  );
}
