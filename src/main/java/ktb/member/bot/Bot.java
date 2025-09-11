package ktb.member.bot;

import ktb.member.KtbMember;
import ktb.properties.AiModel;

public class Bot extends KtbMember {
    private final AiModel aiModel;

    public Bot(String nickName, AiModel aiModel) {
        super(nickName);
        this.aiModel = aiModel;
    }

    public void answer(String msg) {
        System.out.printf("문의하신 \\'%s\\'요청이 처리되었습니다.", msg);
    }

    public AiModel getAiModel() { return this.aiModel; }

}
