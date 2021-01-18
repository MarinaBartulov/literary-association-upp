import React from "react";
import { useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { taskService } from "../services/task-service";
import Button from "react-bootstrap/Button";

const WriterMoreMaterialNeeded = () => {
  const { processId } = useParams();
  const history = useHistory();

  const getTaskId = async () => {
    try {
      const response = await taskService.getAssigneesTaskId(processId);
      console.log("taskId");
      console.log(response);
      history.push("/uploadLiteraryWork/" + processId + "/" + response);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div>
      <h2> More Material Needed </h2>
      <Button
        style={{ marginTop: "1em" }}
        className="ml-2"
        variant="info"
        onClick={getTaskId}
      >
        Upload Literary Work
      </Button>
    </div>
  );
};

export default WriterMoreMaterialNeeded;
