package ktb.member.bot;

import ktb.member.KtbMember;
import ktb.member.human.Trainee;
import ktb.message.Message;
import ktb.properties.AiModel;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Bot extends KtbMember {

    private final AiModel aiModel;
    private final AtomicInteger talkCount;
    private final Random random;

    public Bot(String nickName, AiModel aiModel) {
        super(nickName);
        this.aiModel = aiModel;
        this.talkCount = new AtomicInteger(0);
        this.random = new Random();
    }

    public void talk(Trainee trainee) {
        try {
            System.out.printf(Message.START_TALK_WITH_STATUS_TEMPLATE.getMessage(), trainee.getNickname(), trainee.getIssue());
            int talkTime = random.nextInt(1000) + 1000;
            int resolved = random.nextInt(10) + 1;
            talkCount.incrementAndGet();
            Thread.sleep(talkTime);
            trainee.resolveIssue(resolved);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf(Message.FINISH_TALK_WITH_STATUS_TEMPLATE.getMessage(), trainee.getNickname(), trainee.getIssue());
    }

    public AiModel getAiModel() { return this.aiModel; }
    public int getTalkCount() { return talkCount.intValue(); }

}
