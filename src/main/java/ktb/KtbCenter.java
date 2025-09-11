package ktb;

import ktb.member.KtbMember;
import ktb.member.human.Instructor;
import ktb.member.human.Staff;
import ktb.member.human.Trainee;
import ktb.properties.Course;
import ktb.properties.Gender;
import ktb.properties.StaffRole;
import ktb.properties.TeachingStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * KTB 교육장을 의미하는 클래스
 * -> 여러 개 생성되면 안되기 때문에 Singleton Pattern을 적용함
 */
public class KtbCenter {

    private final Map<String, KtbMember> members;
    private final Map<String, KtbMember> entered;
    private final Random random;

    // For Singleton Pattern
    private KtbCenter() {
        this.members = new HashMap<>();
        this.entered = new HashMap<>();
        random = new Random();
        loadKtbMembers();
    }

    public static KtbCenter getInstance() { return new KtbCenter(); }

    public void checkIn(String nickname) {
        if (members.containsKey(nickname)) {
            if (!entered.containsKey(nickname)) {
                this.entered.put(nickname, members.get(nickname));
            } else {
                System.out.printf("%s는 이미 입실했습니다.\n", nickname);
            }
        } else {
            throw new IllegalArgumentException(String.format("%s는 KTB의 구성원이 아닙니다.", nickname));
        }
    }

    public void checkOut(String nickname) {
        if (members.containsKey(nickname)) {
            if (entered.containsKey(nickname)) {
                this.entered.remove(nickname);
            } else {
                System.out.printf("%s는 입실 상태가 아닙니다.\n", nickname);
            }
        } else {
            throw new IllegalArgumentException(String.format("%s는 KTB의 구성원이 아닙니다.", nickname));
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
                    throw new IllegalStateException("해당 강사의 강의를 수강할 교육생이 없어 강의를 열 수 없습니다.");
                }
                int knowledge = random.nextInt(20) + 1;
                System.out.printf("--> 강사 %s 의 지식 %d 강의가 오픈되었습니다.\n", instructor, knowledge);
                instructor.deliverLecture(trainees, knowledge);
                System.out.printf("--> %d 명의 수강생의 지식이 %d만큼 올랐습니다.\n", trainees.size(), knowledge);
            } else {
                throw new IllegalArgumentException(String.format("%s는 강사가 아니므로 강의를 진행할 수 없습니다.", nickname));
            }
        } else {
            throw new IllegalArgumentException(String.format("%s는 KTB의 구성원이 아닙니다.", nickname));
        }
    }

    public String getMemberStatus() {
        StringBuilder sb = new StringBuilder();
        List<Instructor> instructors = new ArrayList<>();
        List<Staff> staffs = new ArrayList<>();
        List<Trainee> trainees = new ArrayList<>();
        for (KtbMember member : members.values()) {
            if (member instanceof Instructor) {
                instructors.add((Instructor) member);
            } else if (member instanceof Staff) {
                staffs.add((Staff) member);
            } else if (member instanceof Trainee) {
                trainees.add((Trainee) member);
            } else {
                throw new IllegalStateException("존재할 수 없는 유형의 KTB Member 입니다");
            }
        }
        sb.append("[ KTB 구성원 현황 ]\n\n");
        sb.append(String.format("강 사(%d명)\n", instructors.size()));
        instructors.forEach(i -> sb.append(i.toString()).append("\n"));
        sb.append(String.format("\n운영진(%d명)\n", staffs.size()));
        staffs.forEach(s -> sb.append(s.toString()).append("\n"));
        sb.append(String.format("\n교육생(%d명)\n", trainees.size()));
        trainees.forEach(t -> sb.append(t.toString()).append("\n"));
        return sb.toString();
    }

    public String getEnteredStatus() {
        StringBuilder sb = new StringBuilder();
        List<Instructor> instructors = new ArrayList<>();
        List<Staff> staffs = new ArrayList<>();
        List<Trainee> trainees = new ArrayList<>();
        for (KtbMember member : entered.values()) {
            if (member instanceof Instructor) {
                instructors.add((Instructor) member);
            } else if (member instanceof Staff) {
                staffs.add((Staff) member);
            } else if (member instanceof Trainee) {
                trainees.add((Trainee) member);
            } else {
                throw new IllegalStateException("존재할 수 없는 유형의 KTB Member 입니다");
            }
        }
        sb.append("[ KTB 교육장 입실 현황 ]\n\n");
        sb.append(String.format("강 사(%d명)\n", instructors.size()));
        instructors.forEach(i -> sb.append(i.toString()).append("\n"));
        sb.append(String.format("\n운영진(%d명)\n", staffs.size()));
        staffs.forEach(s -> sb.append(s.toString()).append("\n"));
        sb.append(String.format("\n교육생(%d명)\n", trainees.size()));
        trainees.forEach(t -> sb.append(t.toString()).append("\n"));
        return sb.toString();
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
            System.out.println("KTB 회원정보 로딩 실패!!!");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}
