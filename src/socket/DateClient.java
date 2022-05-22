package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Client에서 Server에 접속하여 Server는 현재 날짜와 시각을 Client로 전송하고
 * Client는 전송 받은 값을 출력
 */
public class DateClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 9090);
        BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
        String answer = in.readLine();
        System.out.println(answer);
    }
}
