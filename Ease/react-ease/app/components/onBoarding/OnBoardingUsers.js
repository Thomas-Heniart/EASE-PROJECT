import React from 'react';
import { Input, Icon, Header, Label, TextArea } from 'semantic-ui-react';
import {handleSemanticInput, isEmail} from "../../utils/utils";

function checkTextArea(paste) {
  const comma = paste.search(/[,]/g);
  const semicolon = paste.search(/[;]/g);
  const space = paste.search(/[' ']/g);
  const tab = paste.search(/[\t]/g);

  if (comma !== -1 && isEmail(paste.substring(0, comma))) {
    const emails = paste.split(',').filter(item => {
      return isEmail(item);
    });
    return emails;
  }
  else if (semicolon !== -1 && isEmail(paste.substring(0, semicolon))) {
    const emails = paste.split(';').filter(item => {
      return isEmail(item);
    });
    return emails;
  }
  else if (space !== -1 && isEmail(paste.substring(0, space))) {
    const emails = paste.split(' ').filter(item => {
      return isEmail(item);
    });
    return emails;
  }
  else if (tab !== -1 && isEmail(paste.substring(0, tab))) {
    const emails = paste.split('\t').filter(item => {
      return isEmail(item);
    });
    return emails;
  }
  else
    return [];
}

class OnBoardingUsers extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      number: this.props.number,
      paste: '',
      emails: []
    }
  }
  handleInput = (e, {name, value}) => {
    let emails = [];
    if (!isEmail(value))
      emails = checkTextArea(value);
    else
      emails = [value];
    this.props.textareaToEmails(emails);
    this.setState({[name]: value, emails: emails});
  };
  render() {
    const {emails, onChange, addNewField} = this.props;
    checkTextArea(this.state.paste);
    const fields = emails.map((item, idx) => {
      return (
        <Input
          fluid
          key={idx}
          name='email'
          type='email'
          placeholder='Email'
          autoComplete='off'
          onChange={(e, values) => {onChange(idx, values)}}
          label={<Label><Icon style={{color:'white'}} name='user'/></Label>}/>
      )
    });
    return (
      <React.Fragment>
        <Header as='h1'>Who is working in your company?</Header>
        <p><strong>This step will not send invitations to your team</strong>.<br/>
          Please enter at least {this.state.number} emails or more (manually <u>or</u> paste a list from any file).</p>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          <div style={{width:'380px',paddingRight:'23px',paddingBottom:'10px',paddingTop:'10px',overflowY:'auto',height:'340px'}}>
            {fields}
          </div>
          <div style={{width:'370px',paddingLeft:'23px',paddingBottom:'10px',paddingTop:'10px',borderLeft:'solid 1px black'}}>
            <TextArea name='paste' onChange={this.handleInput} placeholder='Paste a list of emails from any file….' style={{height:'320px'}}/>
          </div>
        </div>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          <div style={{width:'380px',paddingRight:'23px',paddingBottom:'10px',paddingTop:'10px'}}>
            <Icon name="add circle" color="blue" size='large'/>
            <button className="button-unstyle inline-text-button primary"
                    type="button" onClick={addNewField}>
              Add another field
            </button>
          </div>
          {this.state.emails.length > 0 &&
          <div style={{width:'370px',paddingLeft:'23px',paddingBottom:'10px',paddingTop:'10px'}}>
            <p>{this.state.emails.length} email addresses detected.</p>
          </div>}
        </div>

      </React.Fragment>
    )
  }
}

export default OnBoardingUsers;