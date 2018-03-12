package com.Ease.Utils.Slack;

import com.Ease.Context.Variables;
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.model.Channel;

import java.io.IOException;
import java.util.List;

public class SlackAPIWrapper {
    private static SlackAPIWrapper ourInstance = new SlackAPIWrapper();

    public static SlackAPIWrapper getInstance() {
        return ourInstance;
    }

    private Slack slack = Slack.getInstance();

    private SlackAPIWrapper() {
    }

    public List<Channel> getChannels() throws IOException, SlackApiException {
        ChannelsListResponse response =  slack.methods().channelsList(
                ChannelsListRequest.builder()
                        .token(Variables.SLACK_API_KEY)
                        .build()
        );
        return response.getChannels();
    }

    public void postMessage(String channel, String text) throws IOException, SlackApiException {
        slack.methods().chatPostMessage(
                ChatPostMessageRequest.builder()
                        .token(Variables.SLACK_API_KEY)
                        .channel(channel)
                        .text(text)
                        .mrkdwn(true)
                        .build()
        );
    }
}
