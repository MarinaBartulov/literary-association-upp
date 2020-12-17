package team16.literaryassociation.services;

import org.springframework.beans.factory.annotation.Autowired;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.repository.MerchantRepository;

public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Merchant findOne(Long id) {
        return merchantRepository.getOne(id);
    }

    @Override
    public Merchant findOneByMerchantId(String merchantId) {
        return merchantRepository.findByMerchantId(merchantId);
    }

    @Override
    public Merchant create(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

//    @Override
//    public Merchant createInitial() {
//        Merchant merchant = new Merchant();
//        String generatedString = RandomStringUtils.random(30, true, true);
//        merchant.setMerchant_id(generatedString);
//        String password = "Merchant123!";
//        merchant.setPassword(password);
//        merchant.setMerchant_success_url("http://localhost:3000/success");
//        merchant.setMerchant_failed_url("http://localhost:3000/failed");
//        merchant.setMerchant_error_url("http://localhost:3000/error");
//        return merchantRepository.save(merchant);
//    }

    // ne radi sa Camunda
//    @EventListener(ApplicationReadyEvent.class)
//    public void createMerchantOnAppStart() {
//        Merchant merchant = createInitial();
//        System.out.println("On app start.");
//        System.out.println(merchant.getId());
//    }
}
