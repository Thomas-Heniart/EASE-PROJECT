import React from 'react';
import OnBoardingInformations from "./Information";
import {handleSemanticInput, isEmail} from "../../utils/utils";
import {withRouter, Switch, Route, NavLink} from "react-router-dom";
import { Menu, Header, Checkbox, Form, Input, Icon, Dropdown, Button, Message, Divider, Label, TextArea } from 'semantic-ui-react';

class ChooseRooms extends React.Component {
  render() {
    const {rooms, roomsSelected, selectRoom} = this.props;
    const roomsList = rooms.map(item => (
      <div
        key={item.id}
        onClick={e => selectRoom(item.id)}
        className={roomsSelected.filter(id => {return id === item.id}).length > 0 ? 'selected roomsSegment' : 'roomsSegment'}>
        #{item.name}
        <span>{item.description}</span>
      </div>
    ));
    return (
      <React.Fragment>
        <Header as='h1'>What passwords your company uses?</Header>
        <p>Select at least 3 types of passwords your company has. You’ll be able to add the tools you want in it, as well as create your own # later.</p>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          {roomsList}
        </div>
      </React.Fragment>
    )
  }
}

class PasteUsersEmail extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      number: this.props.emails.length
    }
  }
  render() {
    const {emails, onChange, addNewField} = this.props;
    const fields = emails.map((item, idx) => {
      return (
        <Input
          fluid
          key={idx}
          name='email'
          type='email'
          placeholder='Email'
          onChange={(e, values) => {onChange(idx, values)}}
          label={<Label><Icon style={{color:'white'}} name='user'/></Label>}/>
      )
    });
    return (
      <React.Fragment>
        <Header as='h1'>Who is working in your company?</Header>
        <p>This step will not send invitations to your team.<br/>
          Please enter {this.state.number} emails or more (manually <u>or</u> paste a list from any file).</p>
        <div style={{display:'inline-flex',flexWrap:'wrap'}}>
          <div>
            {fields}
            <Icon name="add circle" color="blue" size='large'/>
            <button className="button-unstyle inline-text-button primary"
                    type="button" onClick={addNewField}>
              Add another field
            </button>
          </div>
          <Divider vertical/>
          <div>
            <TextArea placeholder='Paste a list of emails from any file….' style={{height:'320px'}}/>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

class NewTeamCreationView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      activeItem: 1,
      view: 1,
      viewInfo: 1,
      email: '',
      checkEmail: false,
      confirmationCode: '',
      phone: '',
      password: '',
      verificationPassword: '',
      passwordError: true,
      companyName: '',
      companySize: '',
      firstName: '',
      lastName: '',
      rooms: [
        {id: 1, name: 'marketing', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 2, name: 'admin', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 3, name: 'bite', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 4, name: 'putain', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 5, name: 'ca', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 6, name: 'fonctionne', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 7, name: 'sa_mere', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 8, name: 'la', description: 'Ex: Fujihoma, Tasoeur, ...'},
        {id: 9, name: 'puteeeeeu', description: 'Ex: Fujihoma, Tasoeur, ...'},
      ],
      roomsSelected: [2, 7, 9],
      emails: []
    };
  }
  componentWillMount() {
    // get step if not let view 1
    // get Rooms
  }
  componentDidMount() {
    if (this.state.view === 1)
      this.props.history.replace('/newTeamCreation/informations');
    else if (this.state.view === 2) {
      // this.props.history.replace('/newTeamCreation/rooms');
      this.props.history.replace('/newTeamCreation');
    }
    else if (this.state.view === 2) {
      this.props.history.replace('/newTeamCreation');
    }
  }
  handleInput = handleSemanticInput.bind(this);
  handleConfirmationCode = (e, {name, value}) => {
    if (value.match(/^[0-9]{0,6}$/g))
      this.setState({[name]: value});
  };
  handlePasswordInput = (e, {name, value}) => {
    if (/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(value))
      this.setState({[name]: value, passwordError: false});
    else
      this.setState({[name]: value, passwordError: true});
  };
  selectRoom = (id) => {
    const roomsSelected = this.state.roomsSelected.filter(item => {
      if (item !== id)
        return item;
    });
    if (roomsSelected.length === this.state.roomsSelected.length)
      roomsSelected.push(id);
    this.setState({roomsSelected: roomsSelected});
  };
  editField = (idx, {name, value}) => {
    let emails = this.state.emails.slice();
    emails[idx][name] = value;
    this.setState(() => ({emails: emails}));
  };
  addNewField = () => {
    const emails = this.state.emails.slice();
    emails.push({email: '', error: ''});
    this.setState({emails: emails});
  };
  checkPassword = () => {
    return (this.state.password !== '' && this.state.password === this.state.verificationPassword && this.state.phone.length > 9)
  };
  nextInformation = () => {
    if (this.state.viewInfo === 1) {
      // request send email w/ clearbit
      this.setState({viewInfo: 2});
    }
    if (this.state.viewInfo === 2) {
      // request check if is the good confirmation code
      this.setState({viewInfo: 3});
    }
    if (this.state.viewInfo === 3) {
      // get Info
      this.setState({viewInfo: 4});
    }
    if (this.state.viewInfo === 4) {
      // request Info
      this.props.history.replace('/newTeamCreation');
      this.setState({activeItem: 2, view: 2});
    }
  };
  next = () => {
    if (this.state.view === 2) {
      let emails = [{email: '', error: ''}];
      // request create rooms + save step + generate input email
      if (this.state.companySize >= 6 && this.state.companySize <= 30)
        for (let i = 1; this.state.companySize / 2 - 1 > i; i++)
          emails.push({email: '', error: ''});
      else if (this.state.companySize > 30)
        for (let i = 1; 15 > i; i++)
          emails.push({email: '', error: ''});
      this.setState({activeItem: 3, view: 3, emails: emails});
    }
  };
  render() {
    const firstP = this.state.view === 1 ? <p>Confirm your email. You will receive a verification code.</p>
      : this.state.view === 2 ? <p>We’ve sent a code to <strong>{this.state.email}</strong>, it will expire shortly.<br/><br/>
          Haven’t received our email?<br/>Try our spam folder or <br/><u>Resend email</u></p>
        : this.state.view === 3 ? <p>Enter a <strong>strong password</strong><br/>without names, dates or info<br/>related to you.<br/><br/>
            Your password must contain<br/>at least <strong>8 characters,<br/>an uppercase, a lowercase<br/>and a number</strong>.</p>
          : this.state.view === 4 ? <p>This information will be<br/>available only to your team.</p>
            : <p>On Ease.space, passwords are grouped into Rooms. <strong>Rooms enable fast and organised password sharing</strong>.<br/><br/>
              Ex: the Marketing Room groups the marketing passwords and is accessible by the marketing team.</p>;
    return (
      <div id='new_team_creation'>
        <div id='left_bar'>
          {firstP}
          <img src='/resources/images/ease_logo_white.svg'/>
        </div>
        <div id='center'>
          <Menu id='top_bar' pointing secondary fluid>
            <Menu.Item name='Information' active={this.state.activeItem === 1}/>
            <Menu.Item name='Rooms' active={this.state.activeItem === 2}/>
            <Menu.Item name='Members' active={this.state.activeItem === 3}/>
            <Menu.Item name='Groups' active={this.state.activeItem === 4}/>
            <Menu.Item name='Accounts' active={this.state.activeItem === 5}/>
          </Menu>
          <div id='content'>
            <Form onSubmit={this.state.view > 1 ? this.next : this.nextInformation}>
              <Switch>
                <Route path={`${this.props.match.path}/informations`}
                       render={(props) =>
                         <OnBoardingInformations
                            view={this.state.viewInfo}
                            email={this.state.email}
                            checkEmail={this.state.checkEmail}
                            handleInput={this.handleInput}
                            handleConfirmationCode={this.handleConfirmationCode}
                            confirmationCode={this.state.confirmationCode}
                            phone={this.state.phone}
                            password={this.state.password}
                            verificationPassword={this.state.verificationPassword}
                            handlePasswordInput={this.handlePasswordInput}
                            passwordError={this.state.passwordError}
                            companyName={this.state.companyName}
                            companySize={this.state.companySize}
                            firstName={this.state.firstName}
                            lastName={this.state.lastName}/>}/>
              </Switch>
              {this.state.view === 2 &&
                <ChooseRooms
                  rooms={this.state.rooms}
                  roomsSelected={this.state.roomsSelected}
                  selectRoom={this.selectRoom}/>}
              {this.state.view === 3 &&
                <PasteUsersEmail
                  emails={this.state.emails}
                  onChange={this.editField}
                  addNewField={this.addNewField}/>}
            </Form>
          </div>
          <div id='bottom_bar'>
            <Button positive
                    size='tiny'
                    type='submit'
                    onClick={this.state.view > 1 ? this.next : this.nextInformation}
                    disabled={(this.state.view === 1 && !isEmail(this.state.email))
                    || (this.state.view === 2 && this.state.confirmationCode.length < 6)
                    || (this.state.view === 3 && !this.checkPassword())}>
              Next
              <Icon name='arrow right'/>
            </Button>
          </div>
        </div>
      </div>
    )
  }
}

export default NewTeamCreationView;