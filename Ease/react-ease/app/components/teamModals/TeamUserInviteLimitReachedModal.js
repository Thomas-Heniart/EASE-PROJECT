import React, {Component, Fragment} from "react";
import {showTeamUserInviteLimitReachedModal} from "../../actions/teamModalActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {withRouter} from "react-router-dom";
import {upgradePlan} from "../../actions/teamActions";
import {connect} from "react-redux";

@connect(store => ({
  team: store.teams[store.teamModals.teamUserInviteLimitReachedModal.team_id],
  team_user_id_list: store.teamModals.teamUserInviteLimitReachedModal.team_user_id_list
}))
class TeamUserInviteLimitReachedModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false,
      show_more: false
    }
  }
  toggleShowMore = () => {
    this.setState({show_more: !this.state.show_more});
  };
  goToReferralSection = () => {
    const {history} = this.props;
    this.close();
    history.replace(`${this.props.location.pathname}/settings/referral`);
  };
  confirm = (e) => {
    e.preventDefault();
    const {team} = this.props;

    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(upgradePlan({
      team_id: team.id,
      plan_id: 1
    })).then(response => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showTeamUserInviteLimitReachedModal({
      active: false
    }));
  };
  render(){
    const {team, team_user_id_list} = this.props;
    const uninvited_users = team_user_id_list
        .filter(team_user_id => (!(team.team_users[team_user_id].invitation_sent)))
        .map(team_user_id => {
          return team.team_users[team_user_id];
        });
    return (
        <SimpleModalTemplate
            headerContent="You reached the limit"
            onClose={this.close}>
          <Form
              class="container"
              error={this.state.errorMessage.length > 0}
              onSubmit={this.confirm}
              id="team_user_invite_limit_reached_modal">
            <Form.Field>
              You reached the limit of 10 persons in a team. The following person(s) won’t be able to join your team.
            </Form.Field>
            <Form.Field class="display_flex flex_direction_column users">
              {uninvited_users.map((team_user, idx) => {
                if (idx > 5 && !this.state.show_more)
                  return null;
                return (
                    <span key={team_user.id}>
                      <Icon name="user"/>
                      {team_user.email}
                    </span>
                )
              })}
              {uninvited_users.length > 5 && !this.state.show_more &&
              <u onClick={this.toggleShowMore}>Show more</u>}
              {uninvited_users.length > 5 && this.state.show_more &&
              <u onClick={this.toggleShowMore}>Show less</u>}
            </Form.Field>
            <Form.Field>
              To send all invitations, try Pro below or <u onClick={this.goToReferralSection}>referre to friends</u>.
            </Form.Field>
            <Form.Field>
              With Pro, you get unlimited seats and additional security features. Pro is 59€/month before VAT for the whole team.
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                disabled={this.state.loading}
                loading={this.state.loading}
                class="modal-button uppercase"
                content={'TRY PRO FREE FOR 30 DAYS'}/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default withRouter(TeamUserInviteLimitReachedModal);