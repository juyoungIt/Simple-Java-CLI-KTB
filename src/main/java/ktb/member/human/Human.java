package ktb.member.human;

import ktb.member.KtbMember;
import ktb.properties.Gender;

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

    /**
     * KTB의 구성원들은 인사를 잘합니다.
     */
    public void greeting() {
        System.out.printf("안녕하세요. %s(%s) 입니다.\n", super.getNickName(), name);
    }

    public String getName() { return this.name; }
    public int getAge() { return this.age; }
    public Gender getGender() { return this.gender; }

    @Override
    public String toString() {
        return super.getNickName() + "(" + name + ")";
    }

}
