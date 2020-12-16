package team16.literaryassociation.services;

import team16.literaryassociation.model.Merchant;

public interface MerchantService {

    Merchant findOne(Long id);
    Merchant findOneByMerchantId(String merchantId);
    Merchant create(Merchant merchant);
//    Merchant createInitial();
}
