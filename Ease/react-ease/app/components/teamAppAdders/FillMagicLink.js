import React, {Component} from 'react';
import {connect} from "react-redux";
import queryString from "query-string";
import {getTeamCardFromMagicLink, sendCredentialsToTeam} from "../../actions/magicLinkActions";
import {Input, Icon, Button, Label, Form, Loader} from 'semantic-ui-react';
import {credentialIconType, transformCredentialsListIntoObject, transformWebsiteInfoIntoList} from "../../utils/utils";

const ClassicCredentialInput = ({item, onChange}) => {
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
      <Icon name='circle' style={{position:'absolute',bottom:'36px',left:'354px',zIndex:'1',color:'white',margin:'0'}} />
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

@connect()
class FillMagicLink extends Component {
  constructor(props) {
    super(props);
    this.state = {
      error: false,
      team_card: {},
      username: '',
      email: '',
      teamName: '',
      credentials: [],
      loaded: false,
      query: null,
      loading: false
    }
  }

  componentWillMount() {
    const query = queryString.parse(this.props.location.search);
    this.props.dispatch(getTeamCardFromMagicLink({
      card_id: query.card_id,
      uuid: query.uuid
    })).then(response => {
      this.setState({
        loaded: true,
        query: query,
        team_card: response,
        email: response.extra_information.email,
        username: response.extra_information.username,
        teamName: response.extra_information.team_name,
        credentials: transformWebsiteInfoIntoList(response.website ? response.website.information : response.software.connection_information)
      });
    }).catch(error => {
      if (error)
        this.setState({loaded: true, error: true});
    });
  }

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
  testCredentials = () => {
    this.props.dispatch(testCredentials({
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      website_id: this.state.team_card.website.id
    }));
  };
  addFields = () => {
    let inputs = this.state.credentials.slice();
    const newInput = {
      name: "other",
      placeholder: "Click to rename",
      priority: this.state.priority,
      type: "text",
      value: ""
    };
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
  confirm = () => {
    const connection_information = this.state.credentials.reduce((prev, curr) => {
      return {...prev, [curr.name]: {type: curr.type, priority: curr.priority, placeholder: curr.placeholder}}
    }, {});
    this.props.dispatch(sendCredentialsToTeam({
      card_id: this.state.query.card_id,
      uuid: this.state.query.uuid,
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      type: this.state.team_card.sub_type,
      url: this.state.team_card.sub_type === 'any' ? this.state.team_card.url : null,
      img_url: this.state.team_card.sub_type !== 'classic' ? this.state.team_card.logo : null,
      connection_information: connection_information
    }));
  };

  render() {
    let credentialsInputs = null;
    if (!this.state.error && this.state.loaded) {
      if (this.state.team_card.software) {
        this.state.team_card.website = this.state.team_card.software;
        this.state.team_card.website.information = this.state.team_card.software.connection_information;
      }
      credentialsInputs = this.state.credentials.map(item => {
        if (this.state.team_card.sub_type === 'classic')
          return <ClassicCredentialInput key={item.priority} onChange={this.handleCredentialInput} item={item}/>;
        else if (item.name !== 'login' && item.name !== 'password')
          return <OtherInput key={item.priority} onChange={this.handleCredentialInput}
                             onChangePlaceholder={this.handlePlaceholder} onFocus={this.handleFocus}
                             removeField={this.removeField} item={item}/>;
        else
          return <CredentialInput key={item.priority} onChange={this.handleCredentialInput}
                                  removeField={this.removeField} item={item}/>;
      });
    }
    return (
      <React.Fragment>
        <header id="ease_header">
          <a className="logo_container" href="/">
            <img src="/resources/images/ease_logo_white.svg" alt="logo"/>
          </a>
          <div className="full_flex"/>
        </header>
        {(this.state.error && this.state.loaded) &&
        <div>This link was valid only for 24hours and is now <strong>expired</strong>.
          Please contact the person who sent it to you.</div>}
        {(!this.state.error && this.state.loaded) &&
        <React.Fragment>
          <p>Send login and password to {this.state.username} ({this.state.email}), from {this.state.teamName}</p>
          <div>
            <div className="ease_popup ease_team_popup">
              <Form onSubmit={this.confirm}>
                <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
                  <div className="squared_image_handler">
                    <img src={this.state.team_card.website.logo} alt="Website logo"/>
                  </div>
                  <span className="app_name">{this.state.team_card.name}</span>
                </div>
                {credentialsInputs}
                {this.state.team_card.sub_type !== 'classic' &&
                <p><span onClick={this.addFields} className='add_field'><Icon name='plus circle'/>Add a field</span>
                </p>}
                {this.state.team_card.sub_type === 'classic' &&
                <span id='test_credentials' onClick={this.testCredentials}>Test connection <Icon color='green' name='magic'/></span>}
                <Button
                  type="submit"
                  loading={this.state.loading}
                  disabled={this.state.loading}
                  positive
                  className="modal-button uppercase"
                  content={'CONFIRM'}/>
              </Form>
            </div>
            <div>
              <p>Privacy and security tips 🤝</p>
              <div><p>Transfer the login and password only if you know the person.</p></div>
              <div><p>The transfer is <strong>encrypted end-to-end</strong> using internationally recognized encryption
                methods</p></div>
              <div><p>The URL of this page will always start with https://ease.space.</p></div>
            </div>
          </div>
        </React.Fragment>}
        <Loader active={!this.state.loaded}/>
        <p>At Ease.space we help companies secure, use and share their passwords.
          <a href='https://ease.space/contact' target='_blank'>Contact us</a> if you have any question.</p>
      </React.Fragment>
    )
  }
}

export default FillMagicLink;