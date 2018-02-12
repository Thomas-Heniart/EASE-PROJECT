import React from 'react';
import {credentialIconType} from "../../../utils/utils";
import { Input, Icon, Form, Label } from 'semantic-ui-react';

const CredentialInput = ({item, onChange, removeField, account_information}) => {
  return (
    <Form.Field>
      <label style={{fontSize: '16px', fontWeight: '300', color: '#424242',display:'inline-flex',width:'120px'}}>{item.placeholder}</label>
      <Icon size='large' name='circle' style={{position:'relative',top:'14',left:'235',zIndex:'1',color:'white',margin:'0'}} />
      <Icon onClick={e => removeField(item)} size='large' name='remove circle' style={{cursor:'pointer',position:'relative',top:'14',left:'206',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
      <Input size="large"
             id={item.priority}
             autoFocus={item.autoFocus}
             class="modalInput team-app-input"
             required
             autoComplete='on'
             name={item.name}
             onChange={onChange}
             label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
             labelPosition="left"
             placeholder={item.placeholder}
             value={account_information[item.name]}
             type={item.type}/>
    </Form.Field>
  )
};

const OtherInput = ({item, onChange, onChangePlaceholder, onFocus, removeField, account_information}) => {
  return (
    <Form.Field>
      <Input id={item.priority} onFocus={onFocus} transparent style={{fontSize:'16px',fontWeight:'300',color:'#424242',display:'inline-flex',width:'120px'}} value={item.placeholder} onChange={onChangePlaceholder} required/>
      <Icon size='large' name='circle' style={{position:'relative',top:'14',left:'235',zIndex:'1',color:'white',margin:'0'}} />
      <Icon onClick={e => removeField(item)} size='large' name='remove circle' style={{cursor:'pointer',position:'relative',top:'14',left:'206',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
      <Input size="large"
             id={item.priority}
             autoFocus={item.autoFocus}
             class="modalInput team-app-input"
             required
             autoComplete='on'
             name={item.name}
             onChange={onChange}
             label={<Label><Icon name={credentialIconType['other']}/></Label>}
             labelPosition="left"
             placeholder='New field'
             value={account_information[item.name]}
             type={item.type}/>
    </Form.Field>
  )
};

const NewAccountAnyCredentialInputs = ({information, account_information, handleChange, toggle}) => {
  return Object.keys(information).map(item => {
    if (information[item].name !== 'login' && information[item].name !== 'password')
      return <OtherInput key={information[item].priority} onChange={handleChange} onChangePlaceholder={this.handlePlaceholder} onFocus={this.handleFocus} removeField={this.removeField} item={information[item]} account_information={account_information}/>;
    else
      return <CredentialInput key={information[item].priority} onChange={this.handleChange} removeField={this.removeField} item={information[item]} account_information={account_information}/>;
  });
};

export default NewAccountAnyCredentialInputs;