import { Button, Modal } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import { Table } from "react-bootstrap";
import Header from "./Header";
import { bookService } from "../services/book-service";
import { toast } from "react-toastify";

const SearchBooks = () => {
  const [books, setBooks] = useState([]);
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);

  const getBooks = async () => {
    try {
      const response = await bookService.getAllBooks();
      console.log("Books");
      console.log(response);
      setBooks(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const addToCart = async (book) => {
    let currentUserId = localStorage.getItem("currentUserId");
    if (currentUserId === null || currentUserId === undefined) {
      toast.error("You have to login first.", {
        hideProgressBar: true,
      });
    }
  };

  const handleCancel = () => {
    setShowDetails(false);
  };

  const seeBookDetails = async (bookId) => {
    try {
      const response = await bookService.getBookDetails(bookId);
      console.log(response);
      setBookToShow(response);
      setShowDetails(true);
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
    getBooks();
  }, []);

  return (
    <div>
      <Header />
      <h2>Books</h2>
      <div
        style={{ width: "60%", backgroundColor: "#bdbbbb" }}
        className="ml-auto mr-auto"
      >
        <Table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Genre</th>
              <th>Writer</th>
              <th>Publisher</th>
              <th>Price</th>
              <th>Details</th>
              <th>Add to cart</th>
            </tr>
          </thead>
          <tbody>
            {books.map((book) => {
              return (
                <tr key={book.id}>
                  <td>{book.title}</td>
                  <td>{book.genre}</td>
                  <td>{book.writer}</td>
                  <td>{book.publisher}</td>
                  <td>{book.price}&#36;</td>
                  <td>
                    <Button
                      style={{ borderRadius: "2em" }}
                      variant="primary"
                      onClick={() => {
                        seeBookDetails(book.id);
                      }}
                    >
                      Details
                    </Button>
                  </td>
                  <td>
                    <Button
                      style={{ borderRadius: "2em" }}
                      variant="success"
                      onClick={() => {
                        addToCart(book);
                      }}
                    >
                      Add to cart
                    </Button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </div>
      {showDetails && (
        <Modal
          show={showDetails}
          onHide={handleCancel}
          backdrop="static"
          keyboard={false}
          centered
        >
          <Modal.Header>
            <Modal.Title>{bookToShow.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body></Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCancel}>
              Cancel
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </div>
  );
};

export default SearchBooks;
