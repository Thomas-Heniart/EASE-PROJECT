import React from 'react';
import {credentialIconType} from "../../../utils/utils";
import { Input, Icon, Form, Label } from 'semantic-ui-react';

const CredentialInputs = ({information, account_information, handleChange, toggle, seePassword, toggleSeePassword}) => {
  return (Object.keys(information).sort((a, b) => {
    return information[a].priority - information[b].priority;
  }).map(item => {
    return (
      <Form.Field key={item}>
        <label>{information[item].placeholder}</label>
        <div className="display_flex align_items_center">
          <Input
            fluid
            icon
            name={item}
            size='large'
            labelPosition='left'
            // required={this.state.isEmpty}
            placeholder={information[item].placeholder}
            className="modalInput team-app-input"
            onChange={handleChange}
            // disabled={!item.edit && !this.state.isEmpty}
            value={account_information[item]}
            type={information[item].type === 'password' && seePassword === false ? 'password' : 'text'}>
            <Label><Icon name={credentialIconType[item]}/></Label>
            <input />
            {(information[item].type === 'password' && seePassword === true) &&
              <Icon name='eye' link onClick={toggleSeePassword}/>}
            {(information[item].type === 'password' && seePassword === false) &&
              <Icon name='hide' link onClick={toggleSeePassword}/>}
          </Input>
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