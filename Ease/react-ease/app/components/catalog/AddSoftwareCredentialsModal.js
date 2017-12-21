import React from 'react';
import {handleSemanticInput, transformCredentialsListIntoObject, credentialIconType} from "../../utils/utils";
import { Container, Form, Message, Label, Button, Input, Icon } from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import { withRouter } from 'react-router-dom';
import {createProfile} from "../../actions/dashboardActions";
import {catalogAddSoftwareApp} from "../../actions/catalogActions";
import ChooseTypeAppModal from './ChooseTypeAppModal';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

const CredentialInput = ({item, onChange, removeField}) => {
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
             value={item.value}
             type={item.type}/>
    </Form.Field>
  )
};

const OtherInput = ({item, onChange, onChangePlaceholder, onFocus, removeField}) => {
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
             value={item.value}
             type={item.type}/>
    </Form.Field>
  )
};

class SecondStep extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      inputs: [
        {name: "login", placeholder: "Login", priority: 0, type: "text", value:""},
        {name: "password", placeholder:"Password", priority:1, type:"password", value:""}
      ],
      priority: 2,
      loading: false
    }
  }
  handleFocus = (e) => {
    e.target.select();
  };
  handleCredentialInput = (e, {id, value}) => {
    let credentials = this.state.inputs.map(item => {
      if (id === item.priority)
        item.value = value;
      return item;
    });
    this.setState({inputs: credentials});
  };
  handlePlaceholder = (e, {id, value}) => {
    let credentials = this.state.inputs.map(item => {
      if (id === item.priority) {
        item.placeholder = value;
        item.name = value.toLowerCase();
      }
      return item;
    });
    this.setState({inputs: credentials});
  };
  chooseColumn = () => {
    const columns = this.props.dashboard.columns.map((column, index) => {
      let apps = 0;
      column.map(item => {
        let tmp = this.props.dashboard.profiles[item].app_ids.length / 3;
        if (tmp <= Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 1;
        else if (tmp > Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 2;
        apps = apps + tmp;
      });
      if (apps > 0)
        return apps;
      else
        return 0;
    });
    let columnChoose = null;
    columns.map((column, index) => {
      let test = columns.slice();
      test.sort();
      if (column === test[0] && columnChoose === null)
        columnChoose = index;
    });
    return columnChoose;
  };
  addFields = () => {
    let inputs = this.state.inputs.slice();
    const newInput = {name:"other",placeholder:"Click to rename",priority:this.state.priority,type:"text",value:""};
    inputs.push(newInput);
    this.setState({inputs: inputs, priority: this.state.priority + 1});
  };
  removeField = (field) => {
    const inputs = this.state.inputs.filter(item => {
      return item.priority !== field.priority;
    }).map((item, idx) => {
      item.priority = idx;
      return item
    });
    this.setState({inputs: inputs});
  };
  send = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const connection_information = this.state.inputs.reduce((prev, curr) =>{
      return {...prev, [curr.name]: {type:curr.type,priority:curr.priority,placeholder:curr.placeholder}}
    }, {});
    if (this.props.profile_id === 0) {
      this.props.dispatch(createProfile({
        name: this.props.profileName,
        column_index: this.chooseColumn()
      })).then(response => {
        this.props.dispatch(catalogAddSoftwareApp({
          name: this.props.appName,
          logo_url: this.props.website.logo,
          profile_id: response.id,
          account_information: transformCredentialsListIntoObject(this.state.inputs),
          connection_information: connection_information
        })).then(response => {
          this.setState({loading: false});
          this.props.modal.resolve(response);
          this.props.showCatalogAddSoftwareAppModal({active: false});
        });
      });
    }
    else {
      this.props.dispatch(catalogAddSoftwareApp({
        name: this.props.appName,
        logo_url: this.props.website.logo,
        profile_id: this.props.profile_id,
        account_information: transformCredentialsListIntoObject(this.state.inputs),
        connection_information: connection_information
      })).then(response => {
        this.setState({loading: false});
        this.props.modal.resolve(response);
        this.props.showCatalogAddSoftwareAppModal({active: false});
      });
    }
  };
  render() {
    const {website, logoLetter, appName} = this.props;
    const credentialsInputs = this.state.inputs.map(item => {
      if (item.name !== 'login' && item.name !== 'password')
        return <OtherInput key={item.priority} onChange={this.handleCredentialInput} onChangePlaceholder={this.handlePlaceholder} onFocus={this.handleFocus} removeField={this.removeField} item={item}/>;
      else
        return <CredentialInput key={item.priority} onChange={this.handleCredentialInput} removeField={this.removeField} item={item}/>;
    });
    return (
      <Container>
        <div className="display-flex align_items_center" style={{marginBottom: '30px'}}>
          {website.logo ?
            <div className="squared_image_handler">
              <img src={website.logo} alt="Website logo"/>
            </div>
            :
            <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
              <div style={{margin:'auto'}}>
                <p style={{margin:'auto'}}>{logoLetter}</p>
              </div>
            </div>}
          <span className="app_name">{appName}</span>
        </div>
        <Form onSubmit={this.send}>
          {credentialsInputs}
          <p><span onClick={this.addFields} className='add_field'><Icon name='plus circle'/>Add a field</span></p>
          <Message error content={this.state.errorMessage}/>
          <Button
            type="submit"
            positive
            disabled={this.state.loading || this.state.inputs.length < 1}
            loading={this.state.loading}
            className="modal-button"
            content="CONFIRM"/>
        </Form>
      </Container>
    )
  }
}

@connect(store => ({
  profiles: store.dashboard.profiles,
  dashboard: store.dashboard,
  modal: store.teamModals.catalogAddSoftwareAppModal
}), reduxActionBinder)
class AddSoftwareCredentialsModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      view: 1,
      name: this.props.modal.name,
      img_url: this.props.modal.img_url,
      profile_id: -1,
      loading: false,
      errorMessage: '',
      profiles: [],
      profileName: '',
      profileAdded: false,
      addingProfile: false,
      profileLoading: false,
      selectedProfile: -1,
      selectedTeam: -1,
      selectedRoom: -1
    }
  }
  handleInput = handleSemanticInput.bind(this);
  componentWillMount(){
    this.setState({profileLoading: true});
    const profiles = Object.keys(this.props.profiles).map(item => {
      return this.props.profiles[item];
    });
    this.setState({profileLoading: false, profiles: profiles});
  }
  chooseColumn = () => {
    const columns = this.props.dashboard.columns.map((column, index) => {
      let apps = 0;
      column.map(item => {
        let tmp = this.props.dashboard.profiles[item].app_ids.length / 3;
        if (tmp <= Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 1;
        else if (tmp > Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 2;
        apps = apps + tmp;
      });
      if (apps > 0)
        return apps;
      else
        return 0;
    });
    let columnChoose = null;
    columns.map((column, index) => {
      let test = columns.slice();
      test.sort();
      if (column === test[0] && columnChoose === null)
        columnChoose = index;
    });
    return columnChoose;
  };
  createProfile = () => {
    const newProfile = {id: 0, name: this.state.profileName};
    if (this.state.profileName.length === 0)
      return;
    this.addProfile(newProfile);
    this.setState({profileAdded: true});
  };
  addProfile = (profile) => {
    let profiles = this.state.profiles.slice();
    profiles.push(profile);
    this.setState({profiles: profiles, selectedProfile: profile.id});
  };
  selectProfile = (id) => {
    this.setState({ selectedProfile: id, selectedTeam: -1, selectedRoom: -1 });
  };
  selectRoom = (teamId, roomId) => {
    this.setState({ selectedTeam: teamId, selectedRoom: roomId, selectedProfile: -1 });
  };
  close = () => {
    this.props.modal.reject();
    this.props.showCatalogAddSoftwareAppModal({active: false});
  };
  confirm = () => {
    if (this.state.selectedRoom === -1 && this.state.selectedProfile !== -1)
      this.setState({view: 2});
    else if (this.state.selectedRoom !== -1 && this.state.selectedProfile === -1)
      this.setState({view: 3});
    else
      this.createProfile();
  };
  render(){
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={'Choose app location'}>
        {this.state.view === 1 &&
        <ChooseAppLocationModal
          website={{logo: this.state.img_url}}
          logoLetter={this.props.modal.logoLetter}
          appName={this.state.name}
          loading={this.state.loading}
          profiles={this.state.profiles}
          handleInput={this.handleInput}
          selectedProfile={this.state.selectedProfile}
          selectedRoom={this.state.selectedRoom}
          profileAdded={this.state.profileAdded}
          bookmark={true}
          createProfile={this.createProfile}
          confirm={this.confirm}
          selectProfile={this.selectProfile}
          selectRoom={this.selectRoom} />}
        {this.state.view === 2 &&
        <SecondStep
          {...this.props}
          website={{logo: this.state.img_url}}
          appName={this.state.name}
          profile_id={this.state.selectedProfile}
          profileName={this.state.profileName}
          logoLetter={this.props.modal.logoLetter}/>}
        {this.state.view === 3 &&
        <ChooseTypeAppModal
          {...this.props}
          website={{
            logo: this.state.img_url,
            information: {
              login: {placeholder: "Login",priority:0,type:"text"},
              password: {placeholder:"Password",priority:1,type:"password"}
            }
          }}
          logoLetter={this.props.modal.logoLetter}
          appName={this.state.name}
          team_id={this.state.selectedTeam}
          room_id={this.state.selectedRoom}
          subtype={'softwareApp'}
          close={this.close} />}
      </SimpleModalTemplate>
    )
  }
}

export default withRouter(AddSoftwareCredentialsModal);