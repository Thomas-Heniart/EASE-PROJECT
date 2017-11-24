import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import {getLogo} from "../../utils/api";
import {Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import {showLinkAppSettingsModal} from "../../actions/modalActions";
import {handleSemanticInput} from "../../utils/utils";
import {editLinkApp, validateApp} from "../../actions/dashboardActions";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";

@connect(store => ({
  app: store.modals.linkAppSettings.app
}))
class LinkAppSettingsModal extends Component {

  constructor(props){
    super(props);
    this.state = {
      view: 'Account',
      url: this.props.app.url,
      appName: this.props.app.name,
      logo: this.props.app.logo,
      loading: false,
      edit: false,
      errorMessage: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  linkInput = (e, {value}) => {
    this.setState({url: value});
    getLogo({url: value}).then(response => {
      this.setState({logo:response});
    });
  };
  toggleEdit = () => {
    this.setState({edit: !this.state.edit});
  };
  close = () => {
    this.props.dispatch(showLinkAppSettingsModal({active:false}));
  };
  remove = () => {
    this.close();
  };
  edit = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editLinkApp({
      app_id: this.props.app.id,
      name: this.state.appName,
      url: this.state.url,
      img_url: this.state.logo
    })).then(response => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  componentWillMount(){
    if (this.props.app.new)
      this.props.dispatch(validateApp({
        app_id: this.props.app.id
      }));
  }
  render(){
    const {view, logo} = this.state;

    return (
        <SimpleModalTemplate
            headerContent={"App settings"}
            onClose={this.close}>
          <Container class="app_settings_modal">
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={logo} alt="Website logo"/>
              </div>
              <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu view={view} onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form onSubmit={this.edit} error={!!this.state.errorMessage.length}>
              <Form.Field>
                <label>Here is the link</label>
                <div class="display_flex align_items_center">
                  <LabeledInput value={this.state.url} disabled={!this.state.edit} name="url" icon="home" placeholder="https://ease.space" onChange={this.linkInput}/>
                  <Icon name="pencil" fitted link style={{paddingLeft:'15px'}} onClick={this.toggleEdit}/>
                </div>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Button
                  type="submit"
                  loading={this.state.loading}
                  positive
                  className="modal-button"
                  content="CONFIRM"/>
            </Form>}
            {view === 'Remove' &&
            <RemoveSection onRemove={this.remove}/>}
            {view === 'Share' &&
            <ShareSection/>}
          </Container>
        </SimpleModalTemplate>
    )
  }
}

export default LinkAppSettingsModal;