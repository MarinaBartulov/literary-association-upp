import { Route, BrowserRouter as Router, Switch } from "react-router-dom";
import "./App.css";
import ErrorTransaction from "./components/ErrorTransaction";
import FailedTransaction from "./components/FailedTransaction";
import Footer from "./components/Footer";
import Header from "./components/Header";
import Home from "./components/Home";
import RegistrationConfirmation from "./components/RegistrationConfirmation";
import RegistrationReader from "./components/RegistrationReader";
import SuccessfulTransaction from "./components/SuccessfulTransaction";
import PayTest from "./components/PayTest";
import { ToastContainer } from "react-toastify";

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <div className="mainDiv">
          <ToastContainer
            position="top-right"
            autoClose={5000}
            hideProgressBar={true}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
          />
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/home">
              <Home />
            </Route>
            <Route exact path="/registrationReader/:processId/:taskId">
              <RegistrationReader />
            </Route>
            <Route exact path="/registrationConfirmation/:processId/:token">
              <RegistrationConfirmation />
            </Route>
            <Route exact path="/success">
              <SuccessfulTransaction />
            </Route>
            <Route exact path="/failed">
              <FailedTransaction />
            </Route>
            <Route exact path="/error">
              <ErrorTransaction />
            </Route>
            <Route exact path="/payTest">
              <PayTest />
            </Route>
          </Switch>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
