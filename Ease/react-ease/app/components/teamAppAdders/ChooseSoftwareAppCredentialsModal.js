import React from 'react';
import {connect} from "react-redux";
import {reduxActionBinder} from "../../actions/index";
import MagicLinkAdderModal from "./MagicLinkAdderModal";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {teamCreateSoftwareSingleCard} from "../../actions/appsActions";
import {showChooseSoftwareAppCredentialsModal} from "../../actions/modalActions";
import { Form, Button, Message, Label, Input, Icon, Segment, Checkbox, Container } from 'semantic-ui-react';
import {handleSemanticInput, transformWebsiteInfoIntoList, credentialIconType, transformCredentialsListIntoObject} from "../../utils/utils";
import {showUpgradeTeamPlanModal} from "../../actions/teamModalActions";

const CredentialInput = ({item, onChange, removeField}) => {
  return (
    <Form.Field style={{position:'relative'}}>
      <label style={{fontSize: '16px', fontWeight: '300', color: '#424242',display:'inline-flex',width:'120px'}}>{item.placeholder}</label>
      <Icon name='circle' size='large' style={{position:'absolute',bottom:'36px',left:'354px',zIndex:'1',color:'white',margin:'0'}} />
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

const OtherInput = ({item, onChange, onChangePlaceholder, onFocus, removeField}) => {
  return (
    <Form.Field style={{position:'relative'}}>
      <Input id={item.priority} onFocus={onFocus} transparent style={{fontSize:'16px',fontWeight:'300',color:'#424242',display:'inline-flex',width:'120px'}} value={item.placeholder} onChange={onChangePlaceholder} required/>
      <Icon name='circle' size='large' style={{position:'absolute',bottom:'36px',left:'354px',zIndex:'1',color:'white',margin:'0'}} />
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

@connect(store => ({
  teams: store.teams,
  card: store.teamCard,
  receivers: store.modals.chooseSoftwareAppCredentials.receivers
}))
class ChooseHow extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  handleFocus = (e) => {
    e.target.select();
  };
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
      check,
      change,
      confirm,
      teamPro,
      upgrade,
      appName,
      loading,
      addFields,
      logoLetter,
      credentials,
      removeField,
      handlePlaceholder,
      handleCredentialInput
    } = this.props;
    const credentialsInputs = credentials.map(item => {
      if (item.name !== 'login' && item.name !== 'password')
        return <OtherInput key={item.priority} onChange={handleCredentialInput} onChangePlaceholder={handlePlaceholder} onFocus={this.handleFocus} removeField={removeField} item={item}/>;
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
          <Checkbox radio
                    style={{fontSize:'16px',marginBottom:'10px'}}
                    label={<label><strong>Enter</strong> login & password <strong>myself</strong></label>}
                    name='checkboxRadioGroup'
                    value={1}
                    checked={check === 1}
                    onChange={change} />
          {credentialsInputs}
          <p><span onClick={addFields} className='add_field'><Icon name='plus circle'/>Add a field</span></p>
          <Message error content={this.state.errorMessage}/>
          {this.props.receivers.filter(user => (user.id !== this.props.teams[this.props.card.team_id].my_team_user_id)).length > 0 &&
          <Checkbox radio
                    style={{fontSize:'16px',marginBottom:'10px'}}
                    label={<label><strong>Ask</strong> login & password from <strong>a team member</strong></label>}
                    name='checkboxRadioGroup'
                    value={2}
                    checked={check === 2}
                    onChange={change} />}
          <Checkbox radio
                    style={{fontSize:'16px'}}
                    label={teamPro ? <label><strong>Ask</strong> login & password from <strong>outside my team</strong></label>
                      : <label style={{display:'inline-flex'}}>
                        <span><strong>Ask</strong> login & password from <strong>outside my team</strong></span>
                        <img src="/resources/images/upgrade.png" style={{height:'23px'}}/>
                      </label>}
                    name='checkboxRadioGroup'
                    value={3}
                    checked={check === 3}
                    onChange={teamPro ? change : upgrade} />
          <Button
            type="submit"
            loading={loading}
            disabled={loading || (check === 1 && !this.checkValueInput()) || credentials.length < 1}
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
  settingsCard: store.modals.chooseSoftwareAppCredentials,
  receivers: store.modals.chooseSoftwareAppCredentials.receivers
}), reduxActionBinder)
class ChooseSoftwareAppCredentialsModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 1,
      check: 1,
      me: null,
      users: null,
      priority: 2,
      loading: false,
      teamPro: this.props.teams[this.props.card.team_id].plan_id > 0,
      userSelected: this.props.teams[this.props.card.team_id].my_team_user_id,
      credentials: transformWebsiteInfoIntoList(this.props.card.app.information)
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
  handleChangeCheck = (e, {value}) => this.setState({check: value});
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
  upgrade = () => {
    this.close();
    this.props.dispatch(showUpgradeTeamPlanModal({
      active: true,
      feature_id: 8,
      team_id: this.props.card.team_id
    }));
  };
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
    this.props.dispatch(showChooseSoftwareAppCredentialsModal({active: false}));
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const receivers = this.props.receivers.map(item => ({
      [item.id]: {allowed_to_see_password: item.can_see_information}
    }));
    const newReceivers = receivers.reduce(function (result, item) {
      result = Object.assign(result, item);
      return result;
    }, {});
    const connection_information = this.state.check !== 1 ? {
        login: {placeholder: "Login", priority: 0, type: "text", value: ""},
        password: {placeholder: "Password", priority: 1, type: "password", value: ""}
      }
      : this.state.credentials.reduce((prev, curr) => {
        return {...prev, [curr.name]: {type: curr.type, priority: curr.priority, placeholder: curr.placeholder}}
      }, {});
    if (this.state.view === 1 && this.state.check === 2)
      this.setState({view: 2, loading: false});
    else if (this.state.view === 3)
      this.setState({loading: false}, this.close());
    else {
      this.props.dispatch(teamCreateSoftwareSingleCard({
        team_id: this.props.card.team_id,
        channel_id: this.props.card.channel_id,
        name: this.props.settingsCard.card_name,
        description: this.props.settingsCard.description,
        password_reminder_interval: this.props.settingsCard.password_reminder_interval,
        team_user_filler_id: this.state.view === 2 ? this.state.userSelected : null,
        account_information: this.state.check !== 1 ? {} : transformCredentialsListIntoObject(this.state.credentials),
        logo_url: this.props.settingsCard.img_url,
        connection_information: connection_information,
        receivers: newReceivers,
        generate_magic_link: this.state.check === 3
      })).then(response => {
        if (this.state.check === 3)
          this.setState({view: 3, loading: false, link: response.magic_link, website: response.software});
        else
          this.setState({loading: false}, this.close());
        this.props.resetTeamCard();
      });
    }
  };
  render() {
    return (
      <SimpleModalTemplate
        onClose={this.state.view !== 3 ? this.close : null}
        headerContent={'App Credentials'}>
        {this.state.view === 1 &&
        <ChooseHow confirm={this.confirm}
                   upgrade={this.upgrade}
                   check={this.state.check}
                   addFields={this.addFields}
                   teamPro={this.state.teamPro}
                   loading={this.state.loading}
                   website={this.props.card.app}
                   handleInput={this.handleInput}
                   removeField={this.removeField}
                   change={this.handleChangeCheck}
                   credentials={this.state.credentials}
                   logo={this.props.settingsCard.img_url}
                   handlePlaceholder={this.handlePlaceholder}
                   appName={this.props.settingsCard.card_name}
                   logoLetter={this.props.settingsCard.logoLetter}
                   handleCredentialInput={this.handleCredentialInput} />}
        {this.state.view === 2 &&
        <ChoosePersonWhoHasCredentials me={this.state.me}
                                       confirm={this.confirm}
                                       users={this.state.users}
                                       change={this.handleChange}
                                       loading={this.state.loading}
                                       receivers={this.props.receivers}
                                       logo={this.props.settingsCard.img_url}
                                       userSelected={this.state.userSelected}
                                       appName={this.props.settingsCard.card_name}
                                       logoLetter={this.props.settingsCard.logoLetter} />}
        {this.state.view === 3 &&
        <MagicLinkAdderModal me={this.state.me}
                             link={this.state.link}
                             confirm={this.confirm}
                             change={this.handleChange}
                             loading={this.state.loading}
                             website={this.state.website}
                             appName={this.props.settingsCard.card_name}/>}
      </SimpleModalTemplate>
    )
  }
}

export default ChooseSoftwareAppCredentialsModal;