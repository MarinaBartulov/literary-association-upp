package team16.literaryassociation.services;

import team16.literaryassociation.dto.MerchantActivationDTO;
import team16.literaryassociation.dto.MerchantDTO;
import team16.literaryassociation.model.Merchant;

import java.util.List;

public interface MerchantService {

    Merchant findOne(Long id);
    Merchant findOneByMerchantId(String merchantId);
    Merchant findByEmail(String email);
    MerchantDTO registerNewMerchant(MerchantDTO merchantDTO);
    void finishRegistration(MerchantActivationDTO mbi);
    List<MerchantDTO> findAllActiveMerchants();
}
