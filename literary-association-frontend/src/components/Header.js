import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import { useHistory } from "react-router-dom";
import { readerService } from "../services/reader-service";
import { toast } from "react-toastify";

const Header = () => {
  const history = useHistory();

  const startReaderRegistration = async () => {
    console.log("Registration starting...");
    try {
      const response = await readerService.startReg();
      history.push(
        "/registrationReader/" + response.processId + "/" + response.taskId
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

  const startWriterRegistration = async () => {};

  const goToHomePage = () => {
    history.push("/home");
  };
  return (
    <div>
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="#home">Literary Association</Navbar.Brand>
        <Nav className="mr-auto">
          <Button className="ml-2" variant="link" onClick={goToHomePage}>
            Home
          </Button>
          <Button
            className="ml-2"
            variant="link"
            onClick={startReaderRegistration}
          >
            Register reader
          </Button>
          <Button
            className="ml-2"
            variant="link"
            onClick={startWriterRegistration}
          >
            Register writer
          </Button>
        </Nav>
      </Navbar>
    </div>
  );
};

export default Header;
