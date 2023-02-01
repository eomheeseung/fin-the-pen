import './App.css';
import { Route, Routes } from 'react-router-dom';
import HomeLayout from './components/layouts/HomeLayout';
import homeRoutes from './utils/routes/homeRoutes';
import managementRoutes from './utils/routes/managementRoutes';
import ManagementLayout from './components/layouts/ManagementLayout';

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomeLayout />}>
        {
          homeRoutes
            .map((route) => <Route path={route.path} element={route.element} key={route.path} />)
        }
        <Route path="/management" element={<ManagementLayout />}>
          {
            managementRoutes
              .map((route) => <Route path={route.path} element={route.element} key={route.path} />)
          }
        </Route>
      </Route>
    </Routes>
  );
}

export default App;
