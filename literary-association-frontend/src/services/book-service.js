import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class BookService extends HttpService {
  getAllBooks = async () => {
    const response = await this.client.get(ROUTES.BOOKS);
    console.log(response.data);
    return response.data;
  };

  getBookDetails = async (bookId) => {
    const response = await this.client.get(ROUTES.BOOKS + "/" + bookId);
    console.log(response.data);
    return response.data;
  };
}

export const bookService = new BookService();
