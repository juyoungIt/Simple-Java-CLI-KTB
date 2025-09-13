package ktb;

import ktb.member.KtbMember;
import ktb.member.bot.Bot;
import ktb.member.human.Human;
import ktb.member.human.Instructor;
import ktb.member.human.Staff;
import ktb.member.human.Trainee;
import ktb.message.MemberStatus;
import ktb.properties.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ktb.message.Message.*;

public class KtbCenter {

    private static final KtbCenter ktbCenter = new KtbCenter();
    private final Map<String, KtbMember> members;
    private final Map<String, KtbMember> entered;
    private final Random random;

    // For Singleton Pattern
    private KtbCenter() {
        // KtbCenter 에 해당 동시접근은 없지만, 싱글톤이라는 점에서 내부 자료구조를 ConcurrentHashMap을 사용하여 동시성 문제 대비
        this.members = new ConcurrentHashMap<>();
        this.entered = new ConcurrentHashMap<>();
        random = new Random();
        loadKtbMembers();
    }

    public static KtbCenter getInstance() { return ktbCenter; }

    public void checkIn(String nickname) {
        if (members.containsKey(nickname)) {
            if (!entered.containsKey(nickname)) {
                this.entered.put(nickname, members.get(nickname));
                if (members.get(nickname) instanceof Human) {
                    System.out.println(((Human) members.get(nickname)).greeting());
                }
                System.out.println(members.get(nickname).explain());
            } else {
                System.out.printf(ALREADY_ENTERED_TEMPLATE.getMessage(), nickname);
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_KTB_MEMBER.getMessage(), nickname));
        }
    }

    public void checkOut(String nickname) {
        if (members.containsKey(nickname)) {
            if (entered.containsKey(nickname)) {
                this.entered.remove(nickname);
            } else {
                System.out.printf(NOT_ENTERED_STATUS_TEMPLATE.getMessage(), nickname);
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_EXIST_EVENT.getMessage(), nickname));
        }
    }

    public void openLecture(String nickname) {
        if (members.containsKey(nickname)) {
            if (members.get(nickname) instanceof Instructor instructor) {
                List<Trainee> trainees = entered.values().stream()
                        .filter(t -> t instanceof Trainee)
                        .filter(t -> ((Trainee) t).getCourse().equals(instructor.getCourse()))
                        .map(t -> (Trainee) t)
                        .toList();
                if (trainees.isEmpty()) {
                    throw new IllegalStateException(EXCEPTION_NO_TRAINEES_FOR_LECTURE.getMessage());
                }
                int knowledge = random.nextInt(20) + 1;
                System.out.printf(LECTURE_OPEN_TEMPLATE.getMessage(), instructor, knowledge);
                instructor.deliverLecture(trainees, knowledge);
                System.out.printf(LECTURE_RESULT_TEMPLATE.getMessage(), trainees.size(), knowledge);
            } else {
                throw new IllegalArgumentException(String.format(EXCEPTION_NOT_INSTRUCTOR_FOR_LECTURE.getMessage(), nickname));
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_KTB_MEMBER.getMessage(), nickname));
        }
    }

    public void openCounsel(String nickname) {
        if (members.containsKey(nickname)) {
            if (members.get(nickname) instanceof Instructor instructor) {
                if (!entered.containsKey(nickname)) {
                    throw new IllegalArgumentException(
                            String.format(EXCEPTION_INSTRUCTOR_NOT_HERE.getMessage(), nickname)
                    );
                }
                List<Trainee> trainees = entered.values().stream()
                        .filter(t -> t instanceof Trainee)
                        .map(t -> (Trainee) t)
                        .toList();
                ExecutorService pool = Executors.newFixedThreadPool(trainees.size());
                CountDownLatch startGate = new CountDownLatch(1);
                CountDownLatch endGate = new CountDownLatch(trainees.size());
                try {
                    trainees.forEach(t -> {
                        pool.submit(() -> {
                            try {
                                startGate.await();
                                t.requestCounsel(instructor);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } finally {
                                endGate.countDown();
                            }
                        });
                    });
                    startGate.countDown();
                    System.out.printf(START_COUNSEL_TEMPLATE.getMessage(), nickname);
                    endGate.await();
                    System.out.printf(FINISH_COUNSEL_TEMPLATE.getMessage(), nickname);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    pool.shutdown();
                }
            } else {
                throw new IllegalArgumentException(
                        String.format(EXCEPTION_NOT_INSTRUCTOR_FOR_COUNSEL.getMessage(), nickname)
                );
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_KTB_MEMBER.getMessage(), nickname));
        }
    }

    public void openHelpSession(String nickname) {
        if (members.containsKey(nickname)) {
            if (members.get(nickname) instanceof Staff staff) {
                if (!entered.containsKey(nickname)) {
                    throw new IllegalArgumentException(String.format(NOT_ENTERED_STATUS_TEMPLATE.getMessage(), nickname));
                }
                List<Trainee> trainees = entered.values().stream()
                        .filter(t -> t instanceof Trainee)
                        .map(t -> (Trainee) t)
                        .toList();
                ExecutorService pool = Executors.newFixedThreadPool(trainees.size());
                CountDownLatch startGate = new CountDownLatch(1);
                CountDownLatch endGate = new CountDownLatch(trainees.size());
                try {
                    trainees.forEach(t -> {
                        pool.submit(() -> {
                            try {
                                startGate.await();
                                t.requestHelp(staff);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } finally {
                                endGate.countDown();
                            }
                        });
                    });
                    startGate.countDown();
                    System.out.printf(START_HELP_SESSION_TEMPLATE.getMessage(), nickname);
                    endGate.await();
                    System.out.printf(FINISH_HELP_SESSION_TEMPLATE.getMessage(), nickname);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    pool.shutdown();
                }
            } else {
                throw new IllegalArgumentException(String.format(EXCEPTION_NOT_STAFF_FOR_HELP.getMessage(), nickname));
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_KTB_MEMBER.getMessage(), nickname));
        }
    }

    public void openTalkSession(String nickname) {
        if (members.containsKey(nickname)) {
            if (members.get(nickname) instanceof Bot bot) {
                if (!entered.containsKey(nickname)) {
                    throw new IllegalArgumentException(String.format(NOT_ACTIVATED_STATUS_TEMPLATE.getMessage(), nickname));
                }
                List<Trainee> trainees = entered.values().stream()
                        .filter(t -> t instanceof Trainee)
                        .map(t -> (Trainee) t)
                        .toList();
                ExecutorService pool = Executors.newFixedThreadPool(trainees.size());
                CountDownLatch startGate = new CountDownLatch(1);
                CountDownLatch endGate = new CountDownLatch(trainees.size());
                try {
                    trainees.forEach(t -> {
                        pool.submit(() -> {
                            try {
                                startGate.await();
                                t.requestTalk(bot);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } finally {
                                endGate.countDown();
                            }
                        });
                    });
                    startGate.countDown();
                    System.out.printf(START_TALK_SESSION_TEMPLATE.getMessage(), nickname);
                    endGate.await();
                    System.out.printf(FINISH_TALK_SESSION_TEMPLATE.getMessage(), nickname, bot.getTalkCount());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    pool.shutdown();
                }
            } else {
                throw new IllegalArgumentException(String.format(EXCEPTION_NOT_BOT_FOR_TALK.getMessage(), nickname));
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_KTB_MEMBER.getMessage(), nickname));
        }
    }

    public MemberStatus getInstructorStatus(boolean isEntered) {
        Map<String, KtbMember> members = isEntered ? entered : this.members;
        List<Instructor> instructors = members.values().stream()
                .filter(e -> e instanceof Instructor)
                .map(e -> (Instructor) e)
                .toList();
        StringBuilder instructorInfos = new StringBuilder();
        for (Instructor instructor : instructors) {
            instructorInfos.append(
                    String.format(INSTRUCTOR_INFO_TEMPLATE.getMessage(),
                            instructor.getNickname(),
                            instructor.getCourse().toString()
                    )
            ).append("\n");
        }
        return new MemberStatus(instructors.size(), instructorInfos.toString());
    }

    public MemberStatus getStaffStatus(boolean isEntered) {
        Map<String, KtbMember> members = isEntered ? entered : this.members;
        List<Staff> staffs = members.values().stream()
                .filter(e -> e instanceof Staff)
                .map(e -> (Staff) e)
                .toList();
        StringBuilder staffInfos = new StringBuilder();
        for (Staff staff : staffs) {
            staffInfos.append(
                    String.format(STAFF_INFO_TEMPLATE.getMessage(),
                            staff.getNickname(),
                            staff.getRole().toString()
                    )
            ).append("\n");
        }
        return new MemberStatus(staffs.size(), staffInfos.toString());
    }

    public MemberStatus getBotStatus(boolean isEntered) {
        Map<String, KtbMember> members = isEntered ? entered : this.members;
        List<Bot> bots = members.values().stream()
                .filter(e -> e instanceof Bot)
                .map(e -> (Bot) e)
                .toList();
        StringBuilder botInfos = new StringBuilder();
        for (Bot bot : bots) {
            botInfos.append(
                    String.format(BOT_INFO_TEMPLATE.getMessage(),
                            bot.getNickname(),
                            bot.getAiModel().toString()
                    )
            ).append("\n");
        }
        return new MemberStatus(bots.size(), botInfos.toString());
    }

    public MemberStatus getTraineeStatus(boolean isEntered) {
        Map<String, KtbMember> members = isEntered ? entered : this.members;
        List<Trainee> trainees = members.values().stream()
                .filter(e -> e instanceof Trainee)
                .map(e -> (Trainee) e)
                .toList();
        StringBuilder traineeInfos = new StringBuilder();
        for (Trainee trainee : trainees) {
            traineeInfos.append(
                    String.format(TRAINEE_INFO_TEMPLATE.getMessage(),
                            trainee.getNickname(),
                            trainee.getCourse().toString()
                    )
            ).append("\n");
        }
        return new MemberStatus(trainees.size(), traineeInfos.toString());
    }

    public String getMemberStatus() {
        MemberStatus instructorStatus = getInstructorStatus(false);
        MemberStatus staffStatus = getStaffStatus(false);
        MemberStatus botStatus = getBotStatus(false);
        MemberStatus traineeStatus = getTraineeStatus(false);
        return String.format(
                KTB_MEMBERS_HEADER.getMessage()
                        + KTB_INSTRUCTOR_TEMPLATE.getMessage()
                        + KTB_STAFF_TEMPLATE.getMessage()
                        + KTB_BOT_TEMPLATE.getMessage()
                        + KTB_TRAINEE_TEMPLATE.getMessage(),
                instructorStatus.getSize(),
                "닉네임", "참여트랙",
                instructorStatus.getStatus(),
                staffStatus.getSize(),
                "닉네임", "담당역할",
                staffStatus.getStatus(),
                botStatus.getSize(),
                "닉네임", "AI모델",
                botStatus.getStatus(),
                traineeStatus.getSize(),
                "닉네임", "참여트랙",
                traineeStatus.getStatus()
        );
    }

    public String getEnteredStatus() {
        MemberStatus instructorStatus = getInstructorStatus(true);
        MemberStatus staffStatus = getStaffStatus(true);
        MemberStatus botStatus = getBotStatus(true);
        MemberStatus traineeStatus = getTraineeStatus(true);
        return String.format(
                KTB_ENTERED_HEADER.getMessage()
                        + KTB_INSTRUCTOR_TEMPLATE.getMessage()
                        + KTB_STAFF_TEMPLATE.getMessage()
                        + KTB_BOT_TEMPLATE.getMessage()
                        + KTB_TRAINEE_TEMPLATE.getMessage(),
                instructorStatus.getSize(),
                "닉네임", "참여트랙",
                instructorStatus.getStatus(),
                staffStatus.getSize(),
                "닉네임", "담당역할",
                staffStatus.getStatus(),
                botStatus.getSize(),
                "닉네임", "AI모델",
                botStatus.getStatus(),
                traineeStatus.getSize(),
                "닉네임", "참여트랙",
                traineeStatus.getStatus()
        );
    }

    public void checkInAllMembers() {
        members.values().stream()
                .filter(m -> !entered.containsKey(m.getNickname()))
                .forEach(m -> entered.put(m.getNickname(), m));
    }

    private void loadKtbMembers() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/members.txt"))) {
            // 강사 정보 로딩
            int n = Integer.parseInt(br.readLine());
            String[] row;
            for (int i=0 ; i<n ; i++) {
                row = br.readLine().split(" ");
                String nickname = row[0];
                String name = row[1];
                int age = Integer.parseInt(row[2]);
                Gender gender = Gender.valueOf(row[3]);
                Course course = Course.valueOf(row[4]);
                TeachingStyle teachingStyle = TeachingStyle.valueOf(row[5]);
                members.put(nickname, new Instructor(nickname, name, age, gender, course, teachingStyle));
            }
            // 운영진 정보 로딩
            n = Integer.parseInt(br.readLine());
            for (int i=0 ; i<n ; i++) {
                row = br.readLine().split(" ");
                String nickname = row[0];
                String name = row[1];
                int age = Integer.parseInt(row[2]);
                Gender gender = Gender.valueOf(row[3]);
                StaffRole role = StaffRole.valueOf(row[4]);
                members.put(nickname, new Staff(nickname, name, age, gender, role));
            }
            // Bot 정보 로딩
            n = Integer.parseInt(br.readLine());
            for (int i=0 ; i<n ; i++) {
                row = br.readLine().split(" ");
                String nickname = row[0];
                AiModel aiModel = AiModel.valueOf(row[1]);
                members.put(nickname, new Bot(nickname, aiModel));
            }
            // 교육생 정보 로딩
            n = Integer.parseInt(br.readLine());
            for (int i=0 ; i<n ; i++) {
                row = br.readLine().split(" ");
                String nickname = row[0];
                String name = row[1];
                int age = Integer.parseInt(row[2]);
                Gender gender = Gender.valueOf(row[3]);
                Course course = Course.valueOf(row[4]);
                int knowledge = Integer.parseInt(row[5]);
                int stress = Integer.parseInt(row[6]);
                int issue = Integer.parseInt(row[7]);
                members.put(nickname, new Trainee(nickname, name, age, gender, course, knowledge, stress, issue));
            }
        } catch (IOException e) {
            System.out.println(EXCEPTION_FAILED_TO_LOAD_KTB_MEMBERS.getMessage());
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}
