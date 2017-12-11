var React = require('react');
var classnames = require('classnames');
import {showTeamManageAppRequestModal} from "../../actions/teamModalActions";
import {
  teamShareApp,
  deleteJoinAppRequest,
  acceptTeamCardRequest,
  teamAcceptSharedApp, deleteTeamCardRequest
} from "../../actions/appsActions";

import {connect} from "react-redux";

@connect((store)=>({
  teams : store.teams,
  team_card: store.team_apps[store.teamModals.teamManageAppRequestModal.team_card_id]
}))
class TeamManageAppRequestModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      users : [],
      team_card: this.props.team_card
    };
    const team = this.props.teams[this.state.team_card.team_id];
    this.state.users = this.state.team_card.requests.map(request => {
      return {
        user: team.team_users[request.team_user_id],
        accepted: false,
        checked: false
      }
    }, this);
    this.confirmModal = this.confirmModal.bind(this);
    this.acceptUser = this.acceptUser.bind(this);
    this.refuseUser = this.refuseUser.bind(this);
  }
  acceptUser(id){
    const card = this.state.team_card;
    const request = card.requests.find(request => (request.team_user_id === id));

    this.props.dispatch(acceptTeamCardRequest({
      team_id: card.team_id,
      team_card_id: card.id,
      request_id: request.id
    })).then(() => {
      const users = this.state.users.map(item => {
        if (item.user.id === id){
          item.checked = true;
          item.accepted = true;
        }
        return item;
      });
      this.setState({users : users});
    });
  }
  refuseUser(id){
    this.props.dispatch(deleteTeamCardRequest({
      team_card_id: this.state.team_card.id,
      request_id: this.state.team_card.requests.find(request => (request.team_user_id === id)).id
    })).then(() => {
      const users = this.state.users.map(item => {
        if (item.user.id === id)
          item.checked = true;
        return item;
      });
      this.setState({users: users});
    });
  }
  confirmModal(){
    this.close();
  }
  close = () => {
    this.props.dispatch(showTeamManageAppRequestModal({active: false}));
  };
  render(){
    const app = this.state.team_card;

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={this.close}/>
          <div class="ease_popup ease_team_popup" id="modal_app_requests">
            <button class="button-unstyle action_button close_button" onClick={this.close}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Who do you allow to join ?
            </div>
            <div class="row display-flex flex_direction_column" style={{padding: '20px',fontSize:".9rem"}}>
              <span style={{marginBottom: "20px"}}>People would like to access <strong>{app.name}</strong> ?</span>
              <div class="display-flex flex_direction_column">
                {this.state.users.map(item => {
                  return (
                      <div class="display-flex" key={item.user.id}>
                        <span class="full_flex">{item.user.username} - {item.user.first_name} {item.user.last_name}</span>
                        {!item.checked ?
                            <div class="buttons">
                              <button class="button-unstyle only-text-button underlineOnHover accept"
                                      onClick={this.acceptUser.bind(null, item.user.id)}>
                                Accept
                              </button>
                              <button class="button-unstyle only-text-button underlineOnHover refuse" onClick={this.refuseUser.bind(null, item.user.id)}>
                                Refuse
                              </button>
                            </div>
                            :
                            <div class="buttons display-flex align_items_center">
                              {item.accepted ?
                                  <i class="fa fa-check"/>
                                  :
                                  <i class="fa fa-times"/>}
                            </div>
                        }
                      </div>
                  )
                })}
              </div>
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    onClick={this.confirmModal}>
              DONE
            </button>
          </div>
        </div>
    )
  }
}

module.exports = TeamManageAppRequestModal;