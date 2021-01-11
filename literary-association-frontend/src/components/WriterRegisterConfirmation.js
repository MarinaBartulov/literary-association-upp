import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Button from "react-bootstrap/Button";
import { useHistory } from "react-router-dom";
import { writerService } from "../services/writer-service";

const WriterRegistrationConfirmation = () => {
  const { processId, token } = useParams();
  const [message, setMessage] = useState("");
  const [showSuccess, setShowSuccess] = useState(false);
  const history = useHistory();

  const activate = async () => {
    try {
      const response = await writerService.activateAccount(processId, token);
      setMessage(response);
      setShowSuccess(true);
    } catch (error) {
      setMessage(error.response ? error.response.data : error.message);
    }
  };

  const getUploadLWTaskId = async () => {
    try {
      const response = await writerService.getUploadLWTaskId(processId);
      console.log("Odgovor iz getUploadLWTaskId - taskId");
      console.log(response.taskId);
      history.push(
        "/uploadLiteraryWork/" + response.processId + "/" + response.taskId
      );
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    activate();
  }, []);

  return (
    <div>
      <h2 style={{ color: showSuccess ? "green" : "red" }}>{message}</h2>
      {showSuccess}
      <Button
        style={{ marginTop: "1em" }}
        className="ml-2"
        variant="info"
        onClick={getUploadLWTaskId}
      >
        Continue registration process
      </Button>
    </div>
  );
};

export default WriterRegistrationConfirmation;
