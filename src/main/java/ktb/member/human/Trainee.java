package ktb.member.human;

import ktb.member.bot.Bot;
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

    public void requestCounsel(Instructor instructor) {
        instructor.counsel(this);
    }

    public void requestHelp(Staff staff) {
        staff.help(this);
    }

    public void requestTalk(Bot bot) {
        bot.talk(this);
    }

    public Course getCourse() { return this.course; }
    public int getKnowledge() { return this.knowledge; }
    public int getStress() { return this.stress; }
    public int getIssue() { return this.issue; }
    public void addKnowledge(int knowledge) { this.knowledge += knowledge; }
    public void releaseStress(int stress) { this.stress = Math.max(0, this.stress - stress); }
    public void resolveIssue(int issue) { this.issue = Math.max(0, this.issue - issue); }

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
