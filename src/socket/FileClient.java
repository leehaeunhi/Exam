package socket;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {

    /**
     * Client에서 Server에 접속하여 파일을 전송하는 프로그램
     * ClientFiles폴더의 모든 파일을 전송하여 ServerFiles폴더에 저장
     * - Client는 파일 전송 완료 후 종료
     * - Server는 파일을 수신 완료하고 다시 Client 접속 대기
     * - Server는 'QUIT' 입력을 받으면 종료
     */

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        DataOutputStream os = null;
        try {
            socket = new Socket("127.0.0.1", 27015);
            os = new DataOutputStream(socket.getOutputStream());

            byte[] buffer = new byte[4096];
            int length;

            // get all the files from a directory
            File directory = new File("./ClientFiles");
            File[] fList = directory.listFiles();

            for (File file : fList) {
                if (file.isFile()) {
                    os.writeUTF(file.getName());
                    os.writeInt((int) file.length());

                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(file.getPath());
                        while ((length = is.read(buffer)) != -1) {
                            os.write(buffer, 0, length);
                        }
                    } finally {
                        if (is != null) { is.close(); }
                    }
                }
            }
            System.out.println("Sent All Files.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) { os.close(); }
            if (socket != null) { socket.close(); }
        }
    }
}
