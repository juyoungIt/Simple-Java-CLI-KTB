import ktb.KtbCenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

import static ktb.message.Message.*;

public class Main {

    private static final LocalDate today = LocalDate.now();
    private static final KtbCenter ktbCenter = KtbCenter.getInstance();

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            printBanner();
            printGreeting();
            while (true) {
                printOptions();
                String input = br.readLine();
                if (input == null) break;
                int code;
                try {
                    code = Integer.parseInt(input);
                } catch (Exception e) {
                    System.out.println(EXCEPTION_NOT_EXIST_EVENT.getMessage());
                    continue;
                }
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
                        System.out.println(ktbCenter.getInstructorStatus(true).getStatus());
                        printNicknameRequest();
                        String instructorName = br.readLine();
                        try {
                            ktbCenter.openLecture(instructorName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println(ktbCenter.getInstructorStatus(true).getStatus());
                        printNicknameRequest();
                        String counselorName = br.readLine();
                        try {
                            ktbCenter.openCounsel(counselorName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        System.out.println(ktbCenter.getStaffStatus(true).getStatus());
                        printNicknameRequest();
                        String staffName = br.readLine();
                        try {
                            ktbCenter.openHelpSession(staffName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 6:
                        System.out.println(ktbCenter.getBotStatus(true).getStatus());
                        printNicknameRequest();
                        String botName = br.readLine();
                        try {
                            ktbCenter.openTalkSession(botName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 7:
                        System.out.println(SEPARATOR.getMessage());
                        System.out.println(ktbCenter.getMemberStatus());
                        break;
                    case 8:
                        System.out.println(SEPARATOR.getMessage());
                        System.out.println(ktbCenter.getEnteredStatus());
                        break;
                    case 9:
                        ktbCenter.checkInAllMembers();
                        break;
                    default:
                        System.out.println(EXCEPTION_NOT_EXIST_EVENT.getMessage());
                }
                if (code == 0) break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printBanner() {
        System.out.println(SEPARATOR.getMessage());
        System.out.println(WELCOME_BANNER.getMessage());
    }

    private static void printGreeting() {
        System.out.println(SEPARATOR.getMessage());
        System.out.printf(GREETING_TEMPLATE.getMessage(), today);
    }

    private static void printOptions() {
        System.out.println(SEPARATOR.getMessage());
        System.out.print(EVENT_OPTIONS.getMessage());
    }

    private static void printNicknameRequest() {
        System.out.println(SEPARATOR.getMessage());
        System.out.print(NICKNAME_REQUEST.getMessage());
    }

    private static void closing() {
        System.out.println(SEPARATOR.getMessage());
        System.out.printf(CLOSING_TEMPLATE.getMessage(), today);
        System.out.println(SEPARATOR.getMessage());
    }

}
