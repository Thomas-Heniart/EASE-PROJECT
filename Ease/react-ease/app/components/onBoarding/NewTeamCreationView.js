import React from 'react';
import {connect} from "react-redux";
import OnBoardingRooms from "./OnBoardingRooms";
import OnBoardingUsers from "./OnBoardingUsers";
import OnBoardingGroups from "./OnBoardingGroups";
import OnBoardingAccounts from "./OnBoardingAccounts";
import OnBoardingInformations from "./OnBoardingInformation";
import {handleSemanticInput, isEmail} from "../../utils/utils";
import {withRouter, Switch, Route, NavLink} from "react-router-dom";
import { Menu, Form, Icon, Button } from 'semantic-ui-react';
import {
  askRegistration, checkAskRegistration, editFirstNameAndLastName, fetchOnBoardingRooms, getInfoClearbit,
  newRegistration
} from "../../actions/onBoardingActions";
import * as post_api from '../../utils/post_api'

@connect()
class NewTeamCreationView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      error: '',
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
      checkCGU: false,
      rooms: [],
      roomsSelected: [],
      emails: [],
      users: [
        {value: 1, username: 'pute', text: 'pute' + ' - ' + 'victor' + ' ' + 'pprpopor'},
        {value: 2, username: 'fillou', text: 'fillou' + ' - ' + 'sfdbni' + ' ' + 'cfndkj'},
        {value: 3, username: 'rototo', text: 'rototo' + ' - ' + 'dfbfjd' + ' ' + 'fdbjhf'}
      ],
      viewAccounts: 1,
      currentRoom: 1,
      passwordManagerSelected: 0
    };
  }
  componentWillMount() {
    // get step if not let view 1
    // get Rooms
    this.props.dispatch(fetchOnBoardingRooms()).then(response => {
      this.setState({rooms: response})
    });
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
  selectPasswordManager = (id) => {
    this.setState({passwordManagerSelected: id});
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
    return (this.state.password !== '' && this.state.password === this.state.verificationPassword && this.state.phone.length > 9 && /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(this.state.password));
  };
  sentences = () => {
    if (this.state.view === 1) {
      if (this.state.viewInfo === 1)
        return <p>Confirm your email. You will receive a verification code.</p>;
      else if (this.state.viewInfo === 2)
        return <p>We’ve sent a code to <strong>{this.state.email}</strong>, it will expire shortly.<br/><br/>
          Haven’t received our email?<br/>Try our spam folder or <br/><u style={{cursor:'pointer'}} onClick={this.resendEmail}>Resend email</u></p>;
      else if (this.state.viewInfo === 3)
        return <p>Enter a <strong>strong password</strong><br/>without names, dates or info<br/>related to
          you.<br/><br/>
          Your password must contain<br/>at least <strong>8 characters,<br/>an uppercase, a lowercase<br/>and a
            number</strong>.</p>;
      else if (this.state.viewInfo === 4)
        return <p>This information will be<br/>available only to your team.</p>;
    }
    else if (this.state.view === 2)
      return  <p>On Ease.space, passwords are grouped into Rooms. <strong>Rooms enable fast and organised password sharing</strong>.<br/><br/>
        Ex: the Marketing Room groups the marketing passwords and is accessible by the marketing team.</p>;
    else if (this.state.view === 3)
      return <p>This step <strong>will not send invitations to your team</strong>.<br/>
        Adding members allows to start the setup of your platform before you invite them. The best for you is to add everyone.</p>;
    else if (this.state.view === 4)
      return <p>Remember, a Room groups passwords together so people can access them easily. People can be in more than one room.</p>;
    else if (this.state.view === 5) {
      if (this.state.viewAccounts === 1)
        return <p>We have done our best to make your account importation seamless.<br/>Choose a tool to import or justmanage manually</p>;
      else if (this.state.viewAccounts === 2)
        return <p>There is a special Room for accounts everybody uses, it’s the #openspace. Also, if you have more than one account on a website, you’ll be able to add it later.</p>;
      else if (this.state.viewAccounts === 3)
        return <p>This is a short list of tools to get started, you’ll be able to add more accounts later.</p>;
      else if (this.state.viewAccounts === 4)
        return <p>On the company’s Twitter for example, people share the same login and password to access the account.</p>;
      else if (this.state.viewAccounts === 5)
        return <p>If someone else knows a password, ask him or her to fill it for everybody. Also, when you are not 1000% sure about a password, you can test it in one click. </p>;
    }
  };
  resendEmail = () => {
    this.props.dispatch(askRegistration({
      email: this.state.email,
      newsletter: this.state.checkEmail
    })).then(response => {
      if (response.success)
        this.setState({firstName: response.first_name, lastName: response.last_name, companyName: response.company_name});
    });
  };
  nextInformation = () => {
    if (this.state.viewInfo === 1) {
      this.props.dispatch(askRegistration({
        email: this.state.email,
        newsletter: this.state.checkEmail
      })).then(response => {
        if (response.success)
          this.setState({firstName: response.first_name, lastName: response.last_name, companyName: response.company_name});
        this.setState({viewInfo: 2, loading: false, error: ''});
      }).catch(err => {
        this.setState({error: err, loading: false});
      });
    }
    else if (this.state.viewInfo === 2) {
      this.props.dispatch(checkAskRegistration({
        email: this.state.email,
        digits: this.state.confirmationCode
      })).then(response => {
        if (response.valid_digits)
          this.setState({viewInfo: 3, error: ''});
        else
          this.setState({error:'Is not the good confirmation code'});
      });
    }
    else if (this.state.viewInfo === 3) {
      // get Info and create user
      const username = this.state.email.split('@')[0];
      this.props.dispatch(newRegistration({
        username: username,
        email: this.state.email,
        password: this.state.password,
        digits: this.state.confirmationCode,
        code: '',
        phone_number: this.state.phone,
        newsletter: this.state.checkEmail,
        first_name: this.state.firstName,
        last_name: this.state.lastName
      })).then(response => {
        this.props.dispatch(getInfoClearbit({email: this.state.email})).then(response => {
          if (response.success)
            this.setState({firstName: response.first_name, lastName: response.last_name, companyName: response.company_name});
          this.setState({viewInfo: 4});
        });
        easeTracker.trackEvent("EaseOnboardingRegistration");
      });
    }
    else if (this.state.viewInfo === 4) {
      // request Info and create team
      const username = this.state.email.split('@')[0];
      post_api.teams.createTeam({
        name: this.state.companyName,
        email: this.state.email,
        username: username,
        digits: this.state.confirmationCode,
        plan_id: this.state.plan_id,
        company_size: this.state.companySize,
        ws_id: this.props.ws_id
      }).then(response => {
        easeTracker.trackEvent("EaseOnboardingInformationFilled", {
          "plan_id": this.props.plan_id
        });
      }).catch(err => {
        this.setState({errorMessage: err, loading: false});
      });
      this.props.dispatch(editFirstNameAndLastName({
        first_name: this.state.firstName,
        last_name: this.state.lastName
      })).then(response => {
        this.props.history.replace('/newTeamCreation/rooms');
        this.setState({activeItem: 2, view: 2});
      });
    }
  };
  nextAccounts = () => {
    if (this.state.viewAccounts === 1) {
      // Choose PM or mano
      if (this.state.passwordManagerSelected === -1)
        this.props.history.replace('/main/catalog/onBoardingImportation');
      //send to reducer PM etc...
      else
        this.setState({viewAccounts: 2});
    }
    else if (this.state.viewAccounts === 2) {
      // Choose apps for #openspace
      if (this.state.currentRoom === 1)
        this.setState({currentRoom: 2});
      else
        this.setState({viewAccounts: 3});
    }
    else if (this.state.viewAccounts === 3) {
      // Choose apps for every #rooms
      this.setState({viewAccounts: 2, currentRoom: this.state.currentRoom + 1});
    }
    else if (this.state.viewAccounts === 4) {
      // Choose if single or enterprise
      this.setState({viewAccounts: 5});
    }
    else {
      // send credentials for single
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
      this.props.history.replace('/newTeamCreation/users');
      this.setState({activeItem: 3, view: 3, emails: emails});
    }
    else if (this.state.view === 3) {
      this.props.history.replace('/newTeamCreation/groups');
      this.setState({activeItem: 4, view: 4});
    }
    else if (this.state.view === 4) {
      this.props.history.replace('/newTeamCreation/accounts');
      this.setState({activeItem: 5, view: 5});
    }
  };
  render() {
    if ((this.state.view === 1 && this.props.history.location.pathname !== '/newTeamCreation/informations'))
      this.props.history.replace('/newTeamCreation/informations');
    else if ((this.state.view === 2 && this.props.history.location.pathname !== '/newTeamCreation/rooms'))
      this.props.history.replace('/newTeamCreation/rooms');
    else if ((this.state.view === 3 && this.props.history.location.pathname !== '/newTeamCreation/users'))
      this.props.history.replace('/newTeamCreation/users');
    else if ((this.state.view === 4 && this.props.history.location.pathname !== '/newTeamCreation/groups'))
      this.props.history.replace('/newTeamCreation/groups');
    else if ((this.state.view === 5 && this.props.history.location.pathname !== '/newTeamCreation/accounts'))
      this.props.history.replace('/newTeamCreation/accounts');
    const firstP = this.sentences();
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
          <div id='content' className={this.state.view === 3 || (this.state.view === 1 && this.state.viewInfo === 4) || this.state.view === 5 ? 'stepUsers' : null}>
            <Form onSubmit={this.state.view === 1 ? this.nextInformation : this.state.view === 5 ? this.nextAccounts : this.next} error={this.state.error !== ''}>
              <Switch>
                {this.state.view === 1 &&
                <Route path={`${this.props.match.path}/informations`}
                       render={(props) =>
                         <OnBoardingInformations
                            error={this.state.error}
                            loading={this.state.loading}
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
                            lastName={this.state.lastName}
                            checkCGU={this.state.checkCGU}/>}/>}
                {this.state.view === 2 &&
                <Route path={`${this.props.match.path}/rooms`}
                       render={(props) =>
                         <OnBoardingRooms
                           rooms={this.state.rooms}
                           roomsSelected={this.state.roomsSelected}
                           selectRoom={this.selectRoom}/>}/>}
                {this.state.view === 3 &&
                <Route path={`${this.props.match.path}/users`}
                       render={(props) =>
                         <OnBoardingUsers
                           emails={this.state.emails}
                           onChange={this.editField}
                           addNewField={this.addNewField}/>}/>}
                {this.state.view === 4 &&
                <Route path={`${this.props.match.path}/groups`}
                       render={(props) =>
                          <OnBoardingGroups
                            users={this.state.users}
                            rooms={this.state.rooms}
                            roomsSelected={this.state.roomsSelected}/>}/>}
                {this.state.view === 5 &&
                <Route path={`${this.props.match.path}/accounts`}
                       render={(props) =>
                         <OnBoardingAccounts
                           passwordManagerSelected={this.state.passwordManagerSelected}
                           selectPasswordManager={this.selectPasswordManager}
                           currentRoom={this.state.currentRoom}
                           view={this.state.viewAccounts}
                           next={this.nextAccounts}
                           {...this.props}/>}/>}
              </Switch>
            </Form>
          </div>
          <div id='bottom_bar'>
            <Button positive
                    size='tiny'
                    type='submit'
                    onClick={this.state.view === 1 ? this.nextInformation : this.state.view === 5 ? this.nextAccounts : this.next}
                    disabled={(this.state.view === 1 && !isEmail(this.state.email))
                    || (this.state.viewInfo === 2 && this.state.confirmationCode.length < 6)
                    || (this.state.viewInfo === 3 && !this.checkPassword())
                    || (this.state.view === 2 && this.state.roomsSelected.length < 3)}>
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