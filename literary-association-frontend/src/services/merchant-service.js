import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class MerchantService extends HttpService {
  registerMerchant = async (payload) => {
    const response = await this.client.post(ROUTES.MERCHANT_REGISTER, payload);
    return response.data;
  };
}

export const merchantService = new MerchantService();
