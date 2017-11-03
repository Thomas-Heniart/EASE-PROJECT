import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {connect} from "react-redux";
import {List, Segment, Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import {showLogWithAppSettingsModal} from "../../actions/modalActions";
import {handleSemanticInput} from "../../utils/utils";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";

@connect()
class LogWithAppSettings extends Component {
  constructor(props){
    super(props);
    this.state = {
      view: 'Account',
      appName: 'MyName'
    }
  }
  handleInput = handleSemanticInput.bind(this);
  close = () => {
    this.props.dispatch(showLogWithAppSettingsModal({active: false}));
  };
  remove = () => {

  };
  render(){
    const {view} = this.state;
    const img_src = '/resources/websites/Crisp/logo.png';

    return (
        <SimpleModalTemplate
            headerContent={'App settings'}
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
                <Segment.Group class={`logwith_app_selectors linkedin`}>
                  <Segment className='first'>
                    <Icon name='linkedin'/>
                    Select your Linkedin account
                  </Segment>
                  <Segment>
                    <List className="listCategory">
                      <List.Item key={1} as="p" active={true} onClick={undefined}>
                        <Icon name='user circle' />
                        <span>fisun.serge76@gmail.com</span>
                      </List.Item>
                      <p class="text-center errorMessage">
                        You don’t have a Linkedin Account setup yet. Please install one before all.
                      </p>
                    </List>
                  </Segment>
                </Segment.Group>
              </Form.Field>
              <Button
                  type="submit"
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

export default LogWithAppSettings;