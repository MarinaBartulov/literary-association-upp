import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";

const PayTest = () => {
  function pay() {
    testService.test({ price: "200" });
  }
  return (
    <div>
      <h1>Literary association</h1>
      <header className="App-header">
        <p>
          <Button
            variant="dark"
            onClick={() => {
              pay();
            }}
          >
            {" "}
            Pay{" "}
          </Button>{" "}
        </p>
      </header>
    </div>
  );
};

export default PayTest;
