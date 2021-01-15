import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { taskService } from "../services/task-service";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import Select from "react-dropdown-select";
import { useParams } from "react-router-dom";
import Header from "./Header";

const TaskForm = () => {
  const params = useParams();
  const [processId, setProcessId] = useState("");
  const [taskId, setTaskId] = useState(params.taskId);
  const [taskName, setTaskName] = useState("");
  const [formFields, setFormFields] = useState([]);
  const [validated, setValidated] = useState(false);
  const [enumValues, setEnumValues] = useState([]);
  const [data, setData] = useState({});
  const history = useHistory();

  const getTask = async () => {
    try {
      const taskData = await taskService.getTask(taskId);
      setFormFields(taskData.formFields);
      setTaskName(taskData.name);
      setProcessId(taskData.processId);

      const temp = [];
      for (let f of taskData.formFields) {
        if (f.typeName === "enum") {
          Object.keys(f.type.values).map((id) => {
            console.log(f.type.value);
            console.log(id);
            temp.push({ id: id, name: f.type.values[id] });
          });
          break;
        }
      }
      console.log(temp);
      setEnumValues(temp);

      const dataTemp = {};
      for (let f of taskData.formFields) {
        if (f.typeName === "string") {
          if (f.defaultValue == null) {
            dataTemp[`${f.id}`] = "";
          } else {
            dataTemp[`${f.id}`] = f.defaultValue;
          }
        }
        if (f.typeName === "long") {
          dataTemp[`${f.id}`] = 0;
        }
        if (f.typeName === "boolean") {
          dataTemp[`${f.id}`] = false;
        }
        if (f.typeName === "enum") {
          dataTemp[`${f.id}`] = [];
        }
      }

      setData(dataTemp);
      console.log(dataTemp);
      console.log(data);
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
    getTask();
  }, []);

  const handleChange = (e) => {
    console.log(e.target);
    const { id, value } = e.target;
    console.log(id);
    console.log(value);
    console.log(data);
    setData((prevState) => ({
      ...prevState,
      [id]: value,
    }));
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
      //ovo se radi da bi se uzeo novi id taska ako dodje do greske u validaciji
      //task na koji se vrati ima drugaciji id
      const response = await taskService.getCurrentTaskIdForProcess(processId);
      if (response != null) {
        setTaskId(response);
      }
    }
  };

  return (
    <div>
      <Header />
      <h2>{taskName}</h2>
      <Form
        validated={validated}
        style={{
          width: "30%",
          margin: "auto",
        }}
        onSubmit={submitForm}
      >
        {formFields.map((formField) => {
          const { id, label, typeName, properties, defaultValue } = formField;
          return (
            <>
              {typeName === "string" && properties.text_area === undefined && (
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
                    value={defaultValue == null ? data[id] : defaultValue}
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    readOnly={formField.properties.readonly !== undefined}
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
              {typeName === "string" && properties.text_area !== undefined && (
                <Form.Group key={id} controlId={id}>
                  <Form.Label>{label}</Form.Label>
                  <Form.Control
                    as="textarea"
                    onChange={handleChange}
                    placeholder={"Enter " + label}
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    value={defaultValue == null ? data[id] : defaultValue}
                    readOnly={formField.properties.readonly !== undefined}
                    rows={5}
                  />
                </Form.Group>
              )}
              {typeName === "long" && (
                <Form.Group key={id} controlId={id}>
                  <Form.Label>{label}:</Form.Label>
                  <Form.Control
                    type="number"
                    onChange={handleChange}
                    placeholder={"Enter " + label}
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    value={defaultValue == null ? data[id] : defaultValue}
                    readOnly={formField.properties.readonly !== undefined}
                    min={
                      formField.validationConstraints.some(
                        (c) => c.name === "min"
                      )
                        ? formField.validationConstraints.find(
                            (c) => c.name === "min"
                          ).configuration
                        : undefined
                    }
                    max={
                      formField.validationConstraints.some(
                        (c) => c.name === "max"
                      )
                        ? formField.validationConstraints.find(
                            (c) => c.name === "max"
                          ).configuration
                        : undefined
                    }
                  />
                </Form.Group>
              )}
              {typeName === "boolean" && (
                <Form.Group key={id} controlId={id}>
                  <Form.Check
                    type="checkbox"
                    onChange={(e) => {
                      setData((prevState) => ({
                        ...prevState,
                        [formField.id]: e.target.checked,
                      }));
                    }}
                    readOnly={formField.properties.readonly !== undefined}
                    label={label}
                  />
                </Form.Group>
              )}
              {typeName === "enum" && (
                <>
                  <label key={"label-" + id}>{label}</label>
                  <Select
                    key={id}
                    placeholder={"Select " + label}
                    multi={properties.multiselect !== undefined}
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    readOnly={formField.properties.readonly !== undefined}
                    options={enumValues}
                    style={{ backgroundColor: "white", marginBottom: "1em" }}
                    labelField="name"
                    valueField="id"
                    onChange={(values) => {
                      if (properties.multiselect !== undefined) {
                        console.log(values.map((v) => v.name));
                        setData((prevState) => ({
                          ...prevState,
                          [formField.id]: values.map((v) => v.name),
                        }));
                      } else {
                        console.log(values[0].name);
                        setData((prevState) => ({
                          ...prevState,
                          [formField.id]: values[0].name,
                        }));
                      }
                    }}
                  />
                </>
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

export default TaskForm;
