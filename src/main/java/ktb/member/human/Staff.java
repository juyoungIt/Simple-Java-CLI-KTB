package ktb.member.human;

import ktb.properties.Gender;
import ktb.properties.StaffRole;

import java.util.Random;

import static ktb.message.Message.*;

public class Staff extends Human {
    private final StaffRole role;
    private final Object helpSessionLock;
    private final Random random;

    public Staff(String nickName, String name, int age, Gender gender, StaffRole role) {
        super(nickName, name, age, gender);
        this.role = role;
        this.helpSessionLock = new Object();
        this.random = new Random();
    }

    public void help(Trainee trainee) {
        System.out.printf(CHECK_HELP_AVAILABLE_TEMPLATE.getMessage(), trainee.getNickname(), super.getNickname());
        synchronized (helpSessionLock) {
            System.out.printf(ACCEPT_HELP_TEMPLATE.getMessage(), super.getNickname(), trainee.getNickname());
            try {
                System.out.printf(START_HELP_WITH_STATUS_TEMPLATE.getMessage(), trainee.getNickname(), trainee.getIssue());
                int helpTime = random.nextInt(1000) + 1000;
                int resolved = random.nextInt(10) + 1;
                Thread.sleep(helpTime);
                trainee.resolveIssue(resolved);
                System.out.printf(FINISH_HELP_WITH_STATUS_TEMPLATE.getMessage(), trainee.getNickname(), trainee.getIssue());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public StaffRole getRole() { return this.role; }

    @Override
    public String toString() {
        return super.toString() + " - " + role.toString();
    }

}
