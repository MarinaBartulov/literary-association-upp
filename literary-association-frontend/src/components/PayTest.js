import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";

const PayTest = () => {
  function pay() {
    let payload = {
      price: "200",
      currency: "RSD"
    };
    testService.test(payload);
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
        
    </div>
  );
};

export default PayTest;
