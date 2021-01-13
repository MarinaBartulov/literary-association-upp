import { Route, BrowserRouter as Router, Switch } from "react-router-dom";
import "./App.css";
import ErrorTransaction from "./components/ErrorTransaction";
import FailedTransaction from "./components/FailedTransaction";
import Footer from "./components/Footer";
import Home from "./components/Home";
import RegistrationConfirmation from "./components/RegistrationConfirmation";
import WriterRegistrationConfirmation from "./components/WriterRegisterConfirmation";
import UploadLiteraryWork from "./components/UploadLiteraryWork";
import RegistrationReader from "./components/RegistrationReader";
import SuccessfulTransaction from "./components/SuccessfulTransaction";
import PayTest from "./components/PayTest";
import Login from "./components/Login";

import { ToastContainer } from "react-toastify";
import RegistrationWriter from "./components/RegistrationWriter";
import RegistrationMerchant from "./components/RegistrationMerchant";
import ShoppingCart from "./components/ShoppingCart";
import NewBook from "./components/NewBook";

function App() {
  return (
    <Router>
      <div className="App">
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
            <Route exact path="/registrationWriter/:processId/:taskId">
              <RegistrationWriter />
            </Route>
            <Route exact path="/registrationConfirmation/:processId/:token">
              <RegistrationConfirmation />
            </Route>
            <Route
              exact
              path="/registrationConfirmation/writer/:processId/:token"
            >
              <WriterRegistrationConfirmation />
            </Route>
            <Route exact path="/uploadLiteraryWork/:processId/:taskId">
              <UploadLiteraryWork />
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
            <Route exact path="/login">
              <Login />
            </Route>
            <Route exact path="/registrationMerchant">
              <RegistrationMerchant />
            </Route>
            <Route exact path="/shoppingCart">
              <ShoppingCart />
            </Route>
            <Route exact path="/newBook/:processId/:taskId">
              <NewBook />
            </Route>
          </Switch>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
