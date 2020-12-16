import React from "react";
import Card from "react-bootstrap/Card";

const SuccessfulTransaction = () => {

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
        <h1>Successful payment transaction!</h1>
      </Card>
    </div>
  );
};

export default SuccessfulTransaction;
