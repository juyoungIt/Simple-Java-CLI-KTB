package ktb.member.human;

import ktb.properties.Course;
import ktb.properties.Gender;
import ktb.properties.TeachingStyle;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static ktb.message.Message.*;

public class Instructor extends Human {
    private final Course course;
    private final TeachingStyle teachingStyle;
    private final ReentrantLock counselLock;
    private final Random random;

    public Instructor(String nickName, String name, int age, Gender gender, Course course, TeachingStyle teachingStyle) {
        super(nickName, name, age, gender);
        this.course = course;
        this.teachingStyle = teachingStyle;
        this.counselLock = new ReentrantLock(true);
        this.random = new Random();
    }

    public void deliverLecture(List<Trainee> trainees, int knowledge) {
        trainees.forEach(t -> t.addKnowledge(knowledge));
    }

    public void counsel(Trainee trainee) {
        boolean acquired = true;
        try {
            System.out.printf(CHECK_COUNSEL_AVAILABLE_TEMPLATE.getMessage(), trainee.getNickname(), super.getNickname());
            acquired = counselLock.tryLock(3, TimeUnit.SECONDS);
            if (!acquired) {
                System.out.printf(GIVE_UP_COUNSEL_TEMPLATE.getMessage(), trainee.getNickname());
                return;
            }
            System.out.printf(ACCEPT_COUNSEL_TEMPLATE.getMessage(), super.getNickname(), trainee.getNickname());
            System.out.printf(START_COUNSEL_WITH_STATUS_TEMPLATE.getMessage(), trainee.getNickname(), trainee.getStress());
            int counselTime = random.nextInt(1000) + 1000;
            int releaseStress = random.nextInt(10) + 1;
            Thread.sleep(counselTime);
            trainee.releaseStress(releaseStress);
            System.out.printf(FINISH_COUNSEL_WITH_STATUS_TEMPLATE.getMessage(), trainee.getNickname(), trainee.getStress());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquired) {
                counselLock.unlock();
            }
        }
    }

    public Course getCourse() { return this.course; }
    public TeachingStyle getTeachingStyle() { return this.teachingStyle; }

    @Override
    public String toString() {
        return super.toString() + " - " + course.toString();
    }

}
