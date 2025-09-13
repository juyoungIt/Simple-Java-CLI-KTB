package ktb.member;

import static ktb.message.Message.*;

public class KtbMember {

    private final String nickname;

    public KtbMember(String nickname) {
        this.nickname = nickname;
    }

    public String explain() {
        return String.format(KTB_EXPLAIN_TEMPLATE.getMessage(), nickname);
    }

    public String getNickname() { return this.nickname; }

}
