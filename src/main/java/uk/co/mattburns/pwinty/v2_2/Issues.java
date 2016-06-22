package uk.co.mattburns.pwinty.v2_2;

import java.util.List;

/**
 * Inconsistent object that comes back from https://api.pwinty.com/v2.2/Orders/{orderId}/Issues
 * endpoint
 */
public class Issues {
    private List<Issue> issues;

    public List<Issue> getIssues() {
        return issues;
    }
}
