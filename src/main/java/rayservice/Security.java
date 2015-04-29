package rayservice;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
    private static int REDIX = 20; //default = 16

    public static String getMD5(String input) {
        try {
            input += "wwang";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(REDIX);
            while (hashtext.length() < 30) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static String getHash(String str) {
        String[] splitted = str.split("(?<=\\G.....)");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= splitted.length; i++) {
            String ele = splitted[i - 1];
            if (i == 1) {
                sb.append(Integer.toHexString(Integer.toHexString("w".hashCode() * i + i).hashCode()));
            } else if (i == 2) {
                sb.append(Integer.toHexString(Integer.toHexString("a".hashCode() * i + i).hashCode()));
            } else if (i == 3) {
                sb.append(Integer.toHexString(Integer.toHexString("n".hashCode() * i + i).hashCode()));
            } else if (i == 4) {
                sb.append(Integer.toHexString(Integer.toHexString("g".hashCode() * i + i).hashCode()));
            }
            sb.append(Integer.toHexString(Integer.toHexString(ele.hashCode() * i + i).hashCode()));
        }
        return sb.toString();
    }

    static String getusername(String str) {
        String[] splitted = str.split("@");
        StringBuilder sb = new StringBuilder();
        for (String s : splitted) {
            sb.append(s.replace(".", "-"));
        }
        return sb.toString();
    }

    public static void main(String arg[]) {
        System.out.println(getMD5("zzzzzzzzzzzzasdasdzzzzzzzzzzzzz"));
    }
}
