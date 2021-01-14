import React, { useState } from "react";
import { useParams } from "react-router-dom";
import GenericForm from "./GenericForm";
import Header from "./Header";

const NewBook = () => {
  const params = useParams();
  const [processId, setProcessId] = useState(params.processId);
  const [taskId, setTaskId] = useState(params.taskId);
  return (
    <div>
      <Header />
      <h2>New book</h2>
      <GenericForm processId={processId} taskId={taskId}></GenericForm>
    </div>
  );
};

export default NewBook;
