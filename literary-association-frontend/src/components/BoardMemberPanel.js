import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import Header from "./Header";
import { boardMemberService } from "../services/board-member-service";

import Button from "react-bootstrap/Button";
import { Table } from "react-bootstrap";
import { toast } from "react-toastify";

const BoardMemberPanel = () => {
  const [membershipApplications, setMembershipApplicatios] = useState([]);
  const history = useHistory();

  useEffect(() => {
    getMembershipApplications();
  }, []);

  const getMembershipApplications = async () => {
    try {
      const response = await boardMemberService.getAllMembershipApplications();
      setMembershipApplicatios(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const checkoutMembershipApplication = (id, processId) => {
    console.log(id);
    console.log(processId);
    history.push("/membershipApplication/" + id + "/" + processId);
  };

  return (
    <div>
      <Header />
      <h2> Membership Applications </h2>
      <div
        style={{ width: "50%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto"
      >
        <Table>
          <thead>
            <tr>
              <th>Id</th>
              <th>Writer name</th>
              <th>Writer surname </th>
              <th>More material requested</th>
              <th>Checkout </th>
            </tr>
          </thead>
          <tbody>
            {membershipApplications.map((membershipApplication) => {
              return (
                <tr key={membershipApplication.id}>
                  <td> #{membershipApplication.id}</td>
                  <td>{membershipApplication.writerFirstName}</td>
                  <td>{membershipApplication.writerLastName}</td>
                  <td>{membershipApplication.moreMaterialRequested}</td>
                  <td>
                    <Button
                      variant="dark"
                      onClick={() => {
                        checkoutMembershipApplication(
                          membershipApplication.id,
                          membershipApplication.processId
                        );
                      }}
                    >
                      Checkout
                    </Button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </div>
    </div>
  );
};

export default BoardMemberPanel;
