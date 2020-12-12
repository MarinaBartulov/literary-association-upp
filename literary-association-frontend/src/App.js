import { Route, BrowserRouter as Router, Switch } from "react-router-dom";
import "./App.css";
import Footer from "./components/Footer";
import Header from "./components/Header";
import Home from "./components/Home";
import RegistrationConfirmation from "./components/RegistrationConfirmation";
import RegistrationReader from "./components/RegistrationReader";

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <div className="mainDiv">
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/home">
              <Home />
            </Route>
            <Route exact path="/registrationReader">
              <RegistrationReader />
            </Route>
            <Route exact path="/registrationConfirmation/:processId/:token">
              <RegistrationConfirmation />
            </Route>
          </Switch>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
