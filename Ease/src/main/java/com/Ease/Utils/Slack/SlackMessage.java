package com.Ease.Utils.Slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;
import com.Ease.Context.Variables;

public class SlackMessage {
    private static SlackMessage ourInstance = new SlackMessage();

    public static SlackMessage getInstance() {
        return ourInstance;
    }

    private SlackWebApiClient slackWebApiClient = SlackClientFactory.createWebApiClient(Variables.SLACK_API_KEY);

    private SlackMessage() {
    }

    public String postMessage(String channel, String text) {
        return this.slackWebApiClient.postMessage(channel, text);
    }
}
