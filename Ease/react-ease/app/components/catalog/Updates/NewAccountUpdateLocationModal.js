import React from 'react';
import {connect} from "react-redux";
import SimpleModalTemplate from "../../common/SimpleModalTemplate";
import {handleSemanticInput} from "../../../utils/utils";
import {Container, Form, Message, Button, Checkbox} from 'semantic-ui-react';

@connect(store => ({
    teams: store.teams,
}))
class NewAccountUpdateModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            error: '',
            check: '',
            loading: false,
            seePassword: false,
            website: this.props.modal.website,
            account_information: this.props.modal.account_information
        }
    }
    handleInput = handleSemanticInput.bind(this);
    handleChange = (e, {value}) => this.setState({check: value});
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
    edit = () => {
        console.log('submit');
        this.props.modal.resolve();
    };

    render() {
        return (
            <SimpleModalTemplate
                onClose={this.close}
                headerContent={"Choose App location"}>
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
                        <Form.Field>
                            <div style={{fontWeight:'bold'}}>Where would you like to send this app?</div>
                        </Form.Field>
                        <Form.Field className='choose_type_app'>
                            {/*{ // id team to map in room*/}
                                {/*this.state.teamName.map(team => {*/}
                                    {/*return <Checkbox radio*/}
                                                     {/*style={{margin: "0 0 10px 0"}}*/}
                                                     {/*name='check'*/}
                                                     {/*value={team.id}*/}
                                                     {/*onChange={this.handleChange}*/}
                                                     {/*label={team.name}*/}
                                                     {/*checked={this.state.check === team.id}/>*/}
                                {/*})*/}
                            {/*}*/}
                        <Checkbox radio
                                  name='check'
                                  value='toutdur1'
                                  label='Personal Account'
                                  checked={this.state.check === 'newApp'}/>
                        <Checkbox radio
                                  name='check'
                                  value='toutdur2'
                                  label='Personal Account'
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