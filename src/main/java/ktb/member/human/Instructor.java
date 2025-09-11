package ktb.member.human;

import ktb.properties.Course;
import ktb.properties.Gender;
import ktb.properties.TeachingStyle;

import java.util.List;

public class Instructor extends Human {
    private final Course course;
    private final TeachingStyle teachingStyle;

    public Instructor(String nickName, String name, int age, Gender gender, Course course, TeachingStyle teachingStyle) {
        super(nickName, name, age, gender);
        this.course = course;
        this.teachingStyle = teachingStyle;
    }

    /**
     * 강의에 참여한 학생들에게 knowledge 만큼의 지식을 전달합니다
     * @param trainees  : 강의에 참여한 학생들
     * @param knowledge : 강의에서 전달할 지식의 양
     */
    public void deliverLecture(List<Trainee> trainees, int knowledge) {
        trainees.forEach(t -> t.addKnowledge(knowledge));
    }

    /**
     * 면담을 요청한 교육생에 대해서 면담을 수행합니다
     * @param trainee : 면담을 요청한 교육생
     */
    public void counsel(Trainee trainee) {
        // TODO : 멀티 스레드를 활용한 경합상황 만들어보기
    }

    public Course getCourse() { return this.course; }
    public TeachingStyle getTeachingStyle() { return this.teachingStyle; }

    @Override
    public String toString() {
        return super.toString() + " - " + course.toString();
    }

}
