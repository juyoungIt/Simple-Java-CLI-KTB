package ktb.member.human;

import ktb.member.KtbMember;
import ktb.properties.Gender;

import static ktb.message.Message.*;
import static ktb.properties.Gender.*;

public class Human extends KtbMember {

    private final String name;
    private final int age;
    private final Gender gender;

    public Human(String nickName, String name, int age, Gender gender) {
        super(nickName);
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String greeting() {
        return String.format(
                HUMAN_GREETING_TEMPLATE.getMessage(),
                super.getNickname(),
                super.getNickname(),
                this.name,
                this.age,
                gender.equals(MALE) ? "남자" : "여자"
        );
    }

    public String getName() { return this.name; }
    public int getAge() { return this.age; }
    public Gender getGender() { return this.gender; }

    @Override
    public String toString() {
        return super.getNickname() + "(" + name + ")";
    }

}
