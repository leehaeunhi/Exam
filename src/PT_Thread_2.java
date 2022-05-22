import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PT_Thread_2 {

    /**
     * NUM.TXT에 저장되어 있는 5쌍의 숫자들을 add_2sec.exe를 통해 덧셈을 실행시킨 후
     * 각각의 결과들을 모두 출력하시오
     * [조건]
     * - 전체 실행 시간은 5초 이내
     * - 결과의 출력 순서는 상관없음
     * - 실행 시작과 끝에 현재시각 출력
     */

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Start - " + new Date().toString());

        FileReader fr = new FileReader("NUM.TXT");
        BufferedReader in = new BufferedReader(fr);

        List<ProcessThread> thList = new ArrayList<ProcessThread>();

        String line;
        while ((line = in.readLine()) != null ) {

            String[] words = line.split(" ");
            int num1 = Integer.parseInt(words[0]);
            int num2 = Integer.parseInt(words[1]);

            ProcessThread th = new ProcessThread(num1, num2);
            th.start();
            thList.add(th);
        }

        in.close();
        for (ProcessThread th : thList) {
            th.join();
        }

        System.out.println("End - " + new Date().toString());
    }
}

class ProcessThread extends Thread {

    int num1;
    int num2;

    public ProcessThread(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    public void run() {
        String output;

        try {
            output = getProcessOutput(Arrays.asList("add_2sec.exe",
                                    Integer.toString(num1),
                                    Integer.toString(num2)));
            System.out.println(num1 + " + " + num2 + " = " + output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProcessOutput(List<String> cmdList) throws IOException {

        ProcessBuilder builder = new ProcessBuilder(cmdList);
        Process process = builder.start();
        InputStream psout = process.getInputStream();
        byte[] buffer = new byte[1024];
        int len = psout.read(buffer);
        return new String(buffer, 0, len);
    }


}
