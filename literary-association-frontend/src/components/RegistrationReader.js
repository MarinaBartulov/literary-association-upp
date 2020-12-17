import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { readerService } from "../services/reader-service";
import { genreService } from "../services/genre-service";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import Select from "react-dropdown-select";

const RegistrationReader = () => {
  const [processInstanceId, setProcessInstanceId] = useState("");
  const [taskId, setTaskId] = useState("");
  const [formFields, setFormFields] = useState([]);
  const [validated, setValidated] = useState(false);
  const [genres, setGenres] = useState([]);
  const [reader, setReader] = useState({});
  const history = useHistory();

  const getFormData = async () => {
    const taskFormData = await readerService.regFormFields();
    setProcessInstanceId(taskFormData.processInstanceId);
    setTaskId(taskFormData.taskId);
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
    setGenres(temp);

    const readerTemp = {};
    for (let f of taskFormData.formFields) {
      if (f.typeName === "string") {
        readerTemp[`${f.id}`] = "";
      }
      if (f.typeName === "long") {
        readerTemp[`${f.id}`] = 0;
      }
      if (f.typeName === "boolean") {
        readerTemp[`${f.id}`] = "off";
      }
      if (f.typeName === "enum") {
        readerTemp[`${f.id}`] = [];
      }
    }

    setReader(readerTemp);
    console.log(readerTemp);
    console.log(reader);
  };

  useEffect(() => {
    getFormData();
  }, []);

  const handleChange = (e) => {
    console.log(e.target);
    const { id, value } = e.target;
    console.log(id);
    console.log(value);
    console.log(reader);
    setReader((prevState) => ({
      ...prevState,
      [id]: value,
    }));
  };

  const registerReader = (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    }
    setValidated(true);
    console.log(reader);
    const sendData = [];
    for (let f in reader) {
      if (f === "betaReader") {
        if (reader[f] == "on") {
          sendData.push({ fieldId: f, fieldValue: true });
        } else {
          sendData.push({ fieldId: f, fieldValue: false });
        }
      } else if (f === "genres" || f === "betaGenres") {
        // const list = [];
        // for (let i = 0; i < reader[f].length; i++) {
        //   const map = {};
        //   map["item_id"] = reader[f][i].id.toString();
        //   map["item_name"] = reader[f][i].name;
        //   list.push(map);
        // }
        // sendData.push({ fieldId: f, fieldValue: list });
      } else {
        sendData.push({ fieldId: f, fieldValue: reader[f] });
      }
    }
    console.log(sendData);
    const promise = readerService.regReader(sendData, taskId);
    promise.then((res) => {
      toast.success("Registration successful! Email confirmation required.", {
        hideProgressBar: true,
      });
      history.push("/home");
    });
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
        onSubmit={registerReader}
      >
        {formFields.map((formField) => {
          const { id, label, typeName } = formField;
          return (
            <>
              {typeName === "string" && (
                <Form.Group key={id} controlId={id}>
                  <Form.Label>{label}:</Form.Label>
                  <Form.Control
                    type={
                      id.includes("email")
                        ? "email"
                        : id.toLowerCase().includes("password")
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
                    onChange={handleChange}
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    label={label}
                  />
                </Form.Group>
              )}
              {typeName === "enum" && (
                // <Form.Group key={id} controlId={id}>
                //   <Form.Label>{label}:</Form.Label>
                //   <Form.Control
                //     as="select"
                //     placeholder={"Enter " + label}
                //     onChange={handleChange}
                //     // required={formField.validationConstraints.some(
                //     //   (c) => c.name === "required"
                //     // )}
                //     multiple
                //   >
                //     {Object.keys(formField.type.values).map((id) => {
                //       return (
                //         <option key={id} value={id}>
                //           {formField.type.values[id]}
                //         </option>
                //       );
                //     })}
                //   </Form.Control>
                // </Form.Group>
                <>
                  <label>{label}</label>
                  <Select
                    key={id}
                    placeholder={"Select " + label}
                    multi
                    required={formField.validationConstraints.some(
                      (c) => c.name === "required"
                    )}
                    options={genres}
                    style={{ backgroundColor: "white", marginBottom: "1em" }}
                    labelField="name"
                    valueField="id"
                    onChange={(values) =>
                      setReader((prevState) => ({
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
