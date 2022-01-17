import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import IndexPage from '../src/pages/IndexPage'
import IzvjestajPage from '../src/pages/IzvjestajPage'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<IndexPage />} />
        <Route path="/izvjestaj" element={<IzvjestajPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
