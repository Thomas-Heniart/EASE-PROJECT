import React from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Form, Button, Message, Label, Input, Icon, Segment, Checkbox, Container } from 'semantic-ui-react';
import {teamCreateSingleApp} from "../../actions/appsActions";
import {testCredentials} from "../../actions/catalogActions";
import {showChooseAppCredentialsModal} from "../../actions/modalActions";
import {transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import {connect} from "react-redux";
import {reduxActionBinder} from "../../actions/index";

const CredentialInput = ({item, onChange}) => {
  return (
    <Form.Field>
        {/*<label style={{fontSize: '16px', fontWeight: '300', color: '#424242'}}>{item.placeholder}</label>*/}
        <Input size="large"
               autoFocus={item.autoFocus}
               class="modalInput team-app-input"
               required
               autoComplete='on'
               name={item.name}
               onChange={onChange}
               label={<Label><Icon name={credentialIconType[item.name]}/></Label>}
               labelPosition="left"
               placeholder={item.placeholder}
               value={item.value}
               type={item.type}/>
    </Form.Field>
  )
};

class MagicLink extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  render() {
    const {
      back,
      website,
      appName,
      loading,
      confirm
    } = this.props;
    return (
      <Container id="popup_team_single_card">
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <img src={website.logo} alt="Website logo"/>
          </div>
          <span className="app_name">{appName}</span>
        </div>
        <div>
          <p style={{cursor:'pointer'}} onClick={back} className="back_modal">
            <Icon name="arrow left"/>Back
          </p>
        </div>
        <div>
          <p>Send this link to ask the login and password</p>
        </div>
        <Form onSubmit={confirm}>
          <Button as='div' labelPosition='right' size='mini'>
            <Button icon>
              Copy link <Icon name='copy' />
            </Button>
            <Label as='a' basic>https://ease.space/12EBV4567gsu%^@</Label>
          </Button>
          <p style={{fontSize:'14px',color:'#949eb7'}}>The link will be valid until request is answered, or for 24 hours maximum.</p>
          <Button
            type="submit"
            loading={loading}
            disabled={loading}
            positive
            className="modal-button uppercase"
            content={`DONE`}/>
        </Form>
      </Container>
    )
  }
}

class ChooseHow extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  checkValueInput = () => {
    let i = 0;
    let j = 0;
    this.props.credentials.filter(item => {
      i++;
      if (item.value.length > 0) {
        j++;
        return item;
      }
    });
    return j === i;
  };
  render() {
    const {
      check,
      change,
      website,
      credentials,
      testCredentials,
      confirm,
      appName,
      loading,
      handleCredentialInput
    } = this.props;
    const credentialsInputs = credentials.map(item => {
      return <CredentialInput key={item.priority} onChange={handleCredentialInput} item={item}/>;
    });
    return (
      <Container>
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <img src={website.logo} alt="Website logo"/>
          </div>
          <span className="app_name">{appName}</span>
        </div>
        <Form  onSubmit={confirm} error={this.state.errorMessage.length > 0}>
          <Checkbox radio
                    style={{fontSize:'16px',marginBottom:'10px'}}
                    label={<label><strong>Enter</strong> login & password <strong>myself</strong></label>}
                    name='checkboxRadioGroup'
                    value={1}
                    checked={check === 1}
                    onChange={change} />
          {credentialsInputs}
          <Message error content={this.state.errorMessage}/>
          <span id='test_credentials' onClick={testCredentials}>Test connection <Icon color='green' name='magic'/></span>
          <Checkbox radio
                    style={{fontSize:'16px',marginBottom:'10px'}}
                    label={<label><strong>Ask</strong> login & password from <strong>a team member</strong></label>}
                    name='checkboxRadioGroup'
                    value={2}
                    checked={check === 2}
                    onChange={change} />
          <Checkbox radio
                    style={{fontSize:'16px'}}
                    label={<label><strong>Ask</strong> login & password from <strong>outside my team</strong></label>}
                    name='checkboxRadioGroup'
                    value={3}
                    checked={check === 3}
                    onChange={change} />
          <Button
            type="submit"
            loading={loading}
            disabled={loading || (check === 1 && !this.checkValueInput())}
            onClick={confirm}
            positive
            className="modal-button uppercase"
            content={'NEXT'}/>
        </Form>
      </Container>
    )
  }
}

class ChoosePersonWhoHasCredentials extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  role = (role) => {
    if (role === 3)
      return ('Owner');
    else if (role === 2)
      return ('Admin');
    else
      return ('Member');
  };
  render() {
    const {
      me,
      back,
      receivers,
      userSelected,
      website,
      appName,
      loading,
      change,
      confirm
    } = this.props;
    return (
      <Container id="popup_team_single_card">
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          <div className="squared_image_handler">
            <img src={website.logo} alt="Website logo"/>
          </div>
          <span className="app_name">{appName}</span>
        </div>
        <div>
          <p style={{cursor:'pointer'}} onClick={back} className="back_modal">
            <Icon name="arrow left"/>Back
          </p>
        </div>
        <div>
          <p>Ask login and password from a team member</p>
        </div>
        <Form onSubmit={confirm}>
          <Segment style={{borderColor:'#45c997'}} className='pushable'>
            {receivers.map(user => (
              user.id !== me.id &&
              <Checkbox radio
                        key={user.id}
                        label={`${user.username} (${this.role(user.role)})`}
                        name='checkboxRadioGroup'
                        value={user.id}
                        checked={userSelected === user.id}
                        onChange={change}/> )
            )}
          </Segment>
          <p style={{fontSize:'14px',color:'#949eb7'}}>The team member selected will be notified on his/her dashboard and you’ll be notified once it is done.</p>
        <Button
            type="submit"
            loading={loading}
            disabled={loading}
            positive
            className="modal-button uppercase"
            content={`ASK`}/>
        </Form>
      </Container>
    )
  }
}

@connect(store => ({
  card: store.teamCard,
  teams: store.teams,
  modal: store.modals,
  settingsCard: store.modals.chooseAppCredentials,
  receivers: store.modals.chooseAppCredentials.receivers
}), reduxActionBinder)
class ChooseAppCredentialsModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      credentials: transformWebsiteInfoIntoList(this.props.card.app.information),
      view: 1,
      userSelected: this.props.teams[this.props.card.team_id].my_team_user_id,
      me: null,
      users: null,
      check: 1
    }
  }
  componentWillMount() {
    const me_id = this.props.teams[this.props.card.team_id].my_team_user_id;
    const me = this.props.teams[this.props.card.team_id].team_users[me_id];
    const team_users = this.props.teams[this.props.card.team_id].team_users;
    const users = Object.keys(team_users)
      .sort((a, b) => (team_users[a].role - team_users[b].role))
      .map((item) => {
        return {
          ...team_users[item]
        };
      });
    this.setState({ me: me, users: users });
  }
  handleCredentialInput = (e, {name, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (name === item.name)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  handleChange = (e, { value }) => this.setState({ userSelected: value });
  handleChangeCheck = (e, {value}) => this.setState({check: value});
  testCredentials = () => {
    this.props.dispatch(testCredentials({
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      website_id: this.props.card.app.id
    }));
  };
  back = () => {
    this.setState({view: 1});
  };
  close = () => {
    this.props.dispatch(showChooseAppCredentialsModal({active: false}));
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    if (this.state.view === 1) {
      if (this.state.check === 2)
        this.setState({view: 2, loading: false});
      else if (this.state.check === 3)
        this.setState({view: 3, loading: false});
      else {
        const receivers = this.props.receivers.map(item => ({
          [item.id]: {allowed_to_see_password: item.can_see_information}
        }));
        const newReceivers = receivers.reduce(function (result, item) {
          result = Object.assign(result, item);
          return result;
        }, {});
        this.props.dispatch(teamCreateSingleApp({
          team_id: this.props.card.team_id,
          channel_id: this.props.card.channel_id,
          website_id: this.props.card.app.id,
          name: this.props.settingsCard.card_name,
          description: this.props.settingsCard.description,
          password_reminder_interval: this.props.settingsCard.password_reminder_interval,
          account_information: transformCredentialsListIntoObject(this.state.credentials),
          receivers: newReceivers
        })).then(response => {
          this.setState({loading: false});
          this.close();
          this.props.resetTeamCard();
        });
      }
    }
    else if (this.state.view === 2) {
      const receivers = this.props.receivers.map(item => ({
        [item.id]: {allowed_to_see_password: item.can_see_information}
      }));
      const newReceivers = receivers.reduce(function (result, item) {
        result = Object.assign(result, item);
        return result;
      }, {});
      this.props.dispatch(teamCreateSingleApp({
        team_id: this.props.card.team_id,
        channel_id: this.props.card.channel_id,
        website_id: this.props.card.app.id,
        name: this.props.settingsCard.card_name,
        description: this.props.settingsCard.description,
        password_reminder_interval: this.props.settingsCard.password_reminder_interval,
        team_user_filler_id: this.state.userSelected,
        account_information: {},
        receivers: newReceivers
      })).then(response => {
        this.setState({loading: false});
        this.close();
        this.props.resetTeamCard();
      });
    }
    else {
      console.log('[MAGICLINK]');
    }
  };
  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={'Login & password'}>
        {this.state.view === 1 &&
        <ChooseHow confirm={this.confirm}
                   check={this.state.check}
                   loading={this.state.loading}
                   website={this.props.card.app}
                   change={this.handleChangeCheck}
                   credentials={this.state.credentials}
                   testCredentials={this.testCredentials}
                   appName={this.props.settingsCard.card_name}
                   handleCredentialInput={this.handleCredentialInput} />}
        {this.state.view === 2 &&
        <ChoosePersonWhoHasCredentials back={this.back}
                                       me={this.state.me}
                                       confirm={this.confirm}
                                       users={this.state.users}
                                       change={this.handleChange}
                                       loading={this.state.loading}
                                       website={this.props.card.app}
                                       receivers={this.props.receivers}
                                       userSelected={this.state.userSelected}
                                       appName={this.props.settingsCard.card_name} />}
        {this.state.view === 3 &&
        <MagicLink back={this.back}
                   me={this.state.me}
                   confirm={this.confirm}
                   change={this.handleChange}
                   loading={this.state.loading}
                   website={this.props.card.app}
                   appName={this.props.settingsCard.card_name} />}
      </SimpleModalTemplate>
    )
  }
}

export default ChooseAppCredentialsModal;