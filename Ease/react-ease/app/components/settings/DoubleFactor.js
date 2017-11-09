import React from 'react';
import { Segment, Checkbox, Form, Header } from 'semantic-ui-react';

class DoubleFactor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {
        return (
            <Segment>
                <Header as='h5'>Verify your identity when logging in Ease.space on a new device</Header>
                <p>For safety reasons, we will always ask for a verification code when you are using a new device</p>
                <p>How would you like to receive your verification code ?</p>
                <div>
                    <Checkbox toggle />
                    <span>Email (username@mail.com)</span>
                </div>
                <div>
                    <Checkbox toggle />
                    <span>Ease.space mobile App</span>
                </div>
            </Segment>
        )
    }
}

export default DoubleFactor;