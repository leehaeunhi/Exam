import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncDec_Sample {

    /**
     * 1. 문자열을 입력 받아 Base64로 Encoding한 값을 출력하고
     *    그 값을 다시 Decoding하여 입력한 값과 동일한지 확인
     * 2. 1번에서 입력 받은 값을 SHA-256으로 Encryption해서 결과를 출력
     */

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);

        String str;
        while (true) {
            System.out.print("> ");

            str = br.readLine();
            Base64CaseString(str);
            Base64CaseByte(str);
            SHA256(str);
        }
    }

    private static void Base64CaseString(String str) throws UnsupportedEncodingException {

        System.out.println("Base64 인코딩 전 : " + str);

        Base64.Encoder encoder = Base64.getEncoder();
        String encStr = encoder.encodeToString(str.getBytes(StandardCharsets.UTF_8));
        System.out.println("Base64 인코딩 후 : " + encStr);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decBytes = decoder.decode(encStr);
        String decStr = new String(decBytes, "UTF-8");
        System.out.println("Base64 디코딩 후 : " + decStr);
    }

    private static void SHA256(String str) throws NoSuchAlgorithmException {

        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(str.getBytes());

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xFF) + 0x100, 16).substring(1));
        }

        System.out.println("SHA256 인코딩 후 : " + sb.toString());
    }

    public static void Base64CaseByte(String str) throws UnsupportedEncodingException
    {
        //String TestString = "This is a Base64 test.";
        byte[] TestBytes = str.getBytes("UTF-8");

        System.out.println("Base64Byte 인코딩 전 : " + new String(TestBytes));

        // Base64 ���ڵ� ///////////////////////////////////////////////////
        Base64.Encoder encoder = Base64.getEncoder();

        byte[] encodedBytes = encoder.encode(TestBytes);

        System.out.println("Base64Byte 인코딩 후 : " + new String(encodedBytes));

        // Base64 ���ڵ� ///////////////////////////////////////////////////
        Base64.Decoder decoder = Base64.getDecoder();

        byte[] decodedBytes = decoder.decode(encodedBytes);

        System.out.println("Base64Byte 디코딩 후 : " + new String(decodedBytes, "UTF-8"));
    }
}
