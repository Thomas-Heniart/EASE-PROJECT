import React from 'react';
import {credentialIconType} from "../../../utils/utils";
import { Input, Icon, Form, Label } from 'semantic-ui-react';

const CredentialInput = ({item, onChange, removeField, account_information, toggle, seePassword, toggleSeePassword, edit}) => {
  return (
    <Form.Field>
      <label style={{fontSize: '16px', fontWeight: '300', color: '#424242',display:'inline-flex',width:'120px'}}>{item.placeholder}</label>
      <div className="display_flex align_items_center" style={{position:'relative'}}>
        <Icon size='large' name='circle' style={{cursor:'pointer',position:'absolute',bottom:'34',left:'325',zIndex:'1',margin:'0',color:'white'}} />
        <Icon onClick={e => removeField(item.priority, item.name)} size='large' name='remove circle' style={{cursor:'pointer',position:'absolute',bottom:'34',left:'325',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
        <Input
          icon
          fluid
          required
          size="large"
          name={item.name}
          id={item.priority}
          onChange={onChange}
          labelPosition="left"
          disabled={!edit[item.name]}
          placeholder={item.placeholder}
          className="modalInput team-app-input"
          value={account_information[item.name]}
          type={item.type === 'password' && seePassword === false ? 'password' : 'text'}>
          <Label><Icon name={credentialIconType[item.name]}/></Label>
          <input />
          {(item.type === 'password' && seePassword === true) &&
          <Icon name='eye' link onClick={toggleSeePassword}/>}
          {(item.type === 'password' && seePassword === false) &&
          <Icon name='hide' link onClick={toggleSeePassword}/>}
        </Input>
        {(edit[item.name] !== undefined) &&
        <Icon
          fitted link
          name="pencil"
          onClick={toggle.bind(null, item.name)}
          style={{paddingLeft: '15px'}}/>}
      </div>
    </Form.Field>
  )
};

const OtherInput = ({item, onChange, onChangePlaceholder, onFocus, removeField, account_information, toggle, edit}) => {
  return (
    <Form.Field>
      <Input id={item.priority} onFocus={onFocus} transparent style={{fontSize:'16px',fontWeight:'300',color:'#424242',display:'inline-flex',width:'120px'}} value={item.placeholder} onChange={onChangePlaceholder} required/>
      <div className="display_flex align_items_center" style={{position:'relative'}}>
        <Icon size='large' name='circle' style={{cursor:'pointer',position:'absolute',bottom:'34',left:'325',zIndex:'1',margin:'0',color:'white'}} />
        <Icon onClick={e => removeField(item.priority, item.name)} size='large' name='remove circle' style={{cursor:'pointer',position:'absolute',bottom:'34',left:'325',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
        <Input
          icon
          fluid
          required
          size="large"
          name={item.name}
          type={item.type}
          onChange={onChange}
          labelPosition="left"
          placeholder='New field'
          disabled={!edit[item.name]}
          class="modalInput team-app-input"
          value={account_information[item.name]}
          label={<Label><Icon name={credentialIconType['other']}/></Label>}/>
        {(edit[item.name] !== undefined) &&
        <Icon
          fitted link
          name="pencil"
          onClick={toggle.bind(null, item.name)}
          style={{paddingLeft: '15px'}}/>}
      </div>
    </Form.Field>
  )
};

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

const NewAccountAnyCredentialInputs = ({url, information, account_information, handleChange, toggle, edit, seePassword, toggleSeePassword, removeField, handleFocus, handlePlaceholder}) => {
  return (
    <React.Fragment>
      <UrlInput url={url}/>
      {Object.keys(information).sort((a, b) => {
      return information[a].priority - information[b].priority;
        }).map(item => {
      if (information[item].name !== 'login' && information[item].name !== 'password')
        return <OtherInput
          edit={edit}
          toggle={toggle}
          onChange={handleChange}
          item={information[item]}
          onFocus={handleFocus}
          removeField={removeField}
          key={information[item].priority}
          account_information={account_information}
          onChangePlaceholder={handlePlaceholder}/>;
      else
        return <CredentialInput
          edit={edit}
          toggle={toggle}
          item={information[item]}
          seePassword={seePassword}
          onChange={handleChange}
          removeField={removeField}
          key={information[item].priority}
          toggleSeePassword={toggleSeePassword}
          account_information={account_information}/>;
      })}
    </React.Fragment>
  )
};

export default NewAccountAnyCredentialInputs;