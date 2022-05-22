import java.sql.SQLOutput;
import java.util.*;

public class DS_Queue {

    /**
     * 입력 메시지 (문자열)을 저장하는 Queue들을 작성하시오
     */

    static HashMap<String, MsgQueue> queues;

    public static void main(String[] args) {

        queues = new HashMap<String, MsgQueue>();
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.print(">");
            String line = sc.nextLine();
            String words[] = line.split(" ");
            String command = words[0];
            String qname = words[1];

            switch (command) {

                case "CREATE":
                    System.out.println(createQ(qname, Integer.parseInt(words[2])));
                    break;

                case "ENQUEUE":
                    System.out.println(enqueueQ(qname, words[2]));
                    break;

                case "DEQUEUE":
                    System.out.println(dequeueQ(qname));
                    break;

                case "GET":
                    System.out.println(getQ(qname));
                    break;

                case "SET":
                    System.out.println(setQ(qname, Integer.parseInt(words[2])));
                    break;

                case "DEL":
                    System.out.println(deleteQ(qname, Integer.parseInt(words[2])));
                    break;
            }
        }
    }

    private static String deleteQ(String qname, int msgId) {
        return queues.get(qname).deleteQ(msgId);
    }

    private static String setQ(String qname, int msgId) {
        return queues.get(qname).setQ(msgId);
    }

    private static String getQ(String qname) {
        return queues.get(qname).getQ();
    }

    private static String dequeueQ(String qname) {
        return queues.get(qname).dequeueMsg();
    }

    private static String enqueueQ(String qname, String msg) {
        return queues.get(qname).enqueueMsg(msg);
    }

    private static String createQ(String qname, int qsize) {

        if (queues.containsKey(qname)) {
            return "Queue Exist";
        }

        MsgQueue q = new MsgQueue(qsize);
        queues.put(qname, q);

        return "Queue Created";
    }
}

class MsgQueue {

    int size;
    int seqNo;

    private LinkedHashMap<Integer, List<String>> hashMsg;

    public MsgQueue(int size) {
        this.size = size;
        this.seqNo = 0;
        hashMsg = new LinkedHashMap<Integer, List<String>>();
    }

    public String enqueueMsg(String msg) {

        if (hashMsg.size() == size) {
            return "Queue Full";
        }

        List<String> listMsg = new ArrayList<String>();
        listMsg.add("A");   // Status - Available
        listMsg.add(msg);
        hashMsg.put(seqNo++, listMsg);

        return "Enqueued";
    }

    public String dequeueMsg() {

        if (hashMsg.size() == 0) {
            return "Queue Empty";
        }

        int key = (int)hashMsg.keySet().iterator().next();

        String res = hashMsg.get(key).get(1) + "(" + key + ")";
        hashMsg.remove(key);

        return res;
    }

    public String getQ() {

        if (hashMsg.size() > 0) {
            for (Integer key : hashMsg.keySet()) {
                if (hashMsg.get(key).get(0).equals("A")) {
                    List<String> val = hashMsg.get(key);
                    val.set(0, "U");
                    hashMsg.put(key, val);
                    return val.get(1) + "(" + key + ")";
                }
            }
        }

        return "No Msg";
    }

    public String setQ(int msgId) {

        if (hashMsg.size() > 0) {

            if (hashMsg.containsKey(msgId)) {
                hashMsg.get(msgId).set(0, "A");

                return "Msg Set";
            }
        }

        return "Set Fail";
    }

    public String deleteQ(int msgId) {

        if (hashMsg.size() > 0) {
            if (hashMsg.containsKey(msgId)) {
                hashMsg.remove(msgId);
                return "Deleted";
            }
        }
        return "Not Deleted";
    }
}