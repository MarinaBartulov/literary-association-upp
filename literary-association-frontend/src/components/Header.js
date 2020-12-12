import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <div>
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="#home">Literary Association</Navbar.Brand>
        <Nav className="mr-auto">
          <Link className="ml-2" to="/home">
            Home
          </Link>
          <Link className="ml-2" to="/registrationReader">
            Register reader
          </Link>
        </Nav>
      </Navbar>
    </div>
  );
};

export default Header;
