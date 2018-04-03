var React = require('react');
var classnames = require('classnames');
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Button, Form, Message} from 'semantic-ui-react';
import {showTeamDeleteAppModal} from "../../actions/teamModalActions"
import {deleteTeamCard} from "../../actions/appsActions"

import {
  selectChannelFromListById,
  selectUserFromListById
} from "../../utils/helperFunctions"

import {connect} from "react-redux"

@connect((store)=>({
  app_id: store.teamModals.teamDeleteAppModal.app_id,
  team_apps: store.team_apps
}))
class TeamDeleteAppModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      confirmed: false,
      app: this.props.team_apps[this.props.app_id]
    };
  }
  confirmModal = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    const app = this.state.app;
    this.props.dispatch(deleteTeamCard({
      team_id: app.team_id,
      team_card_id: app.id
    })).then(response => {
      this.close();
    });
  };
  confirm = () => {
    this.setState({loading: false, confirmed: !this.state.confirmed});
  };
  close = () => {
    this.props.dispatch(showTeamDeleteAppModal({active: false}));
  };
  render(){
    const app = this.state.app;
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamDeleteAppModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_pin_to_dashboard">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamDeleteAppModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Delete an app
            </div>
            <div class="row display-flex align_items_center" style={{paddingTop: '20px'}}>
              <div class="squared_image_handler">
                <img src={app.website !== undefined ? app.website.logo : app.logo} alt="Website logo"/>
              </div>
              <span style={{fontSize: "1.3rem"}}>{app.name}</span>
            </div>
            <div class="row display-flex flex_direction_column" style={{padding: '20px',fontSize:".9rem"}}>
              <span style={{marginBottom: "20px"}}>Are you sure you want to delete <strong>{app.name}</strong> ?</span>
              <span>If yes:</span>
              <ul style={{marginBottom: 0}}>
                <li>All people using this App will loose the access</li>
                <li>All information related to it will be removed from Ease.space</li>
                <li>You won't be able to restore the App once deleted.</li>
              </ul>
            </div>
            <div class="row display-flex align_items_center" style={{fontSize: ".9rem", marginBottom:"20px"}}>
              <input style={{margin: "0 15px 0 22px"}} type="checkbox" id="confirm" checked={this.state.confirmed} onClick={this.confirm}/>
              <label htmlFor="confirm" style={{margin: 0, fontWeight: 'normal'}}>
                Yes, I am absolutely sure
              </label>
            </div>
            <Button negative
                    onClick={this.confirmModal}
                    loading={this.state.loading}
                    class="row button-unstyle negative_button big_validate_button accept_user_room"
                    disabled={!this.state.confirmed || this.state.loading}>
              DELETE
            </Button>
          </div>
        </div>
    )
  }
}

module.exports = TeamDeleteAppModal;