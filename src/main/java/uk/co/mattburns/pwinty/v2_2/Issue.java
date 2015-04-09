package uk.co.mattburns.pwinty.v2_2;

import java.util.List;

public class Issue {
    private Long id;
    private IssueType issue;
    private String issueDetail;
    private IssueAction action;
    private String actionDetail;

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
}