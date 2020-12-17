import React from "react";
import Card from "react-bootstrap/Card";

const ErrorTransaction = () => {

  return (
    <div className="homeDiv">
      <Card
        body
        style={{
          width: "25rem",
          margin: "auto",
          backgroundColor: "rgb(207, 97, 64)",
        }}
      >
        <h1>Payment transaction ended with an error!</h1>
      </Card>
    </div>
  );
};

export default ErrorTransaction;
