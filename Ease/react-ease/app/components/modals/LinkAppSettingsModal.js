import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import {Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import {showLinkAppSettingsModal} from "../../actions/modalActions";
import {handleSemanticInput} from "../../utils/utils";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";

@connect()
class LinkAppSettingsModal extends Component{
  constructor(props){
    super(props);
    this.state = {
      view: 'Account',
      url: 'https://www.facebook.com',
      appName: 'lala',
      img_src: '/resources/websites/Facebook/logo.png',
      loading: false,
      edit: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  toggleEdit = () => {
    this.setState({edit: !this.state.edit});
  };
  close = () => {
    this.props.dispatch(showLinkAppSettingsModal({active:false}));
  };
  remove = () => {
    this.close();
  };
  render(){
    const {view, img_src} = this.state;

    return (
        <SimpleModalTemplate
            headerContent={"App settings"}
            onClose={this.close}>
          <Container class="app_settings_modal">
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={img_src} alt="Website logo"/>
              </div>
              <AppSettingsNameInput value={this.state.appName} onChange={this.handleInput}/>
            </div>
            <AppSettingsMenu view={view} onChange={this.handleInput}/>
            {view === 'Account' &&
            <Form>
              <Form.Field>
                <label>Here is the link</label>
                <div class="display_flex align_items_center">
                  <LabeledInput value={this.state.url} disabled={!this.state.edit} name="url" icon="home" placeholder="https://ease.space" onChange={this.handleInput}/>
                  <Icon name="pencil" fitted link style={{paddingLeft:'15px'}} onClick={this.toggleEdit}/>
                </div>
              </Form.Field>
              <Form.Field>
                <label>Here is the link</label>
                <div class="display_flex align_items_center">
                  <LabeledInput value={this.state.url} disabled={!this.state.edit} name="url" icon="home" placeholder="https://ease.space" onChange={this.handleInput}/>
                  <Icon name="pencil" fitted link style={{paddingLeft:'15px'}} onClick={this.toggleEdit}/>
                </div>
              </Form.Field>
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