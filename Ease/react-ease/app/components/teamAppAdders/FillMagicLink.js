import React, {Component} from 'react';
import {connect} from "react-redux";
import queryString from "query-string";
import {getTeamCardFromMagicLink, sendCredentialsToTeam} from "../../actions/magicLinkActions";
import {Input, Icon, Button, Label, Form, Loader} from 'semantic-ui-react';
import {
  credentialIconType, isUrl, transformCredentialsListIntoObject,
  transformWebsiteInfoIntoList
} from "../../utils/utils";
import {getClearbitLogo} from "../../utils/api";
import {testCredentials} from "../../actions/catalogActions";

const ClassicCredentialInput = ({item, onChange}) => {
  return (
    <Form.Field>
      <label style={{fontSize: '16px', fontWeight: '300', color: '#424242'}}>{item.placeholder}</label>
      <Input size="large"
             autoFocus={item.autoFocus}
             id={item.priority}
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
             type={item.type ? item.type : item.information_type}/>
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
             type={item.type ? item.type : item.information_type}/>
    </Form.Field>
  )
};

@connect()
class FillMagicLink extends Component {
  constructor(props) {
    super(props);
    this.state = {
      url: '',
      team_card: {},
      credentials: [],
      email: '',
      username: '',
      teamName: '',
      query: null,
      img_url: '',
      sent: false,
      error: false,
      loaded: false,
      loading: false,
      priority: 2
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
        url: response.sub_type === 'any' ? response.website.login_url : '',
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
  getLogo = () => {
    getClearbitLogo(this.state.url).then(response => {
      this.setState({img_url: response});
    }).catch(err => {
      this.setState({img_url: ''});
    });
  };
  changeUrl = (e, {value}) => {
    this.setState({url: value}, this.getLogo);
  };
  checkValueInput = () => {
    let i = 0;
    let j = 0;
    if (this.state.credentials.length === 0)
      return false;
    this.state.credentials.filter(item => {
      i++;
      if (item.value.length > 0) {
        j++;
        return item;
      }
    });
    return j === i;
  };
  testConnection = () => {
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
    this.setState({loading: true});
    const connection_information = this.state.credentials.reduce((prev, curr) => {
      return {...prev, [curr.name]: {type: curr.type, priority: curr.priority, placeholder: curr.placeholder}}
    }, {});
    this.props.dispatch(sendCredentialsToTeam({
      card_id: this.state.query.card_id,
      uuid: this.state.query.uuid,
      account_information: transformCredentialsListIntoObject(this.state.credentials),
      type: this.state.team_card.sub_type,
      url: this.state.team_card.sub_type === 'any' ? this.state.url : null,
      img_url: this.state.img_url !== '' ? this.state.img_url : this.state.team_card.logo,
      connection_information: connection_information
    })).then(res => {
      this.setState({sent: true, loading: false});
    });
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
          <div className='nav'>
            <a href="https://ease.space/product" target='_blank'>PRODUCT</a>
            <a href="https://ease.space/security" target='_blank'>SECURITY</a>
            <a href="/?skipLanding=true" className="connection" target='_blank'>CONNECTION</a>
          </div>
        </header>
        {(this.state.error && this.state.loaded && !this.state.sent) &&
        <div className='expired'>This link was valid only for 24hours and is now <strong>expired</strong>.
          Please contact the person who sent it to you.</div>}
        {(!this.state.error && this.state.loaded && !this.state.sent) &&
        <React.Fragment>
          <p className='title'>Send login and password to <strong>{this.state.username} ({this.state.email})</strong>, from <strong>{this.state.teamName}</strong></p>
          <div className='content'>
            <div className="ease_popup ease_team_popup">
              <Form className="container" onSubmit={this.confirm}>
                <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
                  <div className="squared_image_handler">
                    <img src={this.state.img_url !== '' ? this.state.img_url : this.state.team_card.website.logo} alt="Website logo"/>
                  </div>
                  <span className="app_name">{this.state.team_card.name}</span>
                </div>
                {this.state.team_card.sub_type === 'any' &&
                <Form.Field>
                  <label style={{fontSize: '16px', fontWeight: '300', color: '#424242'}}>URL</label>
                  <Input className="modalInput team-app-input"
                         placeholder="Website URL"
                         size="large"
                         name="url"
                         type='url'
                         value={this.state.url}
                         autoComplete="off"
                         onChange={this.changeUrl}
                         label={<Label><Icon name="home"/></Label>}
                         labelPosition="left"
                         required/>
                </Form.Field>}
                {credentialsInputs}
                {this.state.team_card.sub_type !== 'classic' &&
                <p><span onClick={this.addFields} className='add_field'><Icon name='plus circle'/>Add a field</span>
                </p>}
                {this.state.team_card.sub_type === 'classic' &&
                <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>}
                <Button
                  positive
                  loading={this.state.loading}
                  disabled={this.state.loading || !this.checkValueInput()
                  || (this.state.team_card.sub_type === 'any' && !isUrl(this.state.url))}
                  className="modal-button uppercase"
                  content={'SEND'}/>
              </Form>
            </div>
            <div className='explication'>
              <p className='content_title'>Privacy and security tips 🤝</p>
              <div className='cadre'><p>Transfer the login and password only if you know the person.</p></div>
              <div className='cadre'><p>The transfer is <strong>encrypted end-to-end</strong> using internationally recognized encryption methods</p></div>
              <div className='cadre'><p>The URL of this page will always start with https://ease.space.</p></div>
            </div>
          </div>
        </React.Fragment>}
        {this.state.sent &&
        <div className='expired'>Thank you. We’ve just notified {this.state.username} about your sending. See you soon!</div>}
        <Loader active={!this.state.loaded}/>
        <p className='bottom_sentence'>At Ease.space we help companies secure, use and share their passwords.
          <a href='https://ease.space/contact' target='_blank'>Contact us</a> if you have any question.</p>
      </React.Fragment>
    )
  }
}

export default FillMagicLink;