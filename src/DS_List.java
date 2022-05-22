import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DS_List {

    /**
     * List_Sample.txt에는 학생들의 성적 데이터가 저장되어있음
     * 1. Console화면에서 'PRINT'를 입력하면 이름 순 (오름차순)으로 출력
     * 2. Console화면에서 'KOREAN', 'ENGLISH', 'MATH'를 입력하면 해당 과목 성적 순(오름차순)으로 출력
     * 성적이 동일한 경우에는 이름을 오름차순으로 정렬
     * 'QUIT'를 입력하면 프로그램 종료
     */
    public static void main(String[] args) throws IOException {

        // List_Sample.txt -> List 저장
        ArrayList<Grade> al = new ArrayList<Grade>();

        try {
            FileReader fr = new FileReader("List_Sample.txt");
            BufferedReader in = new BufferedReader(fr);

            String str;
            while ((str = in.readLine()) != null) {

                String words[] = str.split("\t");
                Grade g = new Grade(words[0],
                        Integer.parseInt(words[1]),
                        Integer.parseInt(words[2]),
                        Integer.parseInt(words[3]));

                al.add(g);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 콘솔 입력
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            System.out.print(">");
            String command = br.readLine();

            switch (command) {

                case "PRINT":
                    Collections.sort(al, (g1, g2) -> g1.getName().compareTo(g2.getName()));
                    break;

                case "KOREAN":
                    Collections.sort(al, (g1, g2)
                            -> (g2.getKorean() - g1.getKorean()) == 0?
                            g1.getName().compareTo(g2.getName()) :
                            g2.getKorean() - g1.getKorean());
                    break;

                case "ENGLISH":
                    Collections.sort(al, new Comparator<Grade>() {

                        @Override
                        public int compare(Grade o1, Grade o2) {
                            if (o2.getEnglish() - o1.getEnglish() == 0) {
                                return o1.getName().compareTo(o2.getName());
                            } else {
                                return o2.getEnglish() - o1.getEnglish();
                            }
                        }
                    });
                    break;

                case "MATH":
                    Collections.sort(al, new SortByMath());
                    break;

                case "QUIT":
                    return;

                default:
                    break;
            }

            // 리스트 출력
            for (Grade grade : al) {
                System.out.println(String.format("%s\t%d\t%d\t%d",
                        grade.getName(),
                        grade.getKorean(),
                        grade.getEnglish(),
                        grade.getMath()));
            }
        }
    }
}

class Grade {
    String name;
    int korean;
    int english;
    int math;

    public Grade(String name, int korean, int english, int math) {
        this.name = name;
        this.korean = korean;
        this.english = english;
        this.math = math;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKorean() {
        return korean;
    }

    public void setKorean(int korean) {
        this.korean = korean;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }
}

class SortByMath implements Comparator<Grade> {

    @Override
    public int compare(Grade o1, Grade o2) {

        if (o2.getMath() - o1.getMath() == 0) {
            return o1.getName().compareTo(o2.getName());
        } else {
            return o2.getEnglish() - o1.getEnglish();
        }
    }
}
