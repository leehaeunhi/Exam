public class PT_Thread_1 {

    /**
     * Thread 2개를 만든 후, Main함수와 Thread 2개에서 동시에 0부터 9까지 출력
     * 어디서 출력하였는지 구분할 수 있게 숫자 앞에 [Main], [Thread1], [Thread2] 표시
     */

    public static void main(String[] args) throws InterruptedException {

        ThreadClass tc1 = new ThreadClass("[Thread1] ");
        ThreadClass tc2 = new ThreadClass("[Thread2] ");

        tc1.start();
        tc2.start();

        for (int i = 0; i < 10; i++) {
            System.out.println("[Main] " + i);
            Thread.sleep(10);
        }

        tc1.join();
        tc2.join();
    }
}

class ThreadClass extends Thread {

    String thread_name;

    public ThreadClass(String thread_name) {
        this.thread_name = thread_name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(thread_name + i);
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
