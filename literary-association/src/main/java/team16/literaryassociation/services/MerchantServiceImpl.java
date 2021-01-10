package team16.literaryassociation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.repository.MerchantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Value("${application_id}")
    private String appId;
    @Value("${success_url}")
    private String successUrl;
    @Value("${failed_url}")
    private String failedUrl;
    @Value("${error_url}")
    private String errorUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Merchant findOne(Long id) {
        return merchantRepository.getOne(id);
    }

    @Override
    public Merchant findOneByMerchantId(String merchantId) {
        return merchantRepository.findMerchantByMerchantId(merchantId);
    }

    @Override
    public Merchant findByEmail(String email) {
        return this.merchantRepository.findByMerchantEmail(email);
    }

    @Override
    public MerchantDTO registerNewMerchant(MerchantDTO merchantDTO) {

        Merchant newMerchant = new Merchant();
        newMerchant.setMerchantName(merchantDTO.getName());
        newMerchant.setMerchantEmail(merchantDTO.getEmail());
        newMerchant.setActivated(false);
        newMerchant.setMerchantSuccessUrl(this.successUrl);
        newMerchant.setMerchantFailedUrl(this.failedUrl);
        newMerchant.setMerchantErrorUrl(this.errorUrl);

        MerchantPCDTO pcDTO = new MerchantPCDTO();
        pcDTO.setMerchantName(merchantDTO.getName());
        pcDTO.setMerchantEmail(merchantDTO.getEmail());
        pcDTO.setActivationUrl("https://localhost:8080/api/merchant/activate");
        pcDTO.setAppId(this.appId);
        pcDTO.setSuccessUrl(this.successUrl);
        pcDTO.setFailedUrl(this.failedUrl);
        pcDTO.setErrorUrl(this.errorUrl);
        ResponseEntity<MerchantPCDTO> response
                = restTemplate.postForEntity("https://localhost:8083/psp-service/api/merchant",
                pcDTO, MerchantPCDTO.class);

        this.merchantRepository.save(newMerchant);
        return new MerchantDTO(newMerchant);
    }

    @Override
    public void finishRegistration(MerchantActivationDTO mbi) {

        Merchant m = this.merchantRepository.findByMerchantEmail(mbi.getMerchantEmail());
        m.setActivated(true);
//        if(mbi.isBankPaymentMethod()) {
//            m.setMerchantId(mbi.getMerchantId());
//            m.setMerchantPassword(mbi.getMerchantPassword());
//        }
        this.merchantRepository.save(m);
    }

    @Override
    public List<MerchantDTO> findAllActiveMerchants() {
        List<Merchant> merchants = this.merchantRepository.findAllActiveMerchants();
        List<MerchantDTO> ms = merchants.stream().map(m -> new MerchantDTO(m.getId(), m.getMerchantName(), m.getMerchantEmail())).collect(Collectors.toList());
        return ms;
    }


}
