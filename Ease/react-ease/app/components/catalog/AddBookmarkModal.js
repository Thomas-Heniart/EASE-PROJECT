import React, {Component} from "react";
import {dashboard} from "../../utils/post_api";
var api = require('../../utils/api');
import {handleSemanticInput} from "../../utils/utils";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import {Button,List, Form,Input, Loader, Icon, Message, Container} from 'semantic-ui-react';
import { Switch, Route, withRouter} from 'react-router-dom';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

@connect(store => ({
  modal: store.teamModals.catalogAddBookmarkModal,
  profiles: store.dashboard.profiles
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
      profileLoading: false,
      selectedProfile: -1,
      selectedTeam: -1,
      selectedRoom: -1
    }
  }
  handleInput = handleSemanticInput.bind(this);
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
    this.props.showCatalogAddBookmarkModal({active: false});
  };
  catalogToTeamSpace = (team_id, room_id) => {
    this.close();
    this.props.createTeamCard({
      team_id,
      channel_id: room_id,
      website: {},
      type: 'Link',
      name: this.state.name,
      url: this.state.url
    });
    this.props.history.push(`/teams/${team_id}/${room_id}`);
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    if (this.state.selectedRoom === -1) {
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
    }
    else
      this.catalogToTeamSpace(this.state.selectedTeam, this.state.selectedRoom);
  };
  componentWillMount(){
    this.setState({profileLoading: true});
    const profiles = Object.keys(this.props.profiles).map(item => {
      return this.props.profiles[item];
    });
    this.setState({profileLoading: false, profiles: profiles});
  }
  render(){
    // const profiles = this.state.profiles.map(profile => {
    //   return (
    //       <List.Item as="a"
    //                  key={profile.id}
    //                  class="display_flex"
    //                  active={this.state.profile_id === profile.id}
    //                  onClick={this.chooseProfile.bind(null, profile.id)}>
    //         <strong>{profile.name}</strong>
    //         &nbsp;&nbsp;
    //         <em class="overflow-ellipsis">
    //           {
    //             profile.apps.map(function(app, idx){
    //               var ret = app.name;
    //               ret += (idx === profile.apps.length - 1) ? '' : ', ';
    //               return (ret)
    //             }, this)
    //           }
    //         </em>
    //       </List.Item>
    //   )
    // });
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Choose app location'}>
          <ChooseAppLocationModal
            website={{logo: this.state.img_url}}
            appName={this.state.name}
            loading={this.state.loading}
            profiles={this.state.profiles}
            handleInput={this.handleInput}
            selectedProfile={this.state.selectedProfile}
            selectedRoom={this.state.selectedRoom}
            addProfile={this.addProfile}
            confirm={this.confirm}
            selectProfile={this.selectProfile}
            selectRoom={this.selectRoom} />
          {/*<Form class="container" id="add_bookmark_form" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>*/}
            {/*<Form.Field class="display-flex align_items_center" style={{marginBottom: '30px'}}>*/}
              {/*<div class="squared_image_handler">*/}
                {/*<img src={this.state.img_url} alt="Website logo"/>*/}
              {/*</div>*/}
              {/*<span class="app_name">{this.state.name}</span>*/}
            {/*</Form.Field>*/}
            {/*<Form.Field>*/}
              {/*<div style={{marginBottom: '10px'}}>App location (you can always change it later)</div>*/}
              {/*<Container class="profiles">*/}
                {/*<List link>*/}

                  {/*{this.state.profileLoading ?*/}
                      {/*<Loader inline={'centered'} active size="tiny"/>:*/}
                      {/*profiles}*/}
                {/*</List>*/}
                {/*{!this.state.profileLoading &&*/}
                {/*<form style={{marginBottom: 0}} onSubmit={this.createProfile}>*/}
                  {/*<Input*/}
                      {/*loading={this.state.addingProfile}*/}
                      {/*value={this.state.profileName}*/}
                      {/*name="profileName"*/}
                      {/*required*/}
                      {/*style={{fontSize:'14px'}}*/}
                      {/*transparent*/}
                      {/*onChange={this.handleInput}*/}
                      {/*class="create_profile_input"*/}
                      {/*icon={<Icon name="plus square" link onClick={this.createProfile}/>}*/}
                      {/*placeholder='Create new group'*/}
                  {/*/>*/}
                {/*</form>}*/}
              {/*</Container>*/}
            {/*</Form.Field>*/}
            {/*<Message error content={this.state.errorMessage}/>*/}
            {/*<Button*/}
                {/*attached='bottom'*/}
                {/*type="submit"*/}
                {/*loading={this.state.loading}*/}
                {/*positive*/}
                {/*disabled={this.state.profile_id === -1 || this.state.loading}*/}
                {/*onClick={this.confirm}*/}
                {/*className="modal-button"*/}
                {/*content="CONFIRM"/>*/}
          {/*</Form>*/}
        </SimpleModalTemplate>
    )
  }
}

export default withRouter(AddBookmarkModal);