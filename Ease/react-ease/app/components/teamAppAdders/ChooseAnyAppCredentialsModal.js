import React from 'react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Form, Button, Message, Label, Input, Icon, Segment, Checkbox, Container } from 'semantic-ui-react';
import {teamCreateAnySingleCard} from "../../actions/appsActions";
import {showChooseAnyAppCredentialsModal} from "../../actions/modalActions";
import {handleSemanticInput, transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import {connect} from "react-redux";
import {reduxActionBinder} from "../../actions/index";

const CredentialInput = ({item, onChange, removeField}) => {
  return (
    <Form.Field>
      <label style={{fontSize: '16px', fontWeight: '300', color: '#424242',display:'inline-flex',width:'120px'}}>{item.placeholder}</label>
      <Icon onClick={e => removeField(item)} size='large' name='remove circle' style={{position:'relative',top:'14',left:'234',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
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
             value={item.value}
             type={item.type}/>
    </Form.Field>
  )
};

const OtherInput = ({item, onChange, onChangePlaceholder, removeField}) => {
  return (
    <Form.Field>
      <Input id={item.priority} transparent style={{fontSize:'16px',fontWeight:'300',color:'#424242',display:'inline-flex',width:'120px'}} value={item.placeholder} onChange={onChangePlaceholder} required/>
      <Icon onClick={e => removeField(item)} size='large' name='remove circle' style={{position:'relative',top:'14',left:'234',zIndex:'1',color:'#e0e1e2',margin:'0'}} />
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
      logo,
      logoLetter,
      credentials,
      confirm,
      appName,
      loading,
      check,
      handleInput,
      handleCredentialInput,
      handlePlaceholder,
      addFields,
      removeField
    } = this.props;
    const credentialsInputs = credentials.map(item => {
      if (item.name !== 'login' && item.name !== 'password')
        return <OtherInput key={item.priority} onChange={handleCredentialInput} onChangePlaceholder={handlePlaceholder} removeField={removeField} item={item}/>;
      else
        return <CredentialInput key={item.priority} onChange={handleCredentialInput} removeField={removeField} item={item}/>;
    });
    return (
      <Container>
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          {logo ?
            <div className="squared_image_handler">
              <img src={logo} alt="Website logo"/>
            </div>
            :
            <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
              <div style={{margin:'auto'}}>
                <p style={{margin:'auto'}}>{logoLetter}</p>
              </div>
            </div>}
          <span className="app_name">{appName}</span>
        </div>
        <Form  onSubmit={confirm} error={this.state.errorMessage.length > 0}>
          {credentialsInputs}
          <p onClick={addFields} style={{color:'#414141'}}><Icon name='plus circle'/>Add a field</p>
          <div className='checkbox_credentials'>
            <Checkbox toggle onChange={handleInput} name='check' checked={check}/>
            To create auto-connection on this site, the Ease.space robot can use my password as test.
          </div>
          <Message error content={this.state.errorMessage}/>
          <Button
            type="submit"
            loading={loading}
            disabled={loading || !this.checkValueInput()}
            onClick={confirm}
            positive
            className="modal-button uppercase"
            content={'DONE'}/>
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
      receivers,
      userSelected,
      logo,
      logoLetter,
      appName,
      loading,
      change,
      confirm
    } = this.props;
    return (
      <Container id="popup_team_single_card">
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          {logo ?
          <div className="squared_image_handler">
            <img src={logo} alt="Website logo"/>
          </div>
          :
          <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
            <div style={{margin:'auto'}}>
              <p style={{margin:'auto'}}>{logoLetter}</p>
            </div>
          </div>}
          <span className="app_name">{appName}</span>
        </div>
        <div>
          <p>Who will enter app credentials?</p>
        </div>
        <Form onSubmit={confirm}>
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
          <Button
            type="submit"
            loading={loading}
            disabled={loading}
            positive
            className="modal-button uppercase"
            content={'CONFIRM'}/>
        </Form>
      </Container>
    )
  }
}

@connect(store => ({
  card: store.teamCard,
  teams: store.teams,
  modal: store.modals,
  settingsCard: store.modals.chooseAnyAppCredentials,
  receivers: store.modals.chooseAnyAppCredentials.receivers
}), reduxActionBinder)
class ChooseAppCredentialsModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      credentials: transformWebsiteInfoIntoList(this.props.card.app.information),
      priority: 2,
      view: 1,
      check: false,
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
  handleInput = handleSemanticInput.bind(this);
  handleCredentialInput = (e, {id, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (id === item.priority)
        item.value = value;
      return item;
    });
    this.setState({credentials: credentials});
  };
  handlePlaceholder = (e, {id, value}) => {
    let credentials = this.state.credentials.map(item => {
      if (id === item.priority) {
        item.placeholder = value;
        item.name = value.toLowerCase();
      }
      return item;
    });
    this.setState({credentials: credentials});
  };

  handleChange = (e, { value }) => this.setState({ userSelected: value });
  addFields = () => {
    let inputs = this.state.credentials.slice();
    const newInput = {name:"other",placeholder:"Click to rename",priority:this.state.priority,type:"text",value:""};
    inputs.push(newInput);
    this.setState({credentials: inputs, priority: this.state.priority + 1});
  };
  removeField = (field) => {
    const inputs = this.state.credentials.filter(item => {
      return item.priority !== field.priority;
    }).map((item, idx) => {
      item.priority = idx;
      return item
    });
    this.setState({credentials: inputs});
  };
  close = () => {
    this.props.dispatch(showChooseAnyAppCredentialsModal({active: false}));
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    if (this.state.view === 1) {
      if (this.state.userSelected === this.state.me.id)
        this.setState({view: 2, loading: false});
      else {
        const receivers = this.props.receivers
          .map(item => ({
            [item.id]: {allowed_to_see_password: item.can_see_information}
          }));
        const newReceivers = receivers.reduce(function (result, item) {
          result = Object.assign(result, item);
          return result;
        }, {});
        this.props.dispatch(teamCreateAnySingleCard({
          team_id: this.props.card.team_id,
          channel_id: this.props.card.channel_id,
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
    }
    else {
      const receivers = this.props.receivers
        .map(item => ({
          [item.id]: {allowed_to_see_password: item.can_see_information}
        }));
      const newReceivers = receivers.reduce(function (result, item) {
        result = Object.assign(result, item);
        return result;
      }, {});
      this.props.dispatch(teamCreateAnySingleCard({
        team_id: this.props.card.team_id,
        channel_id: this.props.card.channel_id,
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
  };
  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={'App Credentials'}>
        {this.state.view === 1 &&
        <ChoosePersonWhoHasCredentials logo={this.props.settingsCard.img_url}
                                       logoLetter={this.props.settingsCard.logoLetter}
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
                     loading={this.state.loading}
                     logo={this.props.settingsCard.img_url}
                     logoLetter={this.props.settingsCard.logoLetter}
                     appName={this.props.settingsCard.card_name}
                     check={this.state.check}
                     handleInput={this.handleInput}
                     handleCredentialInput={this.handleCredentialInput}
                     handlePlaceholder={this.handlePlaceholder}
                     addFields={this.addFields}
                     removeField={this.removeField}
                     confirm={this.confirm} />}
      </SimpleModalTemplate>
    )
  }
}

export default ChooseAppCredentialsModal;