import java.util.concurrent.locks.ReentrantLock;

public class Mutex_Sample {

    /**
     * Mutex를 사용하여 Main과 2개의 Thread함수에서
     * 다음과 같이 1~30까지 숫자를 연속으로 출력
     */
    public static void main(String[] args) throws InterruptedException {

        ThreadClass2 th1 = new ThreadClass2("[Thread1] ");
        ThreadClass2 th2 = new ThreadClass2("[Thread2] ");

        th1.start();
        th2.start();

        //ThreadClass2.lock.lock();
        ThreadClass2.printNums("[main] ");
        //ThreadClass2.lock.unlock();

        th1.join();
        th2.join();

    }
}

class ThreadClass2 extends Thread {

    static ReentrantLock lock = new ReentrantLock();
    String thread_name;

    public ThreadClass2(String thread_name) {
        this.thread_name = thread_name;
    }

    @Override
    public void run() {
        //lock.lock();
        printNums(thread_name);
        //lock.unlock();
    }

    static void printNums(String thread_name) {

        System.out.println(thread_name);

        for (int i = 0; i <= 30; i++) {
            System.out.print(i + " ");
        }

        System.out.println();
    }
}
