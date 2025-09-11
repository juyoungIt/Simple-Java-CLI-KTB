package ktb.member;

public class KtbMember {

    private final String nickName; // KTB 내부에서 사용하는 영어이름

    public KtbMember(String nickName) {
        this.nickName = nickName;
    }

    /**
     * KTB 구성원이라면 누구나 KTB에 대해서 설명할 수 있어야 합니다
     */
    public void explain() {
        System.out.println("KTB는 '성장가능성, 기본기, 몰입, 협업'을 강조하며 교육생들의 폭발적 성장을 지원합니다.");
    }

    public String getNickName() { return this.nickName; }

}
