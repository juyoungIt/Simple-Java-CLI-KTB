import ktb.KtbCenter;
import ktb.utils.MsgProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class Main {

    private static final LocalDate today = LocalDate.now();
    private static final KtbCenter ktbCenter = KtbCenter.getInstance();
    private static final MsgProvider msgProvider = MsgProvider.getInstance();

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            printGreeting();
            while (true) {
                printOptions();
                int code = Integer.parseInt(br.readLine());
                switch (code) {
                    case 0:
                        closing();
                        break;
                    case 1:
                        System.out.println(ktbCenter.getMemberStatus());
                        printNicknameRequest();
                        String checkInName = br.readLine();
                        try {
                            ktbCenter.checkIn(checkInName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println(ktbCenter.getEnteredStatus());
                        printNicknameRequest();
                        String checkOutName = br.readLine();
                        try {
                            ktbCenter.checkOut(checkOutName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println(msgProvider.getSeparator());
                        printNicknameRequest();
                        String instructorName = br.readLine();
                        try {
                            ktbCenter.openLecture(instructorName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        System.out.println(msgProvider.getSeparator());
                        System.out.println(ktbCenter.getMemberStatus());
                        break;
                    case 7:
                        System.out.println(msgProvider.getSeparator());
                        System.out.println(ktbCenter.getEnteredStatus());
                        break;
                    default:
                        System.out.println("존재하지 않는 이벤트 입니다.");
                }
                if (code == 0) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printGreeting() {
        System.out.printf(msgProvider.getGreetingMsg(), today);
    }

    private static void printOptions() {
        System.out.print(msgProvider.getBaseOptionMsg());
    }

    private static void printNicknameRequest() {
        System.out.print(msgProvider.getNicknameRequestMsg());
    }

    private static void closing() {
        System.out.printf(msgProvider.getClosingMsg(), today);
    }
}
