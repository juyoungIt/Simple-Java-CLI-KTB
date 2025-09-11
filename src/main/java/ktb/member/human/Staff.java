package ktb.member.human;

import ktb.properties.Gender;
import ktb.properties.StaffRole;

public class Staff extends Human {
    private final StaffRole role;

    public Staff(String nickName, String name, int age, Gender gender, StaffRole role) {
        super(nickName, name, age, gender);
        this.role = role;
    }

    public void help(Trainee trainee) {
        // TODO : 멀티 스레딩을 활용한 경합상황 만들어보기
    }

    public StaffRole getRole() { return this.role; }

}
