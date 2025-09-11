package ktb.member.human;

import ktb.properties.Course;
import ktb.properties.Gender;

public class Trainee extends Human {
    private final Course course;
    private int knowledge;
    private int stress;
    private int issue;

    public Trainee(String nickName, String name, int age, Gender gender, Course course, int knowledge, int stress, int issue) {
        super(nickName, name, age, gender);
        this.course = course;
        this.knowledge = knowledge;
        this.stress = stress;
        this.issue = issue;
    }

    /**
     * 현재 자신의 상태를 정리하여 출력합니다.
     */
    public void report() {
        System.out.println("*****************");
        this.greeting();
        System.out.println("현재 저의 상태는 다음과 같습니다");
        System.out.printf("지식 : %d\n", knowledge);
        System.out.printf("스트레스 : %d\n", stress);
        System.out.printf("이슈 : %d\n", issue);
        System.out.println("*****************");
    }

    public boolean requestCounsel(Instructor instructor) {
        return true;
    }

    public boolean requestHelp(Staff staff) {
        return true;
    }

    public Course getCourse() { return this.course; }
    public int getKnowledge() { return this.knowledge; }
    public int getStress() { return this.stress; }
    public int getIssue() { return this.issue; }
    public void addKnowledge(int knowledge) { this.knowledge += knowledge; }
    public void releaseStress(int stress) { this.stress -= stress; }
    public void resolveIssue(int issue) { this.issue -= issue; }

    @Override
    public String toString() {
        return super.toString()
                + " - "
                + course.toString()
                + " - 지식: " + knowledge
                + " - 스트레스: " + stress
                + " - 이슈: " + issue;
    }

}
