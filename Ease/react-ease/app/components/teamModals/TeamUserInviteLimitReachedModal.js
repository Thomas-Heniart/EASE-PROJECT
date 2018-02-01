import React, {Component, Fragment} from "react";
import {showTeamUserInviteLimitReachedModal} from "../../actions/teamModalActions";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {upgradePlan} from "../../actions/teamActions";
import {connect} from "react-redux";

@connect(store => ({
  team_id: store.teamModals.teamUserInviteLimitReachedModal.team_id
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
  confirm = (e) => {
    e.preventDefault();
  };
  close = () => {
    this.props.dispatch(showTeamUserInviteLimitReachedModal({
      active: false
    }));
  };
  render(){
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
              You reached the limit of 15 persons in a team. The following person(s) won’t be able to join your team.
            </Form.Field>
            <Form.Field>
              agathe@robocop.io
            </Form.Field>
            <Form.Field>
              To send all invitations, try Pro or <u>referre to friends</u>.
            </Form.Field>
            <Form.Field>
              With Pro, you get unlimited seats and additional security features. Pro is 59€/month before VAT for the whole team.
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                class="modal-button uppercase"
                content={'TRY FREE FOR 1 MONTH'}/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default TeamUserInviteLimitReachedModal;