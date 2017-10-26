import React, {Component} from "react";
import {dashboard} from "../../utils/post_api";
var api = require('../../utils/api');
import {handleSemanticInput} from "../../utils/utils";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Button,List, Form,Input, Loader, Icon, Message, Container} from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

@connect(store => ({
  modal: store.teamModals.catalogAddBookmarkModal
}), reduxActionBinder)
class AddBookmarkModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      name: this.props.modal.name,
      url: this.props.modal.url,
      img_url: this.props.modal.img_url,
      profile_id: -1,
      loading: false,
      errorMessage: '',
      profiles: [],
      profileName: '',
      addingProfile: false,
      profileLoading: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  chooseProfile = (id) => {
    this.setState({profile_id: id});
  };
  createProfile = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (this.state.profileName.length === 0)
      return;
    this.setState({addingProfile: true});
    dashboard.createProfile({name: this.state.profileName}).then(response => {
      let profiles = this.state.profiles.slice();
      profiles.push(response);
      this.setState({profiles: profiles, profileName: '', profile_id: response.id, addingProfile: false});
    }).catch(err => {
      this.setState({addingProfile: false});
    });
  };
  close = () => {
    this.props.modal.reject();
    this.props.showCatalogAddBookmarkModal({active: false});
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    this.props.catalogAddBookmark({
      name: this.state.name,
      profile_id: this.state.profile_id,
      url: this.state.url,
      img_url: this.state.img_url
    }).then(app => {
      this.setState({loading: false});
      this.props.modal.resolve(app);
      this.props.showCatalogAddBookmarkModal({active: false});
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
    });
  };
  componentWillMount(){
    this.setState({profileLoading: true});
    api.dashboard.fetchProfiles().then(response => {
      this.setState({profiles: response, profileLoading: false});
    }).catch(err => {
      this.setState({profileLoading: false});
    });
  }
  render(){
    const profiles = this.state.profiles.map(profile => {
      return (
          <List.Item as="a"
                     key={profile.id}
                     class="display_flex"
                     active={this.state.profile_id === profile.id}
                     onClick={this.chooseProfile.bind(null, profile.id)}>
            <strong>{profile.name}</strong>
            &nbsp;&nbsp;
            <em class="overflow-ellipsis">
              {
                profile.apps.map(function(app, idx){
                  var ret = app.name;
                  ret += (idx === profile.apps.length - 1) ? '' : ', ';
                  return (ret)
                }, this)
              }
            </em>
          </List.Item>
      )
    });
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Choose app location'}>
          <Form class="container" id="add_bookmark_form" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>
              <div class="squared_image_handler">
                <img src={this.state.img_url} alt="Website logo"/>
              </div>
              <span class="app_name">{this.state.name}</span>
            </Form.Field>
            <Form.Field>
              <div style={{marginBottom: '10px'}}>App location (you can always change it later)</div>
              <Container class="profiles">
                <List link>

                  {this.state.profileLoading ?
                      <Loader inline={'centered'} active size="tiny"/>:
                      profiles}
                </List>
                {!this.state.profileLoading &&
                <form style={{marginBottom: 0}} onSubmit={this.createProfile}>
                  <Input
                      loading={this.state.addingProfile}
                      value={this.state.profileName}
                      name="profileName"
                      required
                      style={{fontSize:'14px'}}
                      transparent
                      onChange={this.handleInput}
                      class="create_profile_input"
                      icon={<Icon name="plus square" link onClick={this.createProfile}/>}
                      placeholder='Create new group'
                  />
                </form>}
              </Container>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                attached='bottom'
                type="submit"
                loading={this.state.loading}
                positive
                disabled={this.state.profile_id === -1 || this.state.loading}
                onClick={this.confirm}
                className="modal-button"
                content="CONFIRM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default AddBookmarkModal;