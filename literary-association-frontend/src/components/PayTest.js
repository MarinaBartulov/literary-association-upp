import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";
import { writerService } from "../services/writer-service";
import { toast } from "react-toastify";

const PayTest = () => {
  const [loggedIn, setLoggedIn] = useState(
    localStorage.getItem("token") !== "null" &&
      localStorage.getItem("token") !== null
  );
  const [role, setRole] = useState(localStorage.getItem("role"));

  function pay() {
    let payload = {
      price: "10",
      currency: "USD",
    };
    testService.pay(payload);
  }
  function subscribe() {
    let payload = {
      price: "10",
      currency: "USD",
    };
    testService.subscribe(payload);
  }

  const membershipFeePayment = async () => {
    console.log("Membership fee payment started...");
    try {
      const response = await writerService.membershipFeePayment();
      toast.success("Membership Fee Successfully paid!", {
        hideProgressBar: true,
      });
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };
  return (
    <div>
      <Button
        variant="dark"
        onClick={() => {
          pay();
        }}
      >
        {" "}
        Pay{" "}
      </Button>{" "}
      <Button
        variant="warning"
        onClick={() => {
          subscribe();
        }}
      >
        {" "}
        Subscribe{" "}
      </Button>
      <br />
      {loggedIn && role === "ROLE_WRITER" && (
        <Button variant="info" onClick={membershipFeePayment}>
          Membership Fee Payment
        </Button>
      )}
    </div>
  );
};

export default PayTest;
