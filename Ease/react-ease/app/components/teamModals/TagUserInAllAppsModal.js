import React from "react";
import classnames from "classnames";
import {connect} from "react-redux";
import {Button} from 'semantic-ui-react';
import {reflect} from "../../utils/utils";
import {teamShareCard} from "../../actions/appsActions";
import {addTeamUserToChannel} from "../../actions/channelActions";
import {showTagUserInAllAppsModal} from "../../actions/teamModalActions";

@connect((store)=>({
  teams : store.teams,
  team_apps: store.team_apps,
  user_id: store.teamModals.tagUserInAllAppsModal.user_id,
  room: store.teams[store.teamModals.tagUserInAllAppsModal.team_id].rooms[store.teamModals.tagUserInAllAppsModal.room_id]
}))
class TagUserInAllAppsModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      user: this.props.teams[this.props.room.team_id].team_users[this.props.user_id],
      selected: null,
      loading: false
    };
  }
  confirmModal = () => {
    this.setState({loading: true});
    this.props.dispatch(addTeamUserToChannel({
      team_id: this.props.room.team_id,
      channel_id: this.props.room.id,
      team_user_id: this.state.user.id
    })).then(res => {
      let calls = [];
      if (this.state.selected === 1) {
        this.props.room.team_card_ids.map(card_id => {
          calls.push(this.props.dispatch(teamShareCard({
            type: this.props.team_apps[card_id].type,
            team_id: this.props.room.team_id,
            team_card_id: card_id,
            team_user_id: this.state.user.id,
            account_information: {}
          })));
        });
      }
      Promise.all(calls.map(reflect)).then(response => {
        this.setState({loading: false}, this.close());
      });
    });
  };
  close = () => {
    this.props.dispatch(showTagUserInAllAppsModal({active: false}));
  };
  render(){
    return (
      <div class="popupHandler myshow">
        <div class="popover_mask" onClick={this.close}/>
        <div class="ease_popup ease_team_popup" id="modal_app_requests">
          <button class="button-unstyle action_button close_button" onClick={this.close}>
            <i class="fa fa-times"/>
          </button>
          <div class="row title-row text-center">
            User accesses
          </div>
          <div class="row display-flex flex_direction_column" style={{padding: '20px',fontSize:".9rem"}}>
            <span style={{marginBottom: "20px"}}>
              Would you like to add {this.state.user.username} in all apps of #{this.props.room.name} ?
            </span>
            <div class="display-flex flex_direction_column">
              <div class="display-flex">
                <div class="buttons">
                  <button
                    class={classnames("button-unstyle only-text-button underlineOnHover",
                      this.state.selected === null ? "accept" : null,
                      this.state.selected === 1 ? 'accept selected' : null)}
                    onClick={e => this.setState({selected: 1})}>
                    Yes
                  </button>
                  <button
                    class={classnames("button-unstyle only-text-button underlineOnHover",
                      this.state.selected === null ? "refuse" : null,
                      this.state.selected === 0 ? 'refuse selected' : null)}
                    onClick={e => this.setState({selected: 0})}>
                    No
                  </button>
                </div>
              </div>
            </div>
          </div>
          <Button positive
                  onClick={this.confirmModal}
                  loading={this.state.loading}
                  class="row button-unstyle big_validate_button accept_user_room"
                  disabled={this.state.loading || this.state.selected === null}>
            DONE
          </Button>
        </div>
      </div>
    )
  }
}

export default TagUserInAllAppsModal;