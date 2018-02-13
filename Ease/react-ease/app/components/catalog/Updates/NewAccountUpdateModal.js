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
            check: '',
            loading: false,
            website: this.props.modal.website,
            account_information: this.props.modal.account_information
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
        const credentials = this.state.credentials.map(item => {
            if (item.name === name)
                item.edit = !item.edit;
            if (!item.edit)
                item.value = this.props.app.account_information[item.name];
            return item;
        });
        this.setState({credentials: credentials});
    };
    testConnection = () => {
        this.props.dispatch(testCredentials({
            account_information: this.state.account_information,
            website_id: this.state.website.id
        }));
    };
    close = () => {
        this.props.modal.reject();
    };
    edit = () => {
        console.log('submit');
        this.props.modal.resolve();
    };
    componentWillMount() {
        const teamName = [];
        Object.keys(this.props.teams).map(team => {
            return teamName.push(this.props.teams[team]);
        });
        console.log('result mapping',teamName);
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
                                    required/>
                            </div>
                        </div>
                        <CredentialInputs
                            toggle={this.toggleCredentialEdit}
                            handleChange={this.handleCredentialsInput}
                            information={this.state.website.information}
                            account_information={this.state.account_information}/>
                        <span id='test_credentials' onClick={this.testConnection}>Test connection <Icon color='green' name='magic'/></span>
                        <Form.Field>
                            <div style={{fontWeight:'bold'}}>I use this account for:</div>
                        </Form.Field>
                        <Form.Field className='choose_type_app'>
                            <Checkbox radio
                                      name='check'
                                      value='Simple'
                                      onChange={this.handleChange}
                                      label={this.teamName.map(team => {
                                          return team.name + ' ,';
                                      })}
                                      checked={this.state.check === 'Simple'}/>
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