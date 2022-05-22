import java.io.*;

public class FileIO {

    /**
     * INPUT 폴더 하위에 위치한 파일들의 파일명(상대경로 포함), 크기를 Console화면에 출력
     * INPUT 폴더 하위에 위치한 파일들 중 크기가 3kbyte가 넘는 파일들은 모두 OUTPUT 폴더로 복사
     * (OUTPUT 폴더 및 서브 폴더 생성)
     * 단, 파일 복사 시 바이너리 파일을 버퍼에 읽고 쓰는 방식으로 구현하고
     * 버퍼의 크기는 512Byte로 설정
     */

    static String rootPath = "./INPUT";

    public static void main(String[] args) throws IOException {

        fileSearchAll(rootPath);
    }

    private static void fileSearchAll(String path) {

        File directory = new File(path);
        File[] fList = directory.listFiles();

        for(File file : fList) {
            if(file.isDirectory() ) {
                fileSearchAll(file.getPath());
            }
            else {
                String partPath = path.substring(rootPath.length());
                System.out.println("." + partPath + "/" + file.getName());
                if (file.length() > 3*1024) {
                    copyFile(partPath, file.getName());
                }
            }
        }
    }

    private static void copyFile(String partPath, String filename) {

        final int BUFFER_SIZE = 512;
        int readLen;

        try {
            // Create Folder
            File destFolder = new File("./OUTPUT" + partPath);
            if(!destFolder.exists()) {
                destFolder.mkdirs();
            }

            // Copy File
            InputStream inputStream = new FileInputStream("./INPUT"+partPath + "/" + filename);
            OutputStream outputStream = new FileOutputStream("./OUTPUT"+partPath + "/" + filename);

            byte[] buffer = new byte[BUFFER_SIZE];

            while ((readLen = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLen);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
