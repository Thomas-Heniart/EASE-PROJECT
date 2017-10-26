import React from 'react';
import { Input, Form } from 'semantic-ui-react';

class InputModalCatalog extends React.Component {
    constructor(props){
        super(props);
    }

    render() {
        return (
            <Form.Field>
                <label style={{ fontSize: '16px', fontWeight: '300', color: '#424242' }}>{this.props.nameLabel}</label>
                <Input fluid
                       className="modalInput team-app-input"
                       size='large'
                       type={this.props.inputType}
                       name={this.props.nameInput}
                       placeholder={this.props.placeholderInput}
                       onChange={this.props.handleInput}
                       required
                       label={{ icon: this.props.iconLabel }}
                       labelPosition='left'
                       value={this.props.valueInput} />
            </Form.Field>
        )
    }
}

export default InputModalCatalog;