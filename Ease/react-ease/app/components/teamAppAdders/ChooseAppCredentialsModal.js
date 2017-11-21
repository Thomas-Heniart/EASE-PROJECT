import React from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Form, Button, Message, Label, Input, Icon, Segment, Checkbox } from 'semantic-ui-react';
import {teamCreateSingleApp} from "../../actions/appsActions";
import {showPinTeamAppToDashboardModal} from "../../actions/teamModalActions";
import {showChooseAppCredentialsModal} from "../../actions/modalActions";
import {transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import {connect} from "react-redux";
import {reduxActionBinder} from "../../actions/index";

const CredentialInput = ({item, onChange}) => {
  return (
    <Form.Field>
        <label style={{fontSize: '16px', fontWeight: '300', color: '#424242'}}>{item.placeholder}</label>
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

class AddCardForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }

  render() {
    const {
      website,
      credentials,
      confirm,
      appName,
      handleCredentialInput
    } = this.props;
    const credentialsInputs = credentials.map(item => {
      return <CredentialInput key={item.priority} onChange={handleCredentialInput} item={item}/>;
    });
    return (
      <Form as="div" className="container" onSubmit={confirm} error={this.state.errorMessage.length > 0}>
          <Form.Field className="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div className="squared_image_handler">
                  <img src={website.logo} alt="Website logo"/>
              </div>
              <span className="app_name">{appName}</span>
          </Form.Field>
        {credentialsInputs}
          <Message error content={this.state.errorMessage}/>
          <Button
            type="submit"
            loading={this.state.loading}
            disabled={this.state.loading}
            onClick={confirm}
            positive
            className="modal-button uppercase"
            content={'DONE'}/>
      </Form>
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
      users,
      receivers,
      userSelected,
      website,
      appName,
      loading,
      change,
      confirm
    } = this.props;
    return (
      <Form id="popup_team_single_card" as="div" className="container" onSubmit={confirm}>
          <Form.Field className="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div className="squared_image_handler">
                  <img src={website.logo} alt="Website logo"/>
              </div>
              <span className="app_name">{appName}</span>
          </Form.Field>
          <Form.Field>
              <p>Who will enter app credentials?</p>
          </Form.Field>
          <Form.Field>
              <Segment className='pushable'>
                  <Checkbox radio
                            label={`${me.username} (${this.role(me.role)})`}
                            name='checkboxRadioGroup'
                            value={me.id}
                            checked={userSelected === me.id}
                            onChange={change}/>
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
          </Form.Field>
          <Button type="submit"
                  loading={loading}
                  disabled={loading}
                  onClick={confirm}
                  positive
                  className="modal-button uppercase"
                  content={'CONFIRM'}/>
      </Form>
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
      users: null
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

  close = () => {
    this.props.dispatch(showChooseAppCredentialsModal({active: false}));
  };

  confirm = () => {
    this.setState({loading: true});
    if (this.state.view === 1) {
      if (this.state.userSelected === this.state.me.id)
        this.setState({view: 2, loading: false});
      else
        ;
    }
    else {
      // const meReceiver = this.state.selected_users.indexOf(this.props.myId) !== -1;
      const receivers = this.props.receivers
          // .filter(item => (this.state.selected_users.indexOf(item.id) !== -1))
          .map(item => ({
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
        password_change_interval: this.props.settingsCard.password_change_interval,
        account_information: transformCredentialsListIntoObject(this.state.credentials),
        receivers: newReceivers
      })).then(response => {
        // if (meReceiver)
        //     this.props.dispatch(showPinTeamAppToDashboardModal(true, response));
        this.setState({loading: false});
        this.close();
        this.props.resetTeamCard();
      });
    }
  };

  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={'App Credentials'}>
        {this.state.view === 1 &&
        <ChoosePersonWhoHasCredentials website={this.props.card.app}
                                       appName={this.props.settingsCard.card_name}
                                       me={this.state.me}
                                       users={this.state.users}
                                       receivers={this.props.receivers}
                                       userSelected={this.state.userSelected}
                                       loading={this.state.loading}
                                       change={this.handleChange}
                                       confirm={this.confirm} />}
        {this.state.view === 2 &&
        <AddCardForm credentials={this.state.credentials}
                     handleCredentialInput={this.handleCredentialInput}
                     website={this.props.card.app}
                     appName={this.props.settingsCard.card_name}
                     confirm={this.confirm} />}
      </SimpleModalTemplate>
    )
  }
}

export default ChooseAppCredentialsModal;