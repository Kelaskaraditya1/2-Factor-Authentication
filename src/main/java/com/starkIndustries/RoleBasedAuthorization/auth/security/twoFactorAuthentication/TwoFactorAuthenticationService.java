package com.starkIndustries.RoleBasedAuthorization.auth.security.twoFactorAuthentication;

import com.starkIndustries.RoleBasedAuthorization.keys.Keys;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TwoFactorAuthenticationService {

    public String generateNewSecret(){
        return new DefaultSecretGenerator().generate();
    }

    public String generateQrCodeImageUri(String secret){

        QrData qrData = new QrData.Builder()
                .label(Keys.LABEL)
                .secret(secret)
                .issuer(Keys.ISSUER)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try{
            imageData=qrGenerator.generate(qrData);

        }catch (Exception e){
            e.printStackTrace();
            log.error("Error while generating Qr Code:{}",e.getLocalizedMessage());
        }

        return Utils.getDataUriForImage(imageData,qrGenerator.getImageMimeType());
    }

    public boolean isOtpValid(String secret, String code){
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator,timeProvider);
        return codeVerifier.isValidCode(secret,code);
    }

    public boolean isOtpNotValid(String secret,String code){
        return !this.isOtpValid(secret,code);
    }


}
