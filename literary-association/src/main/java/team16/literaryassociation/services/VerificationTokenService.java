package team16.literaryassociation.services;

import team16.literaryassociation.model.User;
import team16.literaryassociation.model.VerificationToken;

public interface VerificationTokenService {

    VerificationToken findToken(String token);
    void createVerificationToken(User user, String token);
}
