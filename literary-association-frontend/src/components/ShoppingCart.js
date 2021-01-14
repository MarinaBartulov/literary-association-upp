import React, { useState, useEffect } from "react";
import { merchantService } from "../services/merchant-service";
import { orderService } from "../services/order-service";
import Button from "react-bootstrap/Button";

import { toast } from "react-toastify";
import { Table } from "react-bootstrap";
import Header from "./Header";

const ShoppingCart = () => {
  const [merchants, setMerchants] = useState([]);

  const getActiveMerchants = async () => {
    try {
      const response = await merchantService.getActiveMerchants();
      console.log(response);
      setMerchants(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const createOrder = async (merchantId) => {
    let payload = {
      amount: "10",
      currency: "USD",
      merchantId: merchantId,
    };
    try {
      const response = await orderService.createOrder(payload);
      window.open(response.redirectionURL);
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
    getActiveMerchants();
  }, []);

  return (
    <div>
      <Header />
      <h2>Shopping cart</h2>
      <div
        style={{ width: "40%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto"
      >
        <Table>
          <thead>
            <tr>
              <th>Merchant</th>
              <th>Create order</th>
            </tr>
          </thead>
          <tbody>
            {merchants.map((merchant) => {
              return (
                <tr key={merchant.id}>
                  <td>{merchant.name}</td>
                  <td>
                    <Button
                      variant="dark"
                      onClick={() => {
                        createOrder(merchant.id);
                      }}
                    >
                      Create order
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

export default ShoppingCart;
