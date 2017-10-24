import React from 'react';
import { Input, Button, Icon, Segment, Checkbox, Form, Grid } from 'semantic-ui-react';
import { render } from 'react-router-dom';

class RequestForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            loadingRequest: false,
            loadingSuggestion: false
        }
    }

    send = (string, e) => {
        e.preventDefault();
        string === 'request' ?
        this.setState({loading: true, loadingRequest: true})
        :
        this.setState({loading: true, loadingSuggestion: true});
    };

    render() {
        return (
            <Grid columns={2}>
                <Grid.Column>
                    <Segment clearing className="requestAnApp">
                        <Form onSubmit={e => this.send('suggestion', e)}>
                            <p>👋 Send suggestion</p>
                            <span>Paste website URL</span>
                            <Input required type='url' fluid placeholder='https://' size="mini" />
                            <Button positive
                                    size="mini"
                                    floated="right"
                                    loading={this.state.loadingSuggestion}
                                    disabled={this.state.loading} >
                                <Icon name="send" />
                                Send
                            </Button>
                        </Form>
                    </Segment>
                </Grid.Column>
                <Grid.Column>
                    <Segment clearing className="requestAnApp">
                        <Form onSubmit={e => this.send('request', e)}>
                            <p>🙏 Request an App</p>
                            <span>Paste website URL</span>
                            <Input required type='url' fluid placeholder='https://' size="mini" />
                            <span>Login</span>
                            <Input required fluid placeholder='Login' size="mini" />
                            <span>Password</span>
                            <Input required fluid placeholder='Password' type='password' size="mini" />
                            <span className="labelCheck"><Checkbox required />In order to add this website to my apps. I authorize Ease.space to use my credentials for a temporarily period of time of 72 hours maximum. More info</span>
                            <Button positive
                                    size="mini"
                                    floated="right"
                                    loading={this.state.loadingRequest}
                                    disabled={this.state.loading}>
                                <Icon name="send" />
                                Send
                            </Button>
                        </Form>
                    </Segment>
                </Grid.Column>
            </Grid>
        )
    }
}

export default RequestForm;