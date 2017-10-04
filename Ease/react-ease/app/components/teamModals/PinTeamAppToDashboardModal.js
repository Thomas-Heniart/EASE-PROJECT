var React = require('react');
var classnames = require('classnames');
var api = require('../../utils/api');
import {dashboard} from "../../utils/post_api";
import {showPinTeamAppToDashboardModal} from "../../actions/teamModalActions"
import {teamAppPinToDashboard, teamPinLinkApp} from "../../actions/appsActions"
import {findMeInReceivers} from "../../utils/helperFunctions"
import {connect} from "react-redux"

@connect((store)=>{
  return {
    modal: store.teamModals.pinTeamAppToDashboardModal,
    me: store.users.me,
    team_id: store.team.id
  };
})
class PinTeamAppToDashboardModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      name: this.props.modal.app.name,
      nameModifying: false,
      profiles: [],
      profileName: '',
      selectedProfile: -2
    };
    const meReceiver = findMeInReceivers(this.props.modal.app.receivers, this.props.me.id);
      if (meReceiver !== null && meReceiver.profile_id > -1)
      this.state.selectedProfile = meReceiver.profile_id;
    this.handleInput = this.handleInput.bind(this);
    this.selectProfile = this.selectProfile.bind(this);
    this.setNameModifying = this.setNameModifying.bind(this);
    this.confirmModal = this.confirmModal.bind(this);
  }
  createProfile = () => {
    dashboard.createProfile({name: this.state.profileName}).then(response => {
      let profiles = this.state.profiles.slice();
      profiles.push(response);
      this.setState({profiles: profiles, profileName: '', selectedProfile: response.id});
    }).catch(err => {
      //do something
    });
  };
  confirmModal(){
    const meReceiver = findMeInReceivers(this.props.modal.app.receivers, this.props.me.id);
    if (meReceiver.profile_id === -1 && this.state.selectedProfile === -1){
      this.props.dispatch(showPinTeamAppToDashboardModal(false));
      return;
    }
    if (this.props.modal.app.type === 'link'){
      this.props.dispatch(teamPinLinkApp({
        team_id: this.props.team_id,
        app_id: this.props.modal.app.id,
        app_name: this.state.name,
        profile_id: this.state.selectedProfile
      })).then(() => {
        this.props.dispatch(showPinTeamAppToDashboardModal(false));
      });
      return;
    }
    this.props.dispatch(teamAppPinToDashboard(meReceiver.shared_app_id, this.state.selectedProfile, this.state.name, this.props.modal.app.id)).then(response => {
      this.props.dispatch(showPinTeamAppToDashboardModal(false));
    });
  }
  setNameModifying(){
    this.setState({nameModifying: true});
  }
  selectProfile(id){
    this.setState({selectedProfile: id});
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  componentWillMount(){
    api.dashboard.fetchProfiles().then(response => {
      this.setState({profiles: response});
    });
  }
  render(){
    const app = this.props.modal.app;
    const profiles = this.state.profiles.map(function (profile) {
      return (
          <span key={profile.id}
                class={classnames('display-flex', 'align_items_center', 'profile', this.state.selectedProfile === profile.id ? 'active':null)}
                onClick={this.selectProfile.bind(null, profile.id)}>
            <strong class="mrgnRight5 name">{profile.name}</strong>
            <em class="overflow-ellipsis">
              {
                profile.apps.map(function(app, idx){
                  var ret = app.name;
                  ret += (idx === profile.apps.length - 1) ? '' : ', ';
                  return (ret)
                }, this)
              }
            </em>
          </span>
      )
    }, this);
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showPinTeamAppToDashboardModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_pin_to_dashboard">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showPinTeamAppToDashboardModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Choose app location
            </div>
            <div class="row display-flex align_items_center" style={{paddingTop: '20px'}}>
              <div class="squared_image_handler">
                  <img src={app.website !== undefined ? app.website.logo : app.logo} alt="Website logo"/>
              </div>
              {!this.state.nameModifying ?
                <div>
                  <span style={{fontSize: "1.3rem"}}>{app.name}</span>
                  <button class="button-unstyle action_button mrgnLeft5"
                          onClick={this.setNameModifying}>
                    <i class="fa fa-pencil"/>
                  </button>
                </div>
                  :
                <input type="text" placeholder="App name..." name="name"
                       class="input_unstyle modal_input name_input"
                       autoFocus={true}
                      value={this.state.name}
                      onChange={this.handleInput}/>
              }
            </div>
            <div class="row" style={{padding: '20px 30px 30px 30px'}}>
              <span style={{fontSize:".9rem"}}>Choose app location</span>
              <div class="display-flex profiles flex_direction_column">
                {profiles}
                <span class="display-flex profile" class={classnames('display-flex', 'profile', this.state.selectedProfile === -1 ? 'active':null)}
                      onClick={this.selectProfile.bind(null, -1)}>
                  <strong class="mrgnRight5 name">Do not pin to dashboard</strong>
                </span>
                <div class="profile_input">
                  <input type="text" class="input_unstyle" name="profileName" placeholder="Create new group"
                         value={this.state.profileName}
                         onChange={this.handleInput}/>
                  {this.state.profileName.length > 0 &&
                  <button class="button-unstyle" onClick={this.createProfile}>
                    Create
                  </button>
                  }
                </div>
              </div>
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    disabled={this.state.selectedProfile === -2 || this.state.name.length === 0}
                    onClick={this.confirmModal}>
              CONFIRM
            </button>
          </div>
        </div>
    )
  }
}

module.exports = PinTeamAppToDashboardModal;