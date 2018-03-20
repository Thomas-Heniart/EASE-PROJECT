import React, {Component} from "react";
import {handleSemanticInput} from "../../utils/utils";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import ChooseAppLocationModal from './ChooseAppLocationModal';
import { withRouter} from 'react-router-dom';
import {appAdded, createProfile} from "../../actions/dashboardActions";
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";

@connect(store => ({
  modal: store.teamModals.catalogAddBookmarkModal,
  profiles: store.dashboard.profiles,
  dashboard: store.dashboard
}), reduxActionBinder)
class AddBookmarkModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      check: null,
      checkRoom: null,
      name: this.props.modal.name,
      url: this.props.modal.url,
      img_url: this.props.modal.img_url,
      profile_id: -1,
      loading: false,
      errorMessage: '',
      profiles: [],
      selectedProfile: -1,
      selectedTeam: -1,
      selectedRoom: -1
    }
  }
  handleInput = handleSemanticInput.bind(this);
  chooseColumn = () => {
    const columns = this.props.dashboard.columns.map((column, index) => {
      let apps = 0;
      column.map(item => {
        let tmp = this.props.dashboard.profiles[item].app_ids.length / 3;
        if (tmp <= Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 1;
        else if (tmp > Number(tmp.toFixed(0)))
          tmp = Number(tmp.toFixed(0)) + 2;
        apps = apps + tmp;
      });
      if (apps > 0)
        return apps;
      else
        return 0;
    });
    let columnChoose = null;
    columns.map((column, index) => {
      let test = columns.slice();
      test.sort();
      if (column === test[0] && columnChoose === null)
        columnChoose = index;
    });
    return columnChoose;
  };
  selectProfile = () => {
    let profileChoose = null;
    Object.keys(this.props.profiles).map(item => {
      if (profileChoose === null && this.props.profiles[item].team_id === -1)
        profileChoose = this.props.profiles[item].id;
    });
    this.setState({
      selectedProfile: profileChoose !== null ? profileChoose : 0,
      selectedTeam: -1,
      selectedRoom: -1,
      check: 'newApp',
      error: ""
    });
  };
  selectRoom = (roomId) => {
    this.setState({selectedRoom: Number(roomId), checkRoom: roomId});
  };
  selectTeam = (teamId) => {
    this.setState({selectedTeam: teamId, check: teamId, selectedRoom: -1, checkRoom: null, selectedProfile: -1});
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
  confirm = () => {
    if (this.state.selectedRoom === -1 && this.state.selectedProfile !== -1) {
      this.setState({errorMessage: '', loading: true});
      if (this.state.selectedProfile === 0) {
        this.props.dispatch(createProfile({name: 'Me', column_index: this.chooseColumn()})).then(response => {
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
          this.props.dispatch(appAdded({
            app: app,
            from: "Catalog"
          }));
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
  };
  render(){
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Setup your App'}>
          <ChooseAppLocationModal
            bookmark={true}
            confirm={this.confirm}
            check={this.state.check}
            appName={this.state.name}
            loading={this.state.loading}
            selectRoom={this.selectRoom}
            selectTeam={this.selectTeam}
            checkRoom={this.state.checkRoom}
            selectProfile={this.selectProfile}
            website={{logo: this.state.img_url}}/>
        </SimpleModalTemplate>
    )
  }
}

export default withRouter(AddBookmarkModal);