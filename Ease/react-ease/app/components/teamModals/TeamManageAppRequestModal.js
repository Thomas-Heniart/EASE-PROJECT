var React = require('react');
var classnames = require('classnames');
import {showTeamManageAppRequestModal} from "../../actions/teamModalActions";
import {teamShareApp, deleteJoinAppRequest} from "../../actions/appsActions";
import {
    selectChannelFromListById,
    selectUserFromListById,
    findMeInReceivers
} from "../../utils/helperFunctions"

import {connect} from "react-redux"

@connect((store)=>{
  return {
    modal: store.teamModals.teamManageAppRequestModal,
    channels: store.channels.channels,
    users: store.users.users
  };
})
class TeamManageAppRequestModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      users : []
    };
    this.state.users = this.props.modal.app.sharing_requests.map(item => {
      return {
        user: selectUserFromListById(this.props.users, item),
        accepted: false,
        checked: false
      }
    }, this);
    this.confirmModal = this.confirmModal.bind(this);
    this.acceptUser = this.acceptUser.bind(this);
    this.refuseUser = this.refuseUser.bind(this);
  }
  acceptUser(id){
    this.props.dispatch(teamShareApp(this.props.modal.app.id, {team_user_id: id, can_see_information: false})).then(() => {
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
    this.props.dispatch(deleteJoinAppRequest(this.props.modal.app.id, id)).then(() => {
      const users = this.state.users.map(item => {
        if (item.user.id === id)
          item.checked = true;
        return item;
      });
      this.setState({users: users});
    });
  }
  confirmModal(){
    this.props.dispatch(showTeamManageAppRequestModal(false));
  }
  render(){
    const app = this.props.modal.app;
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamManageAppRequestModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_app_requests">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamManageAppRequestModal(false))}}>
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