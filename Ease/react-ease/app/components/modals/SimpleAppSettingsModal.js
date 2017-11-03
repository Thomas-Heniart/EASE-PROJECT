import React, {Component} from "react";
import AppSettingsNameInput from "./AppSettingsNameInput";
import {handleSemanticInput} from "../../utils/utils";
import {Image,List,Segment, Grid,Loader,Checkbox,Message, Input, Label,Form, Menu, Icon, Container, Button} from 'semantic-ui-react';
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showSimpleAppSettingsModal} from "../../actions/modalActions";
import {AppSettingsMenu, ShareSection, RemoveSection, LabeledInput} from "./utils";
import {connect} from "react-redux";

@connect()
class SimpleAppSettingsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      appName: 'facebook',
      view: 'Account'
    }
  }
  handleInput = handleSemanticInput.bind(this);
  close = () => {
    this.props.dispatch(showSimpleAppSettingsModal({active: false}));
  };
  changeView = (e, {name}) => {
    this.setState({view: name});
  };
  remove = () => {
    return new Promise((resolve, reject) => {
      resolve();
      this.close();
    });
  };
  render(){
    const {view} = this.state;

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={"App settings"}>
          <Container class="app_settings_modal">
            <div class="display-flex align_items_center">
              <div class="squared_image_handler">
                <img src={'/resources/websites/Facebook/logo.png'} alt="Website logo"/>
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
                  <Icon name="pencil" fitted link style={{paddingLeft:'15px'}}/>
                </div>
              </Form.Field>
              <Form.Field>
                <label>Password</label>
                <div class="display_flex align_items_center">
                  <Input
                      fluid
                      icon
                      className="modalInput team-app-input"
                      size='large'
                      type={'password'}
                      name={'password'}
                      placeholder={'Password'}
                      labelPosition='left'>
                    <Label><Icon name="lock"/></Label>
                    <input/>
                    <Icon name="copy" link/>
                  </Input>
                  <Icon name="pencil" fitted link style={{paddingLeft:'15px'}}/>
                </div>
              </Form.Field>
              <Form.Field>
                <span>Modifications will also apply to:</span>
                <Segment class="pushable ssoListSegment">
                  <Grid columns={2} class="ssoListGrid">
                    <Grid.Column class="showSegment">
                      <div class="display_flex align_items_center">
                        <img class="appLogo" src={'/resources/websites/Facebook/logo.png'}/>
                        <span class="overflow-ellipsis">Facebook</span>
                      </div>
                    </Grid.Column>
                    <Grid.Column class="showSegment">
                      <div class="display_flex align_items_center">
                        <img class="appLogo" src={'/resources/websites/Facebook/logo.png'}/>
                        <span class="overflow-ellipsis">Facebook</span>
                      </div>
                    </Grid.Column>
                    <Grid.Column class="showSegment">
                      <div class="display_flex align_items_center">
                        <img class="appLogo" src={'/resources/websites/Facebook/logo.png'}/>
                        <span class="overflow-ellipsis">Facebook</span>
                      </div>
                    </Grid.Column>
                  </Grid>
                </Segment>
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

export default SimpleAppSettingsModal;