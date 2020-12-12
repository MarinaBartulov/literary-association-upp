import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { readerService } from "../services/reader-service";

const RegistrationConfirmation = () => {
  const { processId, token } = useParams();
  const [message, setMessage] = useState("");
  const [showSuccess, setShowSuccess] = useState(false);

  const activate = async () => {
    const response = await readerService.activateAccount(processId, token);
    console.log(response);
    if (response != undefined) {
      setShowSuccess(true);
      setMessage(response.data);
    } else {
      setMessage("Account activation failed. Token expired.");
    }
  };

  useEffect(() => {
    activate();
  }, []);

  return (
    <div>
      <h2 style={{ color: showSuccess ? "green" : "red" }}>{message}</h2>
    </div>
  );
};

export default RegistrationConfirmation;
