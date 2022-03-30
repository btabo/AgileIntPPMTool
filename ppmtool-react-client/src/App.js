import logo from './logo.svg';
import './App.css';
import Dashboard from './components/Dashboard';
import Header from './components/Layer/Header';
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AddProject from "./components/Project/AddProject";
import {Provider} from "react-redux";
import store from './store';

function App() {
  return (    
      <Provider store={store}>
        <div className="App">
            <Header />
            <Router>
              <Routes>
                <Route exact path="/" element={<Dashboard />} />  
                <Route exact path="/dashboard" element={<Dashboard />} />
                <Route exact path="/addProject" element={<AddProject />} />
              </Routes>            
            </Router>
        </div>
      </Provider>
  );
}

export default App;
