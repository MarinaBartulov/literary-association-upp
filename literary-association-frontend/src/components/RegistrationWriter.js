import React, { useState } from "react";
import { useParams } from "react-router-dom";
import GenericForm from "./GenericForm";

const RegistrationWriter = () => {
  const params = useParams();
  const [processId, setProcessId] = useState(params.processId);
  const [taskId, setTaskId] = useState(params.taskId);
  return (
    <div>
      <h2>Writer registration</h2>
      <GenericForm processId={processId} taskId={taskId}></GenericForm>
    </div>
  );
};

export default RegistrationWriter;
