import React, {Component} from "react";
import {handleSemanticInput} from "../../utils/utils";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import { withRouter} from 'react-router-dom';
import {createProfile} from "../../actions/dashboardActions";
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
      profileAdded: false,
      addingProfile: false,
      profileLoading: false,
      selectedProfile: -1,
      selectedTeam: -1,
      selectedRoom: -1
    }
  }
  handleInput = handleSemanticInput.bind(this);
  createProfile = () => {
    const newProfile = {id: 0, name: this.state.profileName};
    if (this.state.profileName.length === 0)
      return;
    this.addProfile(newProfile);
    this.setState({profileAdded: true});
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
    if (this.state.selectedRoom === -1 && this.state.selectedProfile !== -1) {
      this.setState({errorMessage: '', loading: true});
      if (this.state.selectedProfile === 0) {
        this.props.dispatch(createProfile({name: this.state.profileName, column_index: 1})).then(response => {
          const newProfile = response.id;
          this.props.catalogAddBookmark({
            name: this.state.name,
            profile_id: newProfile,
            url: this.state.url,
            img_url: this.state.img_url
          }).then(app => {
            this.setState({loading: false});
            this.props.modal.resolve(app);
            this.props.showCatalogAddBookmarkModal({active: false});
          }).catch(err => {
            this.setState({errorMessage: err, loading: false});
          });
        }).catch(err => {
          this.setState({loading: false, errorMessage: err});
        });
      }
      else {
        this.props.catalogAddBookmark({
          name: this.state.name,
          profile_id: this.state.selectedProfile,
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
    }
    else if (this.state.selectedRoom !== -1 && this.state.selectedProfile === -1) {
      this.setState({errorMessage: '', loading: true});
      this.catalogToTeamSpace(this.state.selectedTeam, this.state.selectedRoom);
    }
    else
      this.createProfile();
  };
  componentWillMount(){
    this.setState({profileLoading: true});
    const profiles = Object.keys(this.props.profiles).map(item => {
      return this.props.profiles[item];
    });
    this.setState({profileLoading: false, profiles: profiles});
  }
  render(){
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
            profileAdded={this.state.profileAdded}
            bookmark={true}
            createProfile={this.createProfile}
            confirm={this.confirm}
            selectProfile={this.selectProfile}
            selectRoom={this.selectRoom} />
        </SimpleModalTemplate>
    )
  }
}

export default withRouter(AddBookmarkModal);