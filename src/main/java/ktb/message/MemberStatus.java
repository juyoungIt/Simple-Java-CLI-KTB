package ktb.message;

public class MemberStatus {

    private final int size;
    private final String status;

    public MemberStatus(int size, String status) {
        this.size = size;
        this.status = status;
    }

    public int getSize() { return this.size; }
    public String getStatus() { return this.status; }

}
