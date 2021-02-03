import React, { useState, useEffect } from "react";
import { orderService } from "../services/order-service";
import { shoppingCartService } from "../services/shopping-cart-service";
import Button from "react-bootstrap/Button";
import { toast } from "react-toastify";
import { Table } from "react-bootstrap";
import Header from "./Header";
import { AMOUNT } from "../constants";
import Select from "react-select";

const ShoppingCart = () => {
  const [cartItemsPublishers, setCartItemsPublishers] = useState({});
  const [publishers, setPublishers] = useState([]);

  const getCartItems = () => {
    let booksFromCart = shoppingCartService.getBooksFromCart();
    console.log(booksFromCart);
    //podelim po publisherima
    let tempCartItemsPublishers = {};
    let tempPublishers = [];
    for (let i = 0; i < booksFromCart.length; i++) {
      if (
        tempCartItemsPublishers[booksFromCart[i].publisherId] === undefined ||
        tempCartItemsPublishers[booksFromCart[i].publisherId] === null
      ) {
        tempCartItemsPublishers[booksFromCart[i].publisherId] = {
          cartItems: [],
          total: 0.0,
        };
        tempPublishers.push(booksFromCart[i].publisherId);
      }
      tempCartItemsPublishers[booksFromCart[i].publisherId].cartItems.push(
        booksFromCart[i]
      );
      tempCartItemsPublishers[booksFromCart[i].publisherId].total +=
        booksFromCart[i].amount * booksFromCart[i].price;
    }
    console.log(tempCartItemsPublishers);
    console.log(tempPublishers);
    setPublishers(tempPublishers);
    setCartItemsPublishers(tempCartItemsPublishers);
  };

  const removeFromCart = (id) => {
    shoppingCartService.removeBookfromCart(id);
    getCartItems();
  };

  const updateAmountForCartItem = (id, amount) => {
    shoppingCartService.updateAmountForCartItem(id, amount);
    getCartItems();
  };

  const createOrder = async (merchantId) => {
    let cartItemsPublisher = cartItemsPublishers[merchantId];
    let cartItems = cartItemsPublisher.cartItems;
    let total = cartItemsPublisher.total;
    let books = [];
    for (let i = 0; i < cartItems.length; i++) {
      books.push({ bookId: cartItems[i].id, amount: cartItems[i].amount });
    }

    let payload = {
      books: books,
      total: total,
      merchantId: merchantId,
    };
    try {
      const response = await orderService.createOrder(payload);
      for (let i = 0; i < cartItems.length; i++) {
        shoppingCartService.removeBookfromCart(cartItems[i].id);
      }
      getCartItems();
      toast.success("Order has been successfully created.", {
        hideProgressBar: true,
      });
      setTimeout(() => {
        window.open(response.redirectionURL);
      }, 2000);
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
    getCartItems();
  }, []);

  return (
    <div>
      <Header />
      <h2>Shopping cart</h2>
      <div style={{ width: "60%" }} className="ml-auto mr-auto">
        {publishers.map((publisher) => {
          return (
            <div style={{ marginTop: "2em", backgroundColor: "#bdbbbb" }}>
              <h4 style={{ marginTop: "1em", marginBottom: "1em" }}>
                Publisher:{" "}
                {cartItemsPublishers[publisher].cartItems[0].publisher}
              </h4>
              <Table bordered>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Title</th>
                    <th>Genre</th>
                    <th>Writer</th>
                    <th>Amount</th>
                    <th>Price</th>
                    <th>Remove from cart</th>
                  </tr>
                </thead>
                <tbody>
                  {cartItemsPublishers[publisher].cartItems.map(
                    (cartItem, i) => {
                      return (
                        <tr>
                          <td>{i + 1}</td>
                          <td>{cartItem.title}</td>
                          <td>{cartItem.genre}</td>
                          <td>{cartItem.writer}</td>
                          <td>
                            <Select
                              value={{
                                value: cartItem.amount,
                                label: cartItem.amount,
                              }}
                              onChange={(selectedValue) => {
                                console.log(selectedValue);
                                updateAmountForCartItem(
                                  cartItem.id,
                                  selectedValue.value
                                );
                              }}
                              options={AMOUNT}
                            />
                          </td>
                          <td>{cartItem.price}&#36;</td>
                          <td>
                            <Button
                              style={{ borderRadius: "2em" }}
                              variant="danger"
                              onClick={() => {
                                removeFromCart(cartItem.id);
                              }}
                            >
                              Remove from cart
                            </Button>
                          </td>
                        </tr>
                      );
                    }
                  )}
                </tbody>
              </Table>
              <h4 style={{ textAlign: "right", paddingRight: "1em" }}>
                Total: {cartItemsPublishers[publisher].total}&#36;
              </h4>
              <Button
                style={{ borderRadius: "2em", marginBottom: "1em" }}
                variant="success"
                onClick={() => {
                  createOrder(publisher);
                }}
              >
                Create order
              </Button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ShoppingCart;
