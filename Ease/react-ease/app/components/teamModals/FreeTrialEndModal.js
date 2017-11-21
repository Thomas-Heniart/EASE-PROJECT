import React, {Component} from "react";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {selectItemFromListById, isOwner} from "../../utils/helperFunctions";
import {objectToList} from "../../utils/utils";
import post_api from "../../utils/post_api";
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";

@connect(store => ({
  teams: store.teams
}))
class FreeTrialEndModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  confirm = (e) => {
    e.preventDefault();
    const team = this.props.teams[this.props.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meOwner = isOwner(me.role);

    if (meOwner)
      this.props.history.push(`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}/settings/payment`);
    else {
      this.setState({loading: true, errorMessage: ''});
      post_api.teams.askOwnerForBilling({team_id: this.props.team_id}).then(response => {
        window.location.href= '/';
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    }
  };
  render(){
    const team = this.props.teams[this.props.team_id];
    const me = team.team_users[team.my_team_user_id];
    const meOwner = isOwner(me.role);
    const teamOwner = objectToList(team.team_users).find(item => (isOwner(item.role)));

    return (
        <div class="popupHandler myshow" style={{zIndex: '10'}}>
          <div class="popover_mask"/>
          <div class="ease_popup ease_team_popup">
            <button class="button-unstyle action_button close_button" onClick={e => {window.location.href= '/'}}>
              <i class="fa fa-times"/>
            </button>
            <Header as="h3" attached="top">
              Free trial ended
            </Header>
          <Form style={{color: "#96a1b9"}} class="container" onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
            <Form.Field>
              {meOwner ?
                  <p>Your free trial on Pro is over! Now you can choose to update your billing info or leave it like this. Not updating your billing info will remain access blocked, for you and your team members, to {this.props.team_name}.</p> :
                  <p>Your free trial on Pro is over! You can let your Team Owner {teamOwner.username} know you would like to access your team, we will pass the message along.</p>}
              {meOwner &&
              <p>Pro is billed 3,99€ per month per active user.</p>}
              <p>
                Want to see all Pro features? <a style={{textDecoration: 'underline'}} href="/pricing" target="_blank">Visit our website</a>.
              </p>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                loading={this.state.loading}
                positive
                class="modal-button uppercase"
                content={meOwner ? 'Update billing information' : 'Ask team update'}/>
          </Form>
          </div>
        </div>
    )
  }
}

module.exports = withRouter(FreeTrialEndModal);