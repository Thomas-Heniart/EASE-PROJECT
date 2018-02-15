import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import {testCredentials} from "../../../actions/catalogActions";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {handleSemanticInput} from "../../../utils/utils";
import {Container, Icon, Form, Message, Button, Checkbox, Input } from 'semantic-ui-react';
import NewAccountAnyCredentialInputs from "./NewAccountAnyCredentialInputs";

@connect(store => ({
    modal: store.modals.newAccountUpdate,
    teams: store.teams,
}))
class NewAccountUpdateModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: '',
      appName: '',
      teamName: [],
      check: '',
      loading: false,
      seePassword: false,
      website: {},
      account_information: this.props.modal.account_information,
      editCredentials: {},
      priority: 2
    }
  }

  handleInput = handleSemanticInput.bind(this);
  handleFocus = (e) => {
    e.target.select();
  };
  handleChange = (e, {value}) => this.setState({check: value});
  handleCredentialsInput = (e, {name, value}) => {
    let account_information = {...this.state.account_information};
    account_information[name] = value;
    this.setState({account_information: account_information});
  };
  handlePlaceholder = (e, {id, value}) => {
    let website = {...this.state.website};
    Object.keys(this.state.website.information).map(item => {
      if (id === this.state.website.information[item].priority) {
        website.information[item].placeholder = value;
      }
    });
    this.setState({website: website});
  };
  addFields = () => {
    let website = {...this.state.website};
    let acc_info = {...this.state.account_information};
    let edit = {...this.state.editCredentials};
    website.information[`${this.state.priority}`] = {
      name: `${this.state.priority}`,
      placeholder: "Click to rename",
      priority: this.state.priority,
      type: "text"
    };
    acc_info[`${this.state.priority}`] = '';
    edit[`${this.state.priority}`] = true;
    this.setState({
      website: website,
      account_information: acc_info,
      priority: this.state.priority + 1,
      editCredentials: edit
    });
  };
  removeField = (priority, name) => {
    let website = {...this.state.website};
    website.information = {};
    let acc_info = {};
    Object.keys(this.state.account_information).filter(item => {
      if (item !== name)
        acc_info[item] = this.state.account_information[item];
    });
    Object.keys(this.state.website.information).filter(item => {
      return this.state.website.information[item].priority !== priority;
    }).sort((a, b) => {
      return this.state.website.information[a].priority - this.state.website.information[b].priority;
    }).map((item, idx) => {
      website.information[item] = this.state.website.information[item];
      website.information[item].priority = idx;
    });
    this.setState({website: website, account_information: acc_info});
  };
  toggleCredentialEdit = (name) => {
    const editCredentials = {};
    Object.keys(this.state.editCredentials).map(item => {
      if (item === name)
        editCredentials[item] = !this.state.editCredentials[item];
      else
        editCredentials[item] = this.state.editCredentials[item];
    });
    this.setState({editCredentials: editCredentials});
  };
  toggleSeePassword = () => {
    this.setState({seePassword: !this.state.seePassword});
  };
  testConnection = () => {
    this.props.dispatch(testCredentials({
      account_information: this.state.account_information,
      website_id: this.state.website.id,
    }));
  };
  close = () => {
    this.props.modal.reject();
  };
  edit = () => {
    this.props.modal.resolve({
      account_information: this.state.account_information,
      website: this.state.website,
      appName: this.state.appName,
      teamId: this.state.check
    });
  };
  logoLetter = () => {
    let first = '';
    let second = '';
    let space = false;
    for (let letter = 0; letter < this.state.appName.length; letter++) {
      if (first.length < 1 && this.state.appName[letter] !== ' ')
        first = this.state.appName[letter];
      else if (first.length > 0 && second.length < 1 && this.state.appName[letter] !== ' ' && space === true)
        second = this.state.appName[letter];
      else if (this.state.appName[letter] === ' ')
        space = true;
    }
    if (second !== '')
      return first.toUpperCase() + second.toUpperCase();
    else
      return first.toUpperCase();
  };

  componentWillMount() {
    let teamName = [];
    let edit = {};
    let website = {...this.props.modal.website};
    website.information = {};
    Object.keys(this.props.modal.website.information).map(item => {
      website.information[item] = {...this.props.modal.website.information[item]}
    });
    Object.keys(this.props.teams).map(team => {
      teamName.push(this.props.teams[team]);
    });
    Object.keys(this.state.account_information).map(item => {
      edit[item] = false
    });
    this.setState({editCredentials: edit, teamName: teamName, website: website});
  }

  render() {
    return (
      <SimpleModalTemplate
        onClose={this.close}
        headerContent={"New Account detected"}>
        <Container className="app_settings_modal">
          <Form onSubmit={this.edit} error={this.state.error.length > 0} id='add_bookmark_form'>
            <div className="app_name_container display-flex align_items_center">
              {this.state.website.logo && this.state.website.logo !== '' ?
                <div className="squared_image_handler">
                  <img src={this.state.website.logo} alt="Website logo"/>
                </div>
                :
                <div className="squared_image_handler" style={{backgroundColor:'#373b60',color:'white',fontSize:'24px',backgroundSize:'cover',display:'flex'}}>
                  <div style={{margin:'auto'}}>
                    <p style={{margin:'auto'}}>{this.logoLetter()}</p>
                  </div>
                </div>}
              <Input
                placeholder="Name your App"
                size='large'
                name='appName'
                className="modalInput team-app-input"
                onChange={this.handleInput}
                required/>
            </div>
            {!this.state.website.url &&
            <React.Fragment>
              <CredentialInputs
                edit={this.state.editCredentials}
                toggle={this.toggleCredentialEdit}
                seePassword={this.state.seePassword}
                handleChange={this.handleCredentialsInput}
                toggleSeePassword={this.toggleSeePassword}
                information={this.state.website.information}
                account_information={this.state.account_information}/>
              <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>
            </React.Fragment>}
            {this.state.website.url &&
            <React.Fragment>
              <NewAccountAnyCredentialInputs
                handleFocus={this.handleFocus}
                removeField={this.removeField}
                edit={this.state.editCredentials}
                toggle={this.toggleCredentialEdit}
                seePassword={this.state.seePassword}
                handlePlaceholder={this.handlePlaceholder}
                handleChange={this.handleCredentialsInput}
                toggleSeePassword={this.toggleSeePassword}
                information={this.state.website.information}
                account_information={this.state.account_information}/>
              <p><span onClick={this.addFields} className='add_field'><Icon name='plus circle'/>Add a field</span></p>
            </React.Fragment>}
            <Form.Field>
              <div style={{fontWeight: 'bold'}}>I use this account for:</div>
            </Form.Field>
            <Form.Field className='choose_type_app'>
              {
                this.state.teamName.map(team => {
                  return <Checkbox radio
                                   key={team.id}
                                   style={{margin: "0 0 10px 0"}}
                                   name='check'
                                   value={team.id}
                                   onChange={this.handleChange}
                                   label={team.name}
                                   checked={this.state.check === team.id}/>
                })
              }
              <Checkbox radio
                        name='check'
                        value='newApp'
                        label='Personal Account'
                        onChange={this.handleChange}
                        checked={this.state.check === 'newApp'}/>
            </Form.Field>
            <Message error content={this.state.error}/>
            <Button
              positive
              type="submit"
              className="modal-button"
              content="NEXT"
              loading={this.state.loading}
              disabled={this.state.loading || this.state.check === ''}/>
          </Form>
        </Container>
      </SimpleModalTemplate>
    )
  }
}

export default NewAccountUpdateModal;