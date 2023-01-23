/* eslint-disable */
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';

// import { store } from 'api/redux/store';
import { Provider } from 'react-redux';

import { PersistGate } from 'redux-persist/integration/react';
import { persistStore } from 'redux-persist';
import { store } from './utils/redux/store';
import { createTheme, responsiveFontSizes, ThemeProvider } from '@mui/material';

const theme = responsiveFontSizes(createTheme({
  palette:{
    type: 'light',
    primary: {
      main: '#7c4dff',
    },
    secondary: {
      main: '#f50057',
    },
  }
}));

const root = ReactDOM.createRoot(document.getElementById('root'));
let persistor = persistStore(store);
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <BrowserRouter basename='fin-the-pen-web'>
          <ThemeProvider theme={theme}>
          <App />
          </ThemeProvider>
        </BrowserRouter>
      </PersistGate>
    </Provider>
  </React.StrictMode>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
