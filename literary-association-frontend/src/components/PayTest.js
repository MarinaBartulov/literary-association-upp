import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";

const PayTest = () => {
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
    </div>
  );
};

export default PayTest;
