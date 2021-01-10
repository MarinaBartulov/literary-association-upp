import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { taskService } from "../services/task-service";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";

const UploadLiteraryWork = () => {
  const params = useParams();
  const [processId, setProcessId] = useState(params.processId);
  const [taskId, setTaskId] = useState(params.taskId);

  const [formFields, setFormFields] = useState([]);
  const [validated, setValidated] = useState(false);
  const [data, setData] = useState({});
  const history = useHistory();
  //const [selectedFiles, setSelectedFiles] = useState(null);
  var selectedFiles = FileList;
  const getFormData = async () => {
    try {
      const taskFormData = await taskService.getFormFields(taskId);
      setFormFields(taskFormData.formFields);

      /*const dataTemp = {};
      for (let f of taskFormData.formFields) {
        if (f.typeName === "string") {
          dataTemp[`${f.id}`] = "";
        }
      }

      setData(dataTemp);
      console.log(dataTemp);
      console.log(data);*/
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  useEffect(() => {
    getFormData();
  }, []);

  const handleChange = (e) => {
    selectedFiles = e.target.files;
    console.log(selectedFiles);
    //setData({});
    //setData({ fileNumber: selectedFiles.length });
    for (let i = 0; i < selectedFiles.length; i++) {
      const id = i;
      setData((prevState) => ({
        ...prevState,
        [id]: e.target.files[i].name,
      }));
      console.log(i);
      console.log(e.target.files[i].name);
    }
  };

  const submitForm = async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    }
    setValidated(true);
    console.log(data);
    const sendData = [];
    for (let f in data) {
      sendData.push({ fieldId: f, fieldValue: data[f] });
    }
    console.log(sendData);

    try {
      //za svaki od fajlova posebno pozvati funkciju za uploadovanje fajla
      // ... //
      const response = await taskService.submitForm(sendData, taskId);
      console.log(response);
      toast.success(response, {
        hideProgressBar: true,
      });
      history.push("/home");
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  return (
    <div>
      <Form
        validated={validated}
        style={{
          width: "30%",
          margin: "auto",
        }}
        onSubmit={submitForm}
      >
        {formFields.map((formField) => {
          const { id, label, typeName, properties } = formField;
          return (
            <>
              {typeName === "string" && (
                <Form.Group key={id} controlId={id}>
                  <Form.Label>{label}:</Form.Label>
                  <Form.Control
                    type={
                      properties.email !== undefined
                        ? "email"
                        : properties.password !== undefined
                        ? "password"
                        : properties.file !== undefined
                        ? "file"
                        : "text"
                    }
                    multiple={properties.multiple !== undefined ? true : false}
                    onChange={handleChange}
                    placeholder={"Enter " + label}
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    readOnly={formField.validationConstraints.some(
                      (c) => c.name === "readonly"
                    )}
                    minLength={
                      formField.validationConstraints.some(
                        (c) => c.name === "minlength"
                      )
                        ? formField.validationConstraints.find(
                            (c) => c.name === "minlength"
                          ).configuration
                        : undefined
                    }
                    maxLength={
                      formField.validationConstraints.some(
                        (c) => c.name === "maxlength"
                      )
                        ? formField.validationConstraints.find(
                            (c) => c.name === "maxlength"
                          ).configuration
                        : undefined
                    }
                  />
                </Form.Group>
              )}
            </>
          );
        })}

        <Button
          variant="primary"
          type="submit"
          style={{ marginBottom: "1em", marginTop: "1em" }}
        >
          Submit
        </Button>
      </Form>
    </div>
  );
};

export default UploadLiteraryWork;
