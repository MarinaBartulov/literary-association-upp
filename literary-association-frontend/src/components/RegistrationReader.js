import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { taskService } from "../services/task-service";
import { useHistory, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import Select from "react-dropdown-select";

const RegistrationReader = () => {
  const params = useParams();
  const [processId, setProcessId] = useState(params.processId);
  const [taskId, setTaskId] = useState(params.taskId);
  const [formFields, setFormFields] = useState([]);
  const [validated, setValidated] = useState(false);
  const [enumValues, setEnumValues] = useState([]);
  const [data, setData] = useState({});
  const history = useHistory();

  const getFormData = async () => {
    try {
      const taskFormData = await taskService.getFormFields(taskId);
      setFormFields(taskFormData.formFields);

      const temp = [];
      for (let f of taskFormData.formFields) {
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
      for (let f of taskFormData.formFields) {
        if (f.typeName === "string") {
          dataTemp[`${f.id}`] = "";
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
    getFormData();
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

  const commitForm = async (event) => {
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
      const response = await taskService.commitForm(sendData, taskId);
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
      <h2>Reader registration</h2>
      <Form
        validated={validated}
        style={{
          width: "30%",
          margin: "auto",
        }}
        onSubmit={commitForm}
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
                        : "text"
                    }
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
                    readOnly={formField.validationConstraints.some(
                      (c) => c.name === "readonly"
                    )}
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
                    onChange={(e) =>
                      setData((prevState) => ({
                        ...prevState,
                        [formField.id]: e.target.value === "on" ? true : false,
                      }))
                    }
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
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
                    options={enumValues}
                    style={{ backgroundColor: "white", marginBottom: "1em" }}
                    labelField="name"
                    valueField="id"
                    onChange={(values) =>
                      setData((prevState) => ({
                        ...prevState,
                        [formField.id]: values,
                      }))
                    }
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
          Register
        </Button>
      </Form>
    </div>
  );
};

export default RegistrationReader;
