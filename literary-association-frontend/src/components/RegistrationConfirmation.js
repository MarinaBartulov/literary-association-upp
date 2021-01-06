import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { readerService } from "../services/reader-service";

const RegistrationConfirmation = () => {
  const { processId, token } = useParams();
  const [message, setMessage] = useState("");
  const [showSuccess, setShowSuccess] = useState(false);

  const activate = async () => {
    try {
      const response = await readerService.activateAccount(processId, token);
      console.log(response);
      setShowSuccess(true);
      setMessage(response);
    } catch (error) {
      setMessage(error.response ? error.response.data : error.message);
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
