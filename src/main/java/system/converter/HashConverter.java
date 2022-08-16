package system.converter;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashConverter {


    public static String sha384(final String value) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        final byte[] rawPassword = value.getBytes("UTF8");
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
        messageDigest.update(rawPassword);
        return new BigInteger(
                1,
                messageDigest.digest()).toString(16);
    }


}