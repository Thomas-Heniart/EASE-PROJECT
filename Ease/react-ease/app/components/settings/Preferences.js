import React from 'react';
import { Segment, Checkbox, Form, Header } from 'semantic-ui-react';

class Preferences extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            homepage: false,
            background: false,
            loading: false
        }
    }

    toggleHomepage = () => {
        if (this.state.homepage === true)
            this.setState({ homepage: false });
        else
            this.setState({ homepage: true });
    };
    toggleBackground = () => {
        if (this.state.background === true)
            this.setState({ background: false });
        else
            this.setState({ background: true });
    };

    render() {
        return (
            <Segment>
                <Header as='h5'>Choose your preferences</Header>
                <div>
                    <Checkbox toggle onChange={this.toggleHomepage} disabled={this.state.loading} />
                    <span>Ease.space as Homepage</span>
                </div>
                <p>This option allows you to have the Ease.space page when you’ll open a new tab in your browser.</p>
                <div>
                    <Checkbox toggle onChange={this.toggleBackground} disabled={this.state.loading} />
                    <span>Daily background picture</span>
                </div>
                <p>Each day we’ll display a nice background picture, coming from <a href='https://unsplash.com' target='blank'>unsplash.com</a>, behind your Apps.</p>
            </Segment>
        )
    }
}

export default Preferences;