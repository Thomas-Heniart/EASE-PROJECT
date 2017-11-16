import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Button, Form, Message} from 'semantic-ui-react';
import {showDepartureDateEndModal} from "../../actions/teamModalActions";
import {deleteTeamUser, editTeamUserDepartureDate} from "../../actions/userActions";
import {basicDateFormat} from "../../utils/utils";
import {connect} from "react-redux";

@connect(store => ({
  team: store.teams[store.teamModals.reactivateTeamUserModal.team_id],
  team_user_id: store.teamModals.reactivateTeamUserModal.team_user_id
}))
class DepartureDateEndModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: '',
      user: this.props.team.team_users[this.props.team_user_id]
    }
  }
  unfreeze = () => {
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editTeamUserDepartureDate({
      team_id: this.props.team.id,
      team_user_id: this.state.user.id,
      departure_date: null
    })).then(() => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(deleteTeamUser({
      team_id: this.props.team.id,
      team_user_id: this.state.user.id
    })).then(() => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showDepartureDateEndModal({active: false}));
  };
  render() {
    const user = this.state.user;

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={`${user.username}'s account is frozen`}>
          <Form class="container" error={!!this.state.errorMessage.length} onSubmit={this.confirm}>
            <Form.Field>
              <strong class="capitalize">{user.username}</strong>’s account has been frozen because the
              departure date was set on {basicDateFormat(user.departure_date)}. You can now decide to unfreeze the account or
              confirm the departure.
            </Form.Field>
            <Form.Field style={{textAlign: 'right'}}>
              <button class="button-unstyle inline-text-button" type="button" onClick={this.unfreeze}>Unfreeze Account</button>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                class="modal-button uppercase"
                content={'CONFIRM DEPARTURE'}/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = DepartureDateEndModal;