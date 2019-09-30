package uk.co.mattburns.pwinty.v2_6;

public class Issue {
    private Long id;
    private IssueType issue;
    private String issueDetail;
    private IssueAction action;
    private String actionDetail;
    private IssueState issueState;
    private String commentary;

    public enum IssueType {
        DamagedOrder,
        WrongFrameColour,
        IncompleteOrder,
        LostInPost,
        IncorrectOrientation,
        IncorrectPrints,
        PrintDefects,
        SlowArrival,
        SlowDispatch,
        SubmissionErrors,
        WrongAddress,
        Unspecified;
    }

    public enum IssueAction {
        Refund,
        Reprint,
        NoAction,
        Other;
    }

    public enum IssueState {
        Open,
        InProgress,
        ClosedNotResolved,
        ClosedResolved,
        Cancelled;
    }

    /**
     * no-arg constructor included just for the sake of gson serialisation purposes.
     */
    public Issue() {}

    public Issue(IssueType issue, IssueAction action) {
        this(issue, action, null, null);
    }

    public Issue(IssueType issue, IssueAction action, String issueDetail, String actionDetail) {
        this.issue = issue;
        this.issueDetail = issueDetail;
        this.action = action;
        this.actionDetail = actionDetail;
    }

    public Long getId() {
        return id;
    }

    public IssueType getIssue() {
        return issue;
    }

    public String getIssueDetail() {
        return issueDetail;
    }

    public IssueAction getAction() {
        return action;
    }

    public String getActionDetail() {
        return actionDetail;
    }

    public String getCommentary() {
        return commentary;
    }

    public IssueState getIssueState() {
        return issueState;
    }
}
