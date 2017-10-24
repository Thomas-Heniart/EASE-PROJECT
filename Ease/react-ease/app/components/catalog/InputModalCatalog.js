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

                <datalist id='languages' style={{ height: '40px' }}>
                    <option value='English' />
                    <option value='Chinese' />
                    <option value='Dutch' />
                    <option value='Eglish' />
                    <option value='Enlish' />
                    <option value='Engish' />
                    <option value='Englsh' />
                    <option value='Englih' />
                    <option value='Englis' />
                    <option value='Elish' />
                    <option value='Enish' />
                    <option value='Englh' />
                    <option value='Engli' />
                    <option value='Engls' />
                    <option value='Engih' />
                    <option value='Egish' />
                    <option value='Eish' />
                    <option value='Eng' />
                    <option value='En' />
                </datalist>
            </Form.Field>
        )
    }
}

export default InputModalCatalog;