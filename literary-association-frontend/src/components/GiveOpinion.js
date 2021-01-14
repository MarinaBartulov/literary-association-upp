import { useState } from "react";
import { useParams, useHistory } from "react-router-dom";
import GenericForm from "./GenericForm";

import Card from "react-bootstrap/Card";

const GiveOpinion = () => {
  const params = useParams();
  const [processId, setProcessId] = useState(params.processId);
  const [taskId, setTaskId] = useState(params.taskId);

  return (
    <div className="pt-3">
      <Card
        body
        style={{
          width: "70%",
          margin: "auto",
          marginTop: "2rem",
          marginBottom: "2rem",
          backgroundColor: "rgb(169, 169, 169)",
        }}
      >
        <h2>Give Opinion</h2>
        <GenericForm processId={processId} taskId={taskId}></GenericForm>
      </Card>
    </div>
  );
};

export default GiveOpinion;
