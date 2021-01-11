import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import UploadForm from "./UploadForm";

const UploadLiteraryWork = () => {
  const { processId, taskId } = useParams();

  return (
    <div>
      <h1> Upload Literary Work </h1>
      <UploadForm processId={processId} taskId={taskId}></UploadForm>
    </div>
  );
};

export default UploadLiteraryWork;
