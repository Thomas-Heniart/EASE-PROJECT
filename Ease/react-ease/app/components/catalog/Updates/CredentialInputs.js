import React from 'react';
import {credentialIconType} from "../../../utils/utils";
import { Input, Icon, Form } from 'semantic-ui-react';

const CredentialInputs = ({information, account_information, handleChange, toggle}) => {
  return (Object.keys(information).sort((a, b) => {
    return information[a].priority - information[b].priority;
  }).map(item => {
    return (
      <Form.Field key={item}>
        <label>{information[item].placeholder}</label>
        <div className="display_flex align_items_center">
          <Input
            icon
            fluid
            name={item}
            size='large'
            labelPosition='left'
            // required={this.state.isEmpty}
            placeholder={information[item].placeholder}
            className="modalInput team-app-input"
            onChange={handleChange}
            // disabled={!item.edit && !this.state.isEmpty}
            label={{icon: credentialIconType[item]}}
            value={account_information[item]}
            type={information[item].type}/>
          <Icon
            fitted link
            name="pencil"
            onClick={toggle.bind(null, item)}
            style={{paddingLeft: '15px'}}/>
        </div>
      </Form.Field>)
  }));
};

export default CredentialInputs;