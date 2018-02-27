import React from 'react';
import {credentialIconType} from "../../../utils/utils";
import { Input, Icon, Form, Label } from 'semantic-ui-react';

const UrlInput = ({url}) => {
  return (
    <Form.Field>
      <label style={{fontSize: '16px', fontWeight: '300', color: '#424242',display:'inline-flex',width:'120px'}}>URL</label>
      <div className="display_flex align_items_center" style={{position:'relative'}}>
        <Input
          icon
          fluid
          disabled
          required
          name='url'
          type='text'
          value={url}
          size="large"
          placeholder='url'
          labelPosition="left"
          className="modalInput team-app-input"
          label={<Label><Icon name='linkify'/></Label>}/>
      </div>
    </Form.Field>
  )
};

const CredentialInputs = ({url, information, account_information, handleChange, toggle, seePassword, toggleSeePassword, edit}) => {
  return (
    <React.Fragment>
      {url !== -1 &&
      <UrlInput url={url}/>}
      {Object.keys(information).sort((a, b) => {
        return information[a].priority - information[b].priority;
      }).map(item => {
        return (
          <Form.Field key={item}>
            <label>{information[item].placeholder}</label>
            <div className="display_flex align_items_center">
              <Input
                icon
                fluid
                required
                name={item}
                size='large'
                labelPosition='left'
                disabled={!edit[item]}
                onChange={handleChange}
                value={account_information[item]}
                className="modalInput team-app-input"
                placeholder={information[item].placeholder}
                type={information[item].type === 'password' && seePassword === false ? 'password' : 'text'}>
                <Label><Icon name={credentialIconType[item]}/></Label>
                <input/>
                {(information[item].type === 'password' && seePassword === true) &&
                <Icon name='eye' link onClick={toggleSeePassword}/>}
                {(information[item].type === 'password' && seePassword === false) &&
                <Icon name='hide' link onClick={toggleSeePassword}/>}
              </Input>
              {(edit[item] !== undefined) &&
              <Icon
                fitted link
                name="pencil"
                onClick={toggle.bind(null, item)}
                style={{paddingLeft: '15px'}}/>}
            </div>
          </Form.Field>)
      })}
    </React.Fragment>)
};

export default CredentialInputs;