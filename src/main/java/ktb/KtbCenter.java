package ktb;

import ktb.member.KtbMember;

import java.util.ArrayList;
import java.util.List;

/**
 * KTB 교육장을 의미하는 클래스
 * -> 여러 개 생성되면 안되기 때문에 Singleton Pattern을 적용함
 */
public class KtbCenter {

    private final List<KtbMember> entered = new ArrayList<>();

    // For Singleton Pattern
    private KtbCenter() { }

    public static KtbCenter getInstance() { return new KtbCenter(); }

}
