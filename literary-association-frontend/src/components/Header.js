import React, { useState } from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import { useHistory } from "react-router-dom";
import { readerService } from "../services/reader-service";
import { writerService } from "../services/writer-service";
import { bookRequestService } from "../services/book-request-service";
import { toast } from "react-toastify";

const Header = () => {
  const [loggedIn, setLoggedIn] = useState(
    localStorage.getItem("token") !== "null" &&
      localStorage.getItem("token") !== null
  );
  const [role, setRole] = useState(localStorage.getItem("role"));
  const history = useHistory();

  const startReaderRegistration = async () => {
    console.log("Reader registration started...");
    try {
      const response = await readerService.startReg();
      history.push("/task/" + response.taskId);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const startWriterRegistration = async () => {
    console.log("Registration starting...");
    try {
      const response = await writerService.startReg();
      history.push(
        "/registrationWriter/" + response.processId + "/" + response.taskId
      );
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };
  const logout = () => {
    localStorage.setItem("token", null);
    localStorage.setItem("role", null);
    localStorage.setItem("currentUserId", null);
    //history.push("/home");
    setLoggedIn(false);
    history.push("/home");
  };
  const goToHomePage = () => {
    history.push("/home");
  };
  const goToLogin = () => {
    history.push("/login");
  };
  const goToRegisterMerchant = () => {
    history.push("/registrationMerchant");
  };
  const goToShoppingCart = () => {
    history.push("/shoppingCart");
  };

  const goToBoardMemberPanel = () => {
    history.push("/boardMember");
  };

  const startBookPublishing = async () => {
    console.log("Publishing started...");
    try {
      const response = await bookRequestService.startBookPublishing();
      history.push("/task/" + response.taskId);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const goToAllTasks = () => {
    history.push("/allTasks");
  };
  return (
    <div>
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="#home">Literary Association</Navbar.Brand>
        <Nav className="mr-auto">
          <Button className="ml-2" variant="link" onClick={goToHomePage}>
            Home
          </Button>
          {!loggedIn && (
            <Button
              className="ml-2"
              variant="link"
              onClick={startReaderRegistration}
            >
              Reader registration
            </Button>
          )}
          {!loggedIn && (
            <Button
              className="ml-2"
              variant="link"
              onClick={startWriterRegistration}
            >
              Writer registration
            </Button>
          )}
          {loggedIn && role === "ROLE_ADMIN" && (
            <Button
              className="ml-2"
              variant="link"
              onClick={goToRegisterMerchant}
            >
              Register merchant
            </Button>
          )}
          {loggedIn && role === "ROLE_WRITER" && (
            <Button
              className="ml-2"
              variant="link"
              onClick={startBookPublishing}
            >
              Publish a book
            </Button>
          )}

          <Button className="ml-2" variant="link" onClick={goToShoppingCart}>
            Shopping cart
          </Button>
          {loggedIn && role !== "ROLE_BOARD_MEMBER" && (
            <Button className="ml-2" variant="link" onClick={goToAllTasks}>
              My tasks
            </Button>
          )}
          {!loggedIn && (
            <Button className="ml-2" variant="link" onClick={goToLogin}>
              Login
            </Button>
          )}
          {loggedIn && role === "ROLE_BOARD_MEMBER" && (
            <Button
              className="ml-2"
              variant="link"
              onClick={goToBoardMemberPanel}
            >
              My Tasks
            </Button>
          )}
          {loggedIn && (
            <Button className="ml-2" variant="link" onClick={logout}>
              Logout
            </Button>
          )}
        </Nav>
      </Navbar>
    </div>
  );
};

export default Header;
