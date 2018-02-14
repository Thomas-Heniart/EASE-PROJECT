import React from 'react';
import {connect} from "react-redux";
import CredentialInputs from "./CredentialInputs";
import {testCredentials} from "../../../actions/catalogActions";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {handleSemanticInput} from "../../../utils/utils";
import {Container, Icon, Form, Message, Button, Checkbox } from 'semantic-ui-react';

@connect(store => ({
    modal: store.modals.newAccountUpdate,
    teams: store.teams,
}))
class NewAccountUpdateModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            error: '',
            appName: '',
            teamName: [],
            check: '',
            loading: false,
            seePassword: false,
            website: this.props.modal.website,
            account_information: this.props.modal.account_information,
            editCredentials: {}
        }
    }
    handleInput = handleSemanticInput.bind(this);
    handleChange = (e, {value}) => this.setState({check: value});
    handleCredentialsInput = (e, {name, value}) => {
        let account_information = {...this.state.account_information};
        account_information[name] = value;
        this.setState({account_information: account_information});
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
        console.log('submit');
        this.props.modal.resolve({
            account_information: this.state.account_information,
            website: this.state.website,
            appName: this.state.appName,
            check: this.state.check
        });
    };
    handleAppName = e => {
        this.setState({appName: e.target.value});
    };
    componentWillMount() {
        let teamName = [];
        Object.keys(this.props.teams).map(team => {
            teamName.push(this.props.teams[team]);
        });
        this.setState({teamName: teamName});
        let edit = {};
        Object.keys(this.state.account_information).map(item => {
            edit[item] = false
        });
        this.setState({editCredentials: edit});
    }
    render() {
        return (
            <SimpleModalTemplate
                onClose={this.close}
                headerContent={"New Account detected"}>
                <Container className="app_settings_modal">
                    <Form onSubmit={this.edit} error={this.state.error.length > 0} id='add_bookmark_form'>
                        <div className="app_name_container display-flex align_items_center">
                            <div className="squared_image_handler">
                                <img src="/resources/icons/link_app.png" alt="Website Logo"/>
                            </div>
                            <div className="ui input">
                                <input
                                    placeholder="Name your App"
                                    size='large'
                                    className ="modalInput ui.input team-app-input"
                                    onChange={this.handleAppName}
                                    required/>
                            </div>
                        </div>
                        <CredentialInputs
                            edit={this.state.editCredentials}
                            toggle={this.toggleCredentialEdit}
                            seePassword={this.state.seePassword}
                            handleChange={this.handleCredentialsInput}
                            toggleSeePassword={this.toggleSeePassword}
                            information={this.state.website.information}
                            account_information={this.state.account_information}/>
                        <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>
                        <Form.Field>
                            <div style={{fontWeight:'bold'}}>I use this account for:</div>
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
                            disabled={this.state.loading}/>
                    </Form>
                </Container>
            </SimpleModalTemplate>
        )
    }
}

export default NewAccountUpdateModal;